package scientifik.simulation.simba.core

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import org.apache.commons.math3.random.RandomGenerator
import scientific.simba.physics.Particle
import scientifik.kmath.chains.Chain
import scientifik.kmath.operations.Space
import java.util.concurrent.atomic.AtomicLong

interface Step<T>{
    val secondaries : List<T>
}

//interface EventSpace<T: Event> : Space<T>

interface Track<T> {
    val id: Long
    val parentId: Long
    val item: T
    val steps: Flow<Step<T>>
}

interface TrackAcceptor<T>  {
    fun accept(track: Track<T>): Boolean
}

interface PrimaryGenerator<T> : Chain<Flow<T>>

interface TrackPropagator<T> {
    fun propagate(rnd: RandomGenerator, track: Track<T>): Flow<Step<T>>
}

//interface Publisher<T> {
//
//    val subscribers: MutableSet<(T) -> Unit>
//
//    fun send(message: T) {
//        subscribers.forEach { it.invoke(message) }
//    }
//}



interface Event<T> {
    val id: Long
    val tracks: Flow<Track<T>>
}


class STrack<T>(
    override val id: Long,
    override val parentId: Long,
    override val item: T,
    private val trackPropagator: TrackPropagator<T>,
    val rnd: RandomGenerator
) : Track<T> {

    override val steps: Flow<Step<T>> = trackPropagator.propagate(rnd, this@STrack)
}




class SEvent<T>(
    override val id: Long,
    private val primaries: Flow<T>,
    private val trackAcceptor: suspend (Track<T>) -> Boolean,
    private val trackPropagator: TrackPropagator<T>,
    val rnd: RandomGenerator
) : Event<T> {
    private val trackCounter = AtomicLong(0)


    suspend fun Track<T>.subscribeBySecondaries(collector: FlowCollector<Track<T>>){
        steps.onEach {
            step ->
            step.secondaries.forEach {
                val newTrack = STrack(trackCounter.incrementAndGet(), this.id, it, trackPropagator, rnd)
                if (trackAcceptor(newTrack)){
                    newTrack.subscribeBySecondaries(collector)
                    collector.emit(newTrack)
                }
            }
        }
    }

    override val tracks: Flow<Track<T>> = flow<Track<T>> {
            primaries.buffer().collect{
                val newTrack =  STrack(trackCounter.incrementAndGet(), 0, it, trackPropagator, rnd)
                if (trackAcceptor(newTrack)){
                    newTrack.subscribeBySecondaries(this)
                    emit(newTrack)
                }
            }
    }



//    @UseExperimental(InternalCoroutinesApi::class)
//    suspend fun compute(): R {
//        val primaryTrack = flow {
//            primaries.buffer().collect {
//                emit(Track(trackCounter.incrementAndGet(), 0L, it))
//            }
//        }
//        //FIXME(CoroutineScope)
//        startIteration(primaryTrack, GlobalScope)
//
//        return tempResulter()
//    }
//
//    private suspend fun startIteration(tracks: Flow<Track<T>>, coroutineScope: CoroutineScope) {
//        tracks
////                .flowOn(Dispatchers.Default) FIXME()
//                .filter(trackAcceptor)
//                .map{
//                    track ->
//                    trackPropagator
//                            .propagate(rnd, track)
//                            .map{Track(trackCounter.incrementAndGet(), track.id, it)}
////                        trackPostAction(track)
//                    }
//                .map { startIteration(it, coroutineScope) }
//                .launchIn(coroutineScope) // FIXME()
//    }


}

class EventGenerator<T>(
    private val eventCounter: AtomicLong = AtomicLong(0),
    private val primaryGenerator: PrimaryGenerator<T>,
    private val trackAcceptor: suspend (Track<T>) -> Boolean,
    private val trackPropagator: TrackPropagator<T>,
    val rnd: RandomGenerator
) : Chain<Event<T>> {
    override fun fork(): Chain<Event<T>> {
        return this
    }


    override suspend fun next(): Event<T> {
        val primaries = primaryGenerator.next()
        val event = SEvent<T>(
            eventCounter.getAndIncrement(),
            primaries,
            trackAcceptor,
            trackPropagator,
            rnd
        )
        return event
    }

}

interface Simulation<T> {
    fun build(): SimulationChain<T>
}


class SimulationChain<T>(
    val space: Space<T>,
    val eventGenerator: Chain<T>
) : Chain<T> {
    override fun fork(): Chain<T> {
        return SimulationChain(space, eventGenerator.fork())
    }

    private val mutex = Mutex()
    private var number = AtomicLong(0)
    val numberOfIteration: Long
        get() = number.get()
    private var _result: T = space.zero
    var result: T = space.zero
        get() = with(space) {
            _result / number.get()
        }
        private set


    override suspend fun next(): T {
        val newValue = eventGenerator.next()
        mutex.withLock {
            with(space) { _result += newValue }
            number.incrementAndGet()
            return newValue
        }
    }


}

