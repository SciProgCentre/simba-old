package simba.core

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kscience.kmath.chains.Chain
import kscience.kmath.prob.RandomGenerator

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

typealias TrackAcceptor<T, S> = suspend (Track<T,S>) -> Boolean

interface PrimaryGenerator<T> : Chain<Flow<T>>

interface TrackPropagator<T, S : Step<T>> {
    fun propagate(rnd: RandomGenerator, track: Track<T, S>): Flow<S>
}


//interface EventHandler<T, S : Step<T>>{
//    fun handleStep(step: Step<T>) = {}
//    suspend fun handleTrack(track: Track<T, S>) = {
//        track.steps.onEach(::handleStep).collect {  }
//    }
//}

interface Event<T, S : Step<T>> {
    val id: Long
    val tracks: Flow<Track<T, S>>

//    suspend fun compute(handler: EventHandler<T,S>){
//        tracks.
//            onEach { it.steps.onEach(handler::handleStep)}
//        collect() {
//            it.steps.onEach().collect {  }
//        }
//    }
}


class PropagatedTrack<T, S : Step<T>>(
    override val id: Long,
    override val parentId: Long,
    override val item: T,
    private val trackPropagator: TrackPropagator<T, S>,
    val rnd: RandomGenerator
) : Track<T, S> {

    override val steps: Flow<S> = trackPropagator.propagate(rnd, this@PropagatedTrack)
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

