package scientific.simulation.simba.physics.electromagnetic.models

import hep.dataforge.io.Envelope
import hep.dataforge.io.IOPlugin
import hep.dataforge.meta.MetaItem
import hep.dataforge.tables.RowTable
import hep.dataforge.tables.io.TextRows
import hep.dataforge.tables.io.readEnvelope
import mu.KotlinLogging
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import org.apache.commons.math3.random.RandomGenerator
import scientific.simulation.simba.physics.data.TableDataLoader
import scientific.simulation.simba.physics.particles.Electron
import scientific.simulation.simba.physics.particles.ParticleDefinition
import java.nio.file.Path
import kotlin.math.*

val logger = KotlinLogging.logger {}

//class BraggModel(
//    override val material: Material,
//    override val definition: ParticleDefinition
//) : PhysicalModel, IonisationLoss {
//    val lowestKinEnergy: Double = 1.0 * keV
//    val maxEnergy: Double = 0.0 //FIXME(Подумать откуда должна прийти эта переменная)
//    val cutEnergy: Double = 0.0 //FIXME(где оно?)
//    val maxKinEnergy: Double = 0.0 //FIXME(где оно?)
//
//    val ratio: Double = electron_mass_c2 / definition.mass
//    var useAngularGenerator: Boolean = false
//    var iMolecula: Int = 0 //FIXME(nani?)
//    val protonMassAMU = 1.007276 // FIXME(Config)
//
//    fun MaxSecondaryEnergy(kineticEnergy: Double): Double {
//        val tau: Double = kineticEnergy / definition.mass
//        val tmax: Double = 2.0 * electron_mass_c2 * tau * (tau + 2.0) /
//                (1.0 + 2.0 * (tau + 1.0) * ratio + ratio * ratio)
//        return tmax
//    }
//
//
//    fun DEDX(material: Material, kineticEnergy: Double): Double {
//        var eloss = 0.0
//        var baseMaterial = material //FIXME(временно)
//
//        // check DB
//        //if(material != currentMaterial) {
//        //    currentMaterial = material
//        //    baseMaterial = material->GetBaseMaterial()
//        //    ? material->GetBaseMaterial() : material
//        //    iPSTAR    = -1
//        //    iMolecula = -1
//        //    iICRU90 = fICRU90 ? fICRU90->GetIndex(baseMaterial) : -1
//        //
//        //  if(iICRU90 < 0) {
//        //        iPSTAR = fPSTAR->GetIndex(baseMaterial);
//        //        if(iPSTAR < 0) { HasMaterial(baseMaterial) }
//        //    }
//        //} //FIXME(BaseMaterial: kore ha nani desu ka)
//    }
//
//    fun StoppingPower(material: Material, kineticEnergy: Double){
//        var ionloss = 0.0
//        val fitCoeff: List<Double> = listOf<Double>() // FIXME(Load from DataStorage)
//
//        if (iMolecula >= 0) {
//
//            // The data and the fit from:
//            // ICRU Report N49, 1993. Ziegler's model for protons.
//            // Proton kinetic energy for parametrisation (keV/amu)
//
//            var T: Double = kineticEnergy/(keV*protonMassAMU)
//
////            static const G4float a[11][5] = {
////                {1.187E+1f, 1.343E+1f, 1.069E+4f, 7.723E+2f, 2.153E-2f},
////                {7.802E+0f, 8.814E+0f, 8.303E+3f, 7.446E+2f, 7.966E-3f},
////                {7.294E+0f, 8.284E+0f, 5.010E+3f, 4.544E+2f, 8.153E-3f},
////                {8.646E+0f, 9.800E+0f, 7.066E+3f, 4.581E+2f, 9.383E-3f},
////                {1.286E+1f, 1.462E+1f, 5.625E+3f, 2.621E+3f, 3.512E-2f},
////                {3.229E+1f, 3.696E+1f, 8.918E+3f, 3.244E+3f, 1.273E-1f},
////                {1.604E+1f, 1.825E+1f, 6.967E+3f, 2.307E+3f, 3.775E-2f},
////                {8.049E+0f, 9.099E+0f, 9.257E+3f, 3.846E+2f, 1.007E-2f},
////                {4.015E+0f, 4.542E+0f, 3.955E+3f, 4.847E+2f, 7.904E-3f},
////                {4.571E+0f, 5.173E+0f, 4.346E+3f, 4.779E+2f, 8.572E-3f},
////                {2.631E+0f, 2.601E+0f, 1.701E+3f, 1.279E+3f, 1.638E-2f} };
////
////            static const G4float atomicWeight[11] = {
////                101.96128f, 44.0098f, 16.0426f, 28.0536f, 42.0804f,
////                104.1512f, 44.665f, 60.0843f, 18.0152f, 18.0152f, 12.0f};
//
//            if ( T < 10.0 ) {
//                ionloss = (a[iMolecula][0])) * sqrt(T) ;
//
//            } else if ( T < 10000.0 ) {
//                G4double x1 = (G4double)(a[iMolecula][1]);
//                G4double x2 = (G4double)(a[iMolecula][2]);
//                G4double x3 = (G4double)(a[iMolecula][3]);
//                G4double x4 = (G4double)(a[iMolecula][4]);
//                G4double slow  = x1 * G4Exp(G4Log(T)* 0.45);
//                G4double shigh = G4Log( 1.0 + x3/T  + x4*T ) * x2/T;
//                ionloss = slow*shigh / (slow + shigh) ;
//            }
//
//            ionloss = std::max(ionloss, 0.0);
//            if ( 10 == iMolecula ) {
//                static const G4double invLog10 = 1.0/G4Log(10.);
//
//                if (T < 100.0) {
//                    ionloss *= (1.0+0.023+0.0066*G4Log(T)*invLog10);
//                }
//                else if (T < 700.0) {
//                    ionloss *=(1.0+0.089-0.0248*G4Log(T-99.)*invLog10);
//                }
//                else if (T < 10000.0) {
//                    ionloss *=(1.0+0.089-0.0248*G4Log(700.-99.)*invLog10);
//                }
//            }
//            ionloss /= (G4double)atomicWeight[iMolecula];
//
//            // pure material (normally not the case for this function)
//        } else if(1 == (material->GetNumberOfElements())) {
//        G4double z = material->GetZ() ;
//        ionloss = ElectronicStoppingPower( z, kineticEnergy ) ;
//    }
//
//        return ionloss;
//    }
//
//
//    fun ElectronicStoppingPower(z: Int, kineticEnergy: Double) : Double{
//        var T: Double = kineticEnergy/(keV*protonMassAMU)
//        val fitCoeff: List<Double> = listOf<Double>() // FIXME(Load from DataStorage)
//        var fac = 1.0
//        var ionloss: Double = 0.0
//        // Carbon specific case for E < 40 keV
//        if ( T < 40.0 && z == 6) {
//            fac = sqrt(T*0.025);
//            T = 40.0
//
//            // Free electron gas model
//        } else if ( T < 10.0 ) {
//            fac = sqrt(T*0.1)
//            T = 10.0
//        }
//
//        // Main parametrisation
//        var x1: Double = (fitCoeff[1])
//        var x2: Double = (fitCoeff[2])
//        var x3: Double = (fitCoeff[3])
//        var x4: Double = (fitCoeff[4])
//        var slow: Double  = x1 * exp(ln(T) * 0.45)
//        var shigh: Double = ln( 1.0 + x3/T + x4*T ) * x2/T
//        ionloss = slow*shigh*fac / (slow + shigh)
//
//        ionloss = max(ionloss, 0.0)
//
//        return ionloss
//// Coefficient for fittig TODO(Move to separate datafile)
////        static const G4float a[92][5] = {
////            {1.254E+0f, 1.440E+0f, 2.426E+2f, 1.200E+4f, 1.159E-1f},
////            {1.229E+0f, 1.397E+0f, 4.845E+2f, 5.873E+3f, 5.225E-2f},
////            {1.411E+0f, 1.600E+0f, 7.256E+2f, 3.013E+3f, 4.578E-2f},
////            {2.248E+0f, 2.590E+0f, 9.660E+2f, 1.538E+2f, 3.475E-2f},
////            {2.474E+0f, 2.815E+0f, 1.206E+3f, 1.060E+3f, 2.855E-2f},
////            {2.631E+0f, 2.601E+0f, 1.701E+3f, 1.279E+3f, 1.638E-2f},
////            {2.954E+0f, 3.350E+0f, 1.683E+3f, 1.900E+3f, 2.513E-2f},
////            {2.652E+0f, 3.000E+0f, 1.920E+3f, 2.000E+3f, 2.230E-2f},
////            {2.085E+0f, 2.352E+0f, 2.157E+3f, 2.634E+3f, 1.816E-2f},
////            {1.951E+0f, 2.199E+0f, 2.393E+3f, 2.699E+3f, 1.568E-2f},
////            // Z= 11-20
////            {2.542E+0f, 2.869E+0f, 2.628E+3f, 1.854E+3f, 1.472E-2f},
////            {3.791E+0f, 4.293E+0f, 2.862E+3f, 1.009E+3f, 1.397E-2f},
////            {4.154E+0f, 4.739E+0f, 2.766E+3f, 1.645E+2f, 2.023E-2f},
////            {4.914E+0f, 5.598E+0f, 3.193E+3f, 2.327E+2f, 1.419E-2f},
////            {3.232E+0f, 3.647E+0f, 3.561E+3f, 1.560E+3f, 1.267E-2f},
////            {3.447E+0f, 3.891E+0f, 3.792E+3f, 1.219E+3f, 1.211E-2f},
////            {5.301E+0f, 6.008E+0f, 3.969E+3f, 6.451E+2f, 1.183E-2f},
////            {5.731E+0f, 6.500E+0f, 4.253E+3f, 5.300E+2f, 1.123E-2f},
////            {5.152E+0f, 5.833E+0f, 4.482E+3f, 5.457E+2f, 1.129E-2f},
////            {5.521E+0f, 6.252E+0f, 4.710E+3f, 5.533E+2f, 1.112E-2f},
////            // Z= 21-30
////            {5.201E+0f, 5.884E+0f, 4.938E+3f, 5.609E+2f, 9.995E-3f},
////            {4.858E+0f, 5.489E+0f, 5.260E+3f, 6.511E+2f, 8.930E-3f},
////            {4.479E+0f, 5.055E+0f, 5.391E+3f, 9.523E+2f, 9.117E-3f},
////            {3.983E+0f, 4.489E+0f, 5.616E+3f, 1.336E+3f, 8.413E-3f},
////            {3.469E+0f, 3.907E+0f, 5.725E+3f, 1.461E+3f, 8.829E-3f},
////            {3.519E+0f, 3.963E+0f, 6.065E+3f, 1.243E+3f, 7.782E-3f},
////            {3.140E+0f, 3.535E+0f, 6.288E+3f, 1.372E+3f, 7.361E-3f},
////            {3.553E+0f, 4.004E+0f, 6.205E+3f, 5.551E+2f, 8.763E-3f},
////            {3.696E+0f, 4.194E+0f, 4.649E+3f, 8.113E+1f, 2.242E-2f},
////            {4.210E+0f, 4.750E+0f, 6.953E+3f, 2.952E+2f, 6.809E-3f},
////            // Z= 31-40
////            {5.041E+0f, 5.697E+0f, 7.173E+3f, 2.026E+2f, 6.725E-3f},
////            {5.554E+0f, 6.300E+0f, 6.496E+3f, 1.100E+2f, 9.689E-3f},
////            {5.323E+0f, 6.012E+0f, 7.611E+3f, 2.925E+2f, 6.447E-3f},
////            {5.874E+0f, 6.656E+0f, 7.395E+3f, 1.175E+2f, 7.684E-3f},
////            {6.658E+0f, 7.536E+0f, 7.694E+3f, 2.223E+2f, 6.509E-3f},
////            {6.413E+0f, 7.240E+0f, 1.185E+4f, 1.537E+2f, 2.880E-3f},
////            {5.694E+0f, 6.429E+0f, 8.478E+3f, 2.929E+2f, 6.087E-3f},
////            {6.339E+0f, 7.159E+0f, 8.693E+3f, 3.303E+2f, 6.003E-3f},
////            {6.407E+0f, 7.234E+0f, 8.907E+3f, 3.678E+2f, 5.889E-3f},
////            {6.734E+0f, 7.603E+0f, 9.120E+3f, 4.052E+2f, 5.765E-3f},
////            // Z= 41-50
////            {6.901E+0f, 7.791E+0f, 9.333E+3f, 4.427E+2f, 5.587E-3f},
////            {6.424E+0f, 7.248E+0f, 9.545E+3f, 4.802E+2f, 5.376E-3f},
////            {6.799E+0f, 7.671E+0f, 9.756E+3f, 5.176E+2f, 5.315E-3f},
////            {6.109E+0f, 6.887E+0f, 9.966E+3f, 5.551E+2f, 5.151E-3f},
////            {5.924E+0f, 6.677E+0f, 1.018E+4f, 5.925E+2f, 4.919E-3f},
////            {5.238E+0f, 5.900E+0f, 1.038E+4f, 6.300E+2f, 4.758E-3f},
////            // {5.623f,    6.354f,    7160.0f,   337.6f,    0.013940f}, // Ag Ziegler77
////            {5.345E+0f, 6.038E+0f, 6.790E+3f, 3.978E+2f, 1.676E-2f}, // Ag ICRU49
////            {5.814E+0f, 6.554E+0f, 1.080E+4f, 3.555E+2f, 4.626E-3f},
////            {6.229E+0f, 7.024E+0f, 1.101E+4f, 3.709E+2f, 4.540E-3f},
////            {6.409E+0f, 7.227E+0f, 1.121E+4f, 3.864E+2f, 4.474E-3f},
////            // Z= 51-60
////            {7.500E+0f, 8.480E+0f, 8.608E+3f, 3.480E+2f, 9.074E-3f},
////            {6.979E+0f, 7.871E+0f, 1.162E+4f, 3.924E+2f, 4.402E-3f},
////            {7.725E+0f, 8.716E+0f, 1.183E+4f, 3.948E+2f, 4.376E-3f},
////            {8.337E+0f, 9.425E+0f, 1.051E+4f, 2.696E+2f, 6.206E-3f},
////            {7.287E+0f, 8.218E+0f, 1.223E+4f, 3.997E+2f, 4.447E-3f},
////            {7.899E+0f, 8.911E+0f, 1.243E+4f, 4.021E+2f, 4.511E-3f},
////            {8.041E+0f, 9.071E+0f, 1.263E+4f, 4.045E+2f, 4.540E-3f},
////            {7.488E+0f, 8.444E+0f, 1.283E+4f, 4.069E+2f, 4.420E-3f},
////            {7.291E+0f, 8.219E+0f, 1.303E+4f, 4.093E+2f, 4.298E-3f},
////            {7.098E+0f, 8.000E+0f, 1.323E+4f, 4.118E+2f, 4.182E-3f},
////            // Z= 61-70
////            {6.909E+0f, 7.786E+0f, 1.343E+4f, 4.142E+2f, 4.058E-3f},
////            {6.728E+0f, 7.580E+0f, 1.362E+4f, 4.166E+2f, 3.976E-3f},
////            {6.551E+0f, 7.380E+0f, 1.382E+4f, 4.190E+2f, 3.877E-3f},
////            {6.739E+0f, 7.592E+0f, 1.402E+4f, 4.214E+2f, 3.863E-3f},
////            {6.212E+0f, 6.996E+0f, 1.421E+4f, 4.239E+2f, 3.725E-3f},
////            {5.517E+0f, 6.210E+0f, 1.440E+4f, 4.263E+2f, 3.632E-3f},
////            {5.220E+0f, 5.874E+0f, 1.460E+4f, 4.287E+2f, 3.498E-3f},
////            {5.071E+0f, 5.706E+0f, 1.479E+4f, 4.330E+2f, 3.405E-3f},
////            {4.926E+0f, 5.542E+0f, 1.498E+4f, 4.335E+2f, 3.342E-3f},
////            {4.788E+0f, 5.386E+0f, 1.517E+4f, 4.359E+2f, 3.292E-3f},
////            // Z= 71-80
////            {4.893E+0f, 5.505E+0f, 1.536E+4f, 4.384E+2f, 3.243E-3f},
////            {5.028E+0f, 5.657E+0f, 1.555E+4f, 4.408E+2f, 3.195E-3f},
////            {4.738E+0f, 5.329E+0f, 1.574E+4f, 4.432E+2f, 3.186E-3f},
////            {4.587E+0f, 5.160E+0f, 1.541E+4f, 4.153E+2f, 3.406E-3f},
////            {5.201E+0f, 5.851E+0f, 1.612E+4f, 4.416E+2f, 3.122E-3f},
////            {5.071E+0f, 5.704E+0f, 1.630E+4f, 4.409E+2f, 3.082E-3f},
////            {4.946E+0f, 5.563E+0f, 1.649E+4f, 4.401E+2f, 2.965E-3f},
////            {4.477E+0f, 5.034E+0f, 1.667E+4f, 4.393E+2f, 2.871E-3f},
////            //  {4.856f,    5.460f,    18320.0f,  438.5f,    0.002542f}, //Ziegler77
////            {4.844E+0f, 5.458E+0f, 7.852E+3f, 9.758E+2f, 2.077E-2f}, //ICRU49
////            {4.307E+0f, 4.843E+0f, 1.704E+4f, 4.878E+2f, 2.882E-3f},
////            // Z= 81-90
////            {4.723E+0f, 5.311E+0f, 1.722E+4f, 5.370E+2f, 2.913E-3f},
////            {5.319E+0f, 5.982E+0f, 1.740E+4f, 5.863E+2f, 2.871E-3f},
////            {5.956E+0f, 6.700E+0f, 1.780E+4f, 6.770E+2f, 2.660E-3f},
////            {6.158E+0f, 6.928E+0f, 1.777E+4f, 5.863E+2f, 2.812E-3f},
////            {6.203E+0f, 6.979E+0f, 1.795E+4f, 5.863E+2f, 2.776E-3f},
////            {6.181E+0f, 6.954E+0f, 1.812E+4f, 5.863E+2f, 2.748E-3f},
////            {6.949E+0f, 7.820E+0f, 1.830E+4f, 5.863E+2f, 2.737E-3f},
////            {7.506E+0f, 8.448E+0f, 1.848E+4f, 5.863E+2f, 2.727E-3f},
////            {7.648E+0f, 8.609E+0f, 1.866E+4f, 5.863E+2f, 2.697E-3f},
////            {7.711E+0f, 8.679E+0f, 1.883E+4f, 5.863E+2f, 2.641E-3f},
////            // Z= 91-92
////            {7.407E+0f, 8.336E+0f, 1.901E+4f, 5.863E+2f, 2.603E-3f},
////            {7.290E+0f, 8.204E+0f, 1.918E+4f, 5.863E+2f, 2.673E-3f}
////        };
//
//
//    }
//
//    fun ComputeCrossSectionPerElectron(kineticEnergy: Double): Double {
//
//        val cross: Double = 0.0
//        val tmax: Double = MaxSecondaryEnergy(kineticEnergy)
//        val maxEnergy: Double = min(tmax, maxKinEnergy)
//        if (cutEnergy < maxEnergy) {
//
//            val energy: Double = kineticEnergy + definition.mass
//            val energy_squared: Double = energy * energy
//            val beta2: Double = kineticEnergy * (kineticEnergy + 2.0 * definition.mass) / energy_squared
//            cross = (maxEnergy - cutEnergy) / (cutEnergy * maxEnergy)
//            -beta2 * ln(maxEnergy / cutEnergy) / tmax
//
//
//            if (0.0 < definition.spin) {
//                cross += 0.5 * (maxEnergy - cutEnergy) / energy_squared; } //FIXME(Хьюстон, где спин?)
//
//            cross *= twopi_mc2_rcl2 * definition.charge * definition.charge / beta2
//        }
//        return cross
//    }
//
//    override fun sampleSecondaries(rnd: RandomGenerator, particle: Particle, element: Element): List<Particle> {
//
//        val xmin: Double = 0.0 //FIXME(Что это и откуда берется?)
//
//        var kineticEnergy: Double = particle.dynamicParticle.kineticEnergy
//        val tmax: Double = MaxSecondaryEnergy(kineticEnergy)
//        val xmax: Double = min(tmax, maxEnergy)
//        if (xmin >= xmax) {
//            return emptyList()
//        }
//
//
//        val energy: Double = kineticEnergy + definition.mass
//        val energy_squared: Double = energy * energy
//        val beta_squared: Double = kineticEnergy * (kineticEnergy + 2.0 * definition.mass) / energy_squared
//        val grej: Double = 1.0
//        var deltaKinEnergy: Double = 0.0
//        var f: Double = 0.0
//
//        do {
//            val rnd1 = rnd.nextDouble()
//            val rnd2 = rnd.nextDouble()
//
//            deltaKinEnergy = xmin * xmax / (xmin * (1.0 - rnd1) + xmax * rnd1)
//
//            f = 1.0 - beta_squared * deltaKinEnergy / tmax
//
//            if (f > grej) {
//                logger.warn { "Majorant $grej < $f for e = $deltaKinEnergy" }
//            }
//        } while (grej * rnd2 >= f)
//
//        val deltaDirection: Vector3D
//
//        if (useAngularGenerator) {
//            //const G4Material* mat =  couple->GetMaterial()
//            //G4int Z = SelectRandomAtomNumber(mat)
//
//            //deltaDirection =
//            //GetAngularDistribution()->SampleDirection(dp, deltaKinEnergy, Z, mat)
//
//        } else {
//
//            var deltaMomentum: Double =
//                sqrt(deltaKinEnergy * (deltaKinEnergy + 2.0 * electron_mass_c2))
//            var cost: Double = deltaKinEnergy * (energy + electron_mass_c2) /
//                    (deltaMomentum * particle.dynamicParticle.totalMomentum)
//            if (cost > 1.0) {
//                cost = 1.0
//            }
//            var sint: Double = sqrt((1.0 - cost) * (1.0 + cost))
//
//            var phi: Double = twopi * rnd.nextDouble()
//
//            var deltaDirection: Vector3D =
//                Vector3D(sint * cos(phi), sint * sin(phi), cost).rotateUz(particle.dynamicParticle.momentumDirection)
//        }
//        val delta = DynamicParticle(
//            Electron,
//            deltaKinEnergy,
//            deltaDirection,
//            0.0
//        )
//        kineticEnergy -= deltaKinEnergy
//        var finalP: Vector3D = particle.dynamicParticle.momentum - delta.momentum
//        //finalP = finalP.unit()// FIXME(в единичный вектор)
//
//        //fParticleChange->SetProposedKineticEnergy(kineticEnergy)// FIXME()
//        //fParticleChange->SetProposedMomentumDirection(finalP)// FIXME()
//
//        //vdp->push_back(delta)// FIXME()
//
//        TODO("Implement based on G4BraggModel::SampleSecondaries")
//    }
//
//    override fun computeCrossSectionPerAtom(rnd: RandomGenerator, kineticEnergy: Double, element: Element): Double {
//        element.Z * ComputeCrossSectionPerElectron(kineticEnergy)
//        TODO("Implement based on G4BraggModel::ComputeCrossSectionPerAtom")
//    }
//
//    override fun ionizationLoss(rnd: RandomGenerator, kineticEnergy: Double) {
//        var massRate: Double = definition.mass / proton_mass_c2
//        var tmax: Double = MaxSecondaryEnergy(kineticEnergy)
//        var tkin: Double = kineticEnergy / massRate
//        var dedx = 0.0
//
//        if (tkin < lowestKinEnergy) {
//            dedx = DEDX(material, lowestKinEnergy) * sqrt(tkin / lowestKinEnergy)
//        } else {
//            dedx = DEDX(material, tkin)
//        }
//
//        if (cutEnergy < tmax) {
//
//            var tau: Double = kineticEnergy / definition.mass
//            var gam: Double = tau + 1.0
//            var bg2: Double = tau * (tau + 2.0)
//            var beta2: Double = bg2 / (gam * gam)
//            var x: Double = cutEnergy / tmax
//
//            dedx += (ln(x) + (1.0 - x) * beta2) * twopi_mc2_rcl2 * (material.ElectronDensity()) / beta2
//        }
//
//        TODO("Implement based on G4BraggModel::ComputeDEDXPerVolume")
//    }
//}

data class ElectronicStoppingPowerCoefficient(
    val x1: Double,
    val x2: Double,
    val x3: Double,
    val x4: Double,
    val x5: Double
)


class ESCPLoader(override val envelope: Envelope): TableDataLoader<ElectronicStoppingPowerCoefficient, Any>() {
    override fun available(item: MetaItem<*>): Boolean {
        TODO("Not yet implemented")
    }

    override fun load(item: MetaItem<*>): ElectronicStoppingPowerCoefficient {
        TODO("Not yet implemented")
    }
}