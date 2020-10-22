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

class Step
interface StepProduction

//class Track : Chain<Step>


//interface EventSpace<T: Event> : Space<T>



class Track<T>(
        val id: Long,
        val parentId: Long,
        val item: T
)


interface Publisher<T> {

    val subscribers: MutableSet<(T) -> Unit>

    fun send(message: T) {
        subscribers.forEach { it.invoke(message) }
    }
}

interface TrackAcceptor<T > : Publisher<Track<T>> {
    fun accept(track: Track<T>): Boolean
}

interface PrimaryGenerator<T> : Chain<Flow<T>>

interface TrackPropagator<T > {
    suspend fun propagate(rnd: RandomGenerator, track: Track<T>): Flow<T>
}


class Event<T , R>(
        val id: Long,
        private val primaries: Flow<T>,
        private val trackAcceptor: suspend (Track<T>) -> Boolean,
        private val trackPropagator: TrackPropagator<T>,
        private val tempResulter: () -> R,
        val rnd: RandomGenerator
) {
    private val trackCounter = AtomicLong(0)

    @UseExperimental(InternalCoroutinesApi::class)
    suspend fun compute(): R {
        val primaryTrack = flow {
            primaries.buffer().collect {
                emit(Track(trackCounter.incrementAndGet(), 0L, it))
            }
        }
        //FIXME(CoroutineScope)
        startIteration(primaryTrack, GlobalScope)

        return tempResulter()
    }

    private suspend fun startIteration(tracks: Flow<Track<T>>, coroutineScope: CoroutineScope) {
        tracks
//                .flowOn(Dispatchers.Default) FIXME()
                .filter(trackAcceptor)
                .map{
                    track ->
                    trackPropagator
                            .propagate(rnd, track)
                            .map{Track(trackCounter.incrementAndGet(), track.id, it)}
//                        trackPostAction(track)
                    }
                .map { startIteration(it, coroutineScope) }
                .launchIn(coroutineScope) // FIXME()
    }


}

class EventGenerator<T , R>(
        private val eventCounter: AtomicLong = AtomicLong(0),
        private val primaryGenerator: PrimaryGenerator<T>,
        private val trackAcceptor: suspend (Track<T>) -> Boolean,
        private val trackPropagator: TrackPropagator<T>,
        private val tempResulter: () -> R,
        val rnd : RandomGenerator
//        private val coroutineScope: CoroutineScope
) : Chain<R> {
    override fun fork(): Chain<R> {
        return this
    }


    override suspend fun next(): R {
        val primaries = primaryGenerator.next()
        val event = Event<T, R>(eventCounter.getAndIncrement(), primaries, trackAcceptor, trackPropagator, tempResulter, rnd)//, coroutineScope)
        return event.compute()
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

