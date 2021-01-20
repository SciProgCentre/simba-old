package simba.core

import kotlinx.coroutines.flow.*
import kscience.kmath.chains.Chain
import kscience.kmath.prob.RandomGenerator
import java.util.concurrent.atomic.AtomicLong



class SEvent<T, S : Step<T>>(
    override val id: Long,
    private val primaries: Flow<T>,
    private val trackAcceptor: TrackAcceptor<T, S>,
    private val trackPropagator: TrackPropagator<T,S>,
    val rnd: RandomGenerator
) : Event<T, S> {
    private val trackCounter = AtomicLong(0)


    private suspend fun Track<T, S>.subscribeBySecondaries(collector: FlowCollector<Track<T, S>>){
        steps.onEach {
                step ->
            step.secondaries.forEach {
                val newTrack = PropagatedTrack(trackCounter.incrementAndGet(), this.id, it, trackPropagator, rnd.fork())
                if (trackAcceptor(newTrack)){
                    newTrack.subscribeBySecondaries(collector)
                    collector.emit(newTrack)
                }
            }
        }
    }

    override val tracks: Flow<Track<T, S>> = flow{
        primaries.buffer().collect{
            val newTrack =  PropagatedTrack(trackCounter.incrementAndGet(), 0, it, trackPropagator, rnd.fork())
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
    private val trackAcceptor: TrackAcceptor<T, S> = {true},
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