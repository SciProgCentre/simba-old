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

interface Track<T, S : Step<T>> {
    val id: Long
    val parentId: Long
    val item: T
    val steps: Flow<S>
}

interface TrackAcceptor<T, S : Step<T>>  {
    fun accept(track: Track<T, S>): Boolean
}

interface PrimaryGenerator<T> : Chain<Flow<T>>

interface TrackPropagator<T, S : Step<T>> {
    fun propagate(rnd: RandomGenerator, track: Track<T, S>): Flow<S>
}



interface Event<T, S : Step<T>> {
    val id: Long
    val tracks: Flow<Track<T, S>>
}


class STrack<T, S : Step<T>>(
    override val id: Long,
    override val parentId: Long,
    override val item: T,
    private val trackPropagator: TrackPropagator<T, S>,
    val rnd: RandomGenerator
) : Track<T, S> {

    override val steps: Flow<S> = trackPropagator.propagate(rnd, this@STrack)
}




class SEvent<T, S : Step<T>>(
    override val id: Long,
    private val primaries: Flow<T>,
    private val trackAcceptor: suspend (Track<T,S>) -> Boolean,
    private val trackPropagator: TrackPropagator<T,S>,
    val rnd: RandomGenerator
) : Event<T, S> {
    private val trackCounter = AtomicLong(0)


    suspend fun Track<T, S>.subscribeBySecondaries(collector: FlowCollector<Track<T, S>>){
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

    override val tracks: Flow<Track<T, S>> = flow<Track<T, S>> {
            primaries.buffer().collect{
                val newTrack =  STrack(trackCounter.incrementAndGet(), 0, it, trackPropagator, rnd)
                if (trackAcceptor(newTrack)){
                    newTrack.subscribeBySecondaries(this)
                    emit(newTrack)
                }
            }
    }
}

class EventGenerator<T, S : Step<T>>(
    private val eventCounter: AtomicLong = AtomicLong(0),
    private val primaryGenerator: PrimaryGenerator<T>,
    private val trackAcceptor: suspend (Track<T, S>) -> Boolean,
    private val trackPropagator: TrackPropagator<T, S>,
    val rnd: RandomGenerator
) : Chain<Event<T, S>> {
    override fun fork(): Chain<Event<T, S>> {
        return this
    }


    val numberOfEvents : Long
        get() = eventCounter.get()

    override suspend fun next(): Event<T, S> {
        val primaries = primaryGenerator.next()
        val event = SEvent(
            eventCounter.getAndIncrement(),
            primaries,
            trackAcceptor,
            trackPropagator,
            rnd
        )
        return event
    }

}

//abstract class Simulation<T, S : Step<T>> (private val eventGenerator: Chain<Event<T, S>>){
//
//
//    private val trackAction = HashSet<suspend (Track<T, S>) -> Unit>()
//
//    fun onStep(action: suspend (S) -> Unit) : Simulation<T, S>
//    fun onTrack(action: suspend (Track<T, S>) -> Unit): Simulation<T, S> = this.apply {
//        trackAction.add(action)
//    }
//
//}



//interface Simulation<T> {
//    fun build(): SimulationChain<T>
//}
//
//
//class SimulationChain<T>(
//    val space: Space<T>,
//    val eventGenerator: Chain<T>
//) : Chain<T> {
//    override fun fork(): Chain<T> {
//        return SimulationChain(space, eventGenerator.fork())
//    }
//
//    private val mutex = Mutex()
//    private var number = AtomicLong(0)
//    val numberOfIteration: Long
//        get() = number.get()
//    private var _result: T = space.zero
//    var result: T = space.zero
//        get() = with(space) {
//            _result / number.get()
//        }
//        private set
//
//
//    override suspend fun next(): T {
//        val newValue = eventGenerator.next()
//        mutex.withLock {
//            with(space) { _result += newValue }
//            number.incrementAndGet()
//            return newValue
//        }
//    }
//}

