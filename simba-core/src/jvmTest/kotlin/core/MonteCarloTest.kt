package scientifik.simulation.simba.core


import hep.dataforge.context.Global
import hep.dataforge.context.logger
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.modules.serializersModuleOf
import kscience.kmath.chains.Chain
import kscience.kmath.geometry.Vector3D
import kscience.kmath.prob.RandomGenerator
import simba.core.EventGenerator
import simba.core.PrimaryGenerator
import simba.core.SimpleTrackPropagator
import simba.geometry.Orb
import simba.physics.ClassicParticle
import simba.physics.Particle
import simba.math.*
import kotlin.test.Test


val rnd = RandomGenerator.default

val logger = Global.logger

class MonteCarloTest {

    @Test
    fun test(){
        val eventGenerator = EventGenerator(
            trackPropagator = SimpleTrackPropagator(
                Orb(5.0),
                0.010,
                {1/it.kineticEnergy},
                {it.momentumDirection}
            ),
            rnd = rnd,
            primaryGenerator = object : PrimaryGenerator<Particle>{
                override fun fork(): Chain<Flow<Particle>> {
                    return this
                }

                override suspend fun next(): Flow<Particle> {
                    return flow{emit(ClassicParticle(1.0, 0.01, Vector3D(1.0, 0.0, 0.0), Vector3D(0.0, 0.0, 0.0)))}
                }

            }
        )

        runBlocking {
            repeat(5) {
                val id = it
                eventGenerator.next().tracks
                    .onStart { logger.info { "Start event $id" } }
                    .onEach { logger.info { "Start track ${it.id}" } }
                    .onCompletion { logger.info { "Stop event $id" } }
                    .collect{
                        val particle = it.item
                        it.steps.onEach {
                            val x = particle.position.x
                            val y = particle.position.y
                            val z = particle.position.z
                            val dx = it.shift.x
                            val dy = it.shift.y
                            val dz = it.shift.z
                            println("Position: $x, $y, $z. Shift: $dx, $dy, $dz.")
                        }.collect {  }
                        logger.info { "Stop track ${it.id}" }
                    }

            }
        }

    }
}