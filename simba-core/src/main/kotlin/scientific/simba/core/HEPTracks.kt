package scientific.simba.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import org.apache.commons.math3.random.RandomGenerator
import scientific.simba.geometry.Solid
import scientific.simba.geometry.UniformVolume
import scientific.simba.physics.*
import scientifik.simulation.simba.core.Track
import scientifik.simulation.simba.core.TrackPropagator
import kotlin.math.ln



class SimpleTrackPropagator(
        val solid: Solid,
        val concentration: Double,
        val crossSection : (Particle) -> Double,
        val angularDistribution: (Particle) -> Vector3D
) : TrackPropagator<Particle>{
    override suspend fun propagate(rnd : RandomGenerator, track: Track<Particle>): Flow<Particle> {
        return flow{
            val particle = track.item
            do {
                val sigma = crossSection(particle)
                val MFP = 1/(sigma*concentration)
                val range = -MFP*ln(rnd.nextDouble())
                particle.move(range)
                particle.momentumDirection = angularDistribution(particle)
            } while (solid.inSolid(particle.position))

        }
    }

}


//
//class SimpleHEPTrackPropagator(
//        val volume : UniformVolume,
//        val processes : List<LongStepPhysicsProcess>,
//        val rnd: RandomGenerator
//) : TrackPropagator<HEPParticle>{
//    override suspend fun propagate(track: Track<HEPParticle>): Flow<HEPParticle> {
//        return flow{
//            val material = volume.material
//            val n_elem = material.elements.size
//            val n_proc = processes.size
//            val particle = track.item
//            val density = volume.density
//            val avogadro = 1 // TODO(Create units undefined const)
//            do{
//                val table = processes
//                        .map {
//                            val n = material.composition.map { it.massFraction *avogadro*density/it.ingredient.Aeff}
//                            val sigma = it.computeMicroscopicCrossSection(particle, material)
//                            sigma.zip(n){si, ni -> ni*si}
//                        }
//                val SigmaElem = List(n_elem){ i -> table.map {it[i]}.sum()}
//                val i_elem = sampleIndx(rnd, SigmaElem)
//                val SigmaProc = table[i_elem]
//                val i_proc = sampleIndx(rnd, SigmaProc)
//                val MFP = 1/SigmaProc[i_proc]
//                particle.move(MFP)
//                val process = processes[i_proc]
//                val element = material.elements[i_elem]
//                process.sampleSecondary(rnd, particle, material, element).forEach {
//                    emit(it)
//                }
//            } while (particle.kineticEnergy > 0)
//        }
//    }
//
//}
//
//



//class Selector(val generator: RandomGenerator) {
//
//    fun selectProcess(SigmaTable: List<List<Double>>, Sigma: Double): Int {
//        val processProbability = SigmaTable.map { it.sum() / Sigma }.toMutableList()
//        for (i in 1..processProbability.size - 1) {
//            processProbability[i] = processProbability[i] + processProbability[i - 1]
//        }
//        assert(processProbability.last() == 1.0)
//        return processProbability.indexOfFirst { it > generator.nextDouble() }
//    }
//
//}
//
//
//class LongStepIntarector(
//        val simulationSettings: SimulationSettings,
//        val volume: Volume , val processes: List<LongStepPhysicsProcess>) : Intarector {
//    val generator = simulationSettings.get<CoreSettings>().generator
//    val selector = Selector(generator)
//    val material: Material = volume.material
//    val densityDistribution: DensityDistribution = volume.densityDistribution
//
//    init {
//        logger.debug { "LongStepIntarector  init: processes.size ${processes.size}" }
//    }
//
//    override fun doIt(eventContext: EventContext, track: Track): ArrayList<Track> {
//
//
//        val SigmaMax = getMaxMacroCrossSection(track.particle)
//        if (SigmaMax == 0.0 ){
//            track.trackStatus = TrackStatus.stop
//            return ArrayList()
//        }
////        val FMP = 1/SigmaMax
////        logger.debug { SigmaMax}
//        var SigmaTable: List<List<Double>>
//        var Sigma: Double
//        var newPosition = track.position
//        var path = 0.0
//
//        logger.debug { "DoIt : start step length calculation" }
//        do {
//            val range = -ln(generator.nextDouble()) / SigmaMax
//            path += range
//            newPosition += track.momentumDirection * range
//            SigmaTable = calculateSigmaTable(track.particle, densityDistribution.getDesnity(newPosition))
//            Sigma = sumList(SigmaTable)
//            logger.debug { "DoIt : Sigma/SigmaMax = ${Sigma / SigmaMax}, ${Sigma}, ${SigmaMax}" }
//
//        } while (Sigma / SigmaMax < generator.nextDouble())
//        logger.debug { "DoIt : end step length calculation" }
//        val newTime = track.globalTime + (path / track.velocity)
//        val indx = selector.selectProcess(SigmaTable, Sigma)
//        val element = selector.selectElement(material, SigmaTable[indx])
//        val secondaries = processes[indx].sampleSecondary(track.particle, material, element)
//        val tracks = ArrayList<Track>()
//        logger.debug { "Time $newTime" }
//        if (secondaries != null) {
//            secondaries.forEach {
//                tracks.add(Track(it, eventContext.trackCounter.incrementAndGet(), track.trackID, newPosition, newTime))
//            }
//        }
//        track.globalTime = newTime
//        track.position = newPosition
//        if (track.kineticEnergy <= 0.0){
//            track.trackStatus = TrackStatus.stop
//        }
//        else{
//            //TODO(Номрмальный выход из объема)
//            if (!volume.inVolume(track.position)){
//                track.trackStatus = TrackStatus.stop
//            }
//        }
//
////        logger.debug { "DoIt end step:  ${track.position}  ${track.kineticEnergy}"  }
////        printTrackInfo(track)
//        return tracks
//    }
//
//    fun calculateSigmaTable(particle: Particle, density: Double): List<List<Double>> {
//        val microCrossSection = processes.map { it.computeMicroscopicCrossSection(particle, material) }
////        val totalCrossSection = microCrossSection.reduce { acc, doubles -> acc + doubles}
//        val concentration = material.ratioWeigthMolMass.map { it * density }
//        return microCrossSection.map { it.zip(concentration).map { it.first * it.second } }
//    }
//
//    fun getMaxMacroCrossSection(particle: Particle): Double {
//        return calculateSigmaTable(particle, densityDistribution.getMaxDensity()).map { it.sum() }.sum()
//    }
//
//
//}