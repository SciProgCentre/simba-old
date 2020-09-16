package scientifik.simulation.simba.core

import kotlinx.coroutines.*
import mu.KotlinLogging
import scientifik.kmath.chains.Chain
import scientifik.kmath.operations.RealField
import kotlin.system.measureTimeMillis
import kotlin.test.Test
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.apache.commons.math3.random.JDKRandomGenerator
import org.apache.commons.math3.random.RandomGenerator

private val logger = KotlinLogging.logger {}


object FakeTrackable

class FakeTrackPropagator : TrackPropagator<FakeTrackable> {
    override suspend fun propagate(rnd: RandomGenerator, track: Track<FakeTrackable>): Flow<FakeTrackable> {
        return flow {
            val n = track.id % 3
            logger.info { "Start propagation of track ${track.id}"}
            delay(1000)
            logger.info { "End propagation of track ${track.id}" }
            repeat(n.toInt()){emit(FakeTrackable)}
        }
    }
}

val rnd = JDKRandomGenerator(0)

class MonteCarloFakeTest {

    @Test
    fun test(){
        runBlocking {

        val eventGenerator = EventGenerator<FakeTrackable, Double>(
                primaryGenerator = object : PrimaryGenerator<FakeTrackable> {
                    override fun fork(): Chain<Flow<FakeTrackable>> {
                        return this
                    }

                    override suspend fun next(): Flow<FakeTrackable> {
                        return flow{
                            (1..100).forEach { emit(FakeTrackable)}
                        }
                    }

                },
                trackAcceptor = {
                    delay(200)
                    if (it.id % 2 == 0L){
                    logger.info{"Accept track ${it.id} which born parent track ${it.parentId}"}
                    true
                } else{
                    logger.info{"Kill track ${it.id} which born parent track ${it.parentId}"}
                    false
                }
                },
                trackPropagator = FakeTrackPropagator(),
                tempResulter = {1.0},
                rnd = rnd
        )

        val simChain = SimulationChain<Double>(RealField, eventGenerator)
            val time  = measureTimeMillis {
                val res = simChain.next()
                logger.info{"Result: ${res}"}
            }
            logger.info{"Time: $time"}

        }
    }
}