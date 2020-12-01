package simba.core

import kotlinx.coroutines.*
import mu.KotlinLogging
import scientifik.kmath.chains.Chain
import kotlin.system.measureTimeMillis
import kotlin.test.Test
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.apache.commons.math3.random.JDKRandomGenerator
import org.apache.commons.math3.random.RandomGenerator

private val logger = KotlinLogging.logger {}


object FakeTrackable

class FakeStep(override val secondaries: List<FakeTrackable>) : Step<FakeTrackable>

class FakeTrackPropagator : TrackPropagator<FakeTrackable, FakeStep> {
    override fun propagate(rnd: RandomGenerator, track: Track<FakeTrackable, FakeStep>): Flow<FakeStep> {
        return flow {
            val n = track.id % 3
            logger.info { "Start propagation of track ${track.id}"}
            delay(1000)
            logger.info { "End propagation of track ${track.id}" }
            repeat(n.toInt()){emit(FakeStep(listOf(FakeTrackable)))}
        }
    }
}

val rnd = JDKRandomGenerator(0)

class MonteCarloFakeTest {

    @InternalCoroutinesApi
    @Test
    fun test(){
        runBlocking {

        val eventGenerator = EventGenerator<FakeTrackable, FakeStep>(
                primaryGenerator = object : PrimaryGenerator<FakeTrackable> {
                    override fun fork(): Chain<Flow<FakeTrackable>> {
                        return this
                    }

                    override suspend fun next(): Flow<FakeTrackable> {
                        return flow{
                            (1..10).forEach { emit(FakeTrackable)}
                        }
                    }

                },
                trackAcceptor = {
                    if (it.id % 2 == 0L){
                    logger.info{"Accept track ${it.id} which born parent track ${it.parentId}"}
                    true
                } else{
                    logger.info{"Kill track ${it.id} which born parent track ${it.parentId}"}
                    false
                }
                },
                trackPropagator = FakeTrackPropagator(),
                rnd = rnd
        )

//        val simChain = SimulationChain<Double>(RealField, eventGenerator)
            val time  = measureTimeMillis {
//                val res = simChain.next()
                    runBlocking {
                        eventGenerator.next().tracks.buffer().map {
                            it.steps.buffer().collect()
                        }.collect()
                    }

//                logger.info{"Result: ${res}"}
            }
            logger.info{"Time: $time"}

        }
    }
}