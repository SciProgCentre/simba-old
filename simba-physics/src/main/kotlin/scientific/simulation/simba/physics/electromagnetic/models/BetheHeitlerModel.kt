//package ru.mipt.npm.mcengine.geant4.physics.electromagnetic.model
//
//import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
//import org.apache.commons.math3.random.RandomGenerator
//import ru.mipt.npm.mcengine.extensions.rotateUz
//import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.coulomb
//import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.ionParamElm
//import ru.mipt.npm.mcengine.material.Element
//import ru.mipt.npm.mcengine.particles.Electron
//import ru.mipt.npm.mcengine.particles.Particle
//import ru.mipt.npm.mcengine.particles.Positron
//import ru.mipt.npm.mcengine.particles.TrackStatus
//import ru.mipt.npm.mcengine.utils.electron_mass_c2
//import ru.mipt.npm.mcengine.utils.MeV
//import ru.mipt.npm.mcengine.utils.microbarn
//import ru.mipt.npm.mcengine.physics.LongStepPhysicalModel
//import ru.mipt.npm.mcengine.utils.twopi
//import java.lang.Math.pow
//import kotlin.math.*
//
//class BetheHeitlerModel(val generator : RandomGenerator) : LongStepPhysicalModel() {
//
//    fun screenFunction1(screenVariable: Double): Double {
//        if (screenVariable > 1.0) {
//            return (42.24 - 8.368 * ln(screenVariable + 0.952))
//        } else {
//            return 42.392 - screenVariable * (7.796 - 1.961 * screenVariable)
//        }
//    }
//
//    fun screenFunction2(screenVariable: Double): Double {
//        if (screenVariable > 1.0) {
//            return (42.24 - 8.368 * ln(screenVariable + 0.952))
//        } else {
//            return (41.405 - screenVariable * (5.828 - 0.8945 * screenVariable))
//        }
//    }
//
//    override fun SampleSecondaries(particle: Particle, element: Element): List<Particle> {
//// The secondaries e+e- energies are sampled using the Bethe - Heitler
//// cross sections with Coulomb correction.
//// A modified version of the random number techniques of Butcher & Messel
//// is used (Nuc Phys 20(1960),15).
////
//// GEANT4 internal units.
////
//// Note 1 : Effects due to the breakdown of the Born approximation at
////          low energy are ignored.
//// Note 2 : The differential cross section implicitly takes account of
////          pair creation in both nuclear and atomic electron fields.
////          However triplet prodution is not generated.
//
////            const G4Material* aMaterial = couple->GetMaterial();
//
//        val GammaEnergy = particle.kineticEnergy;
//        val GammaDirection = particle.momentumDirection;
//
//        var epsil: Double
//        val epsil0 = electron_mass_c2 / GammaEnergy;
//        if (epsil0 > 1.0) {
//            return emptyList(); }
//
//        // do it fast if GammaEnergy < Egsmall
//        // select randomly one element constituing the material
////            const G4Element* anElement =
////            SelectRandomAtom(aMaterial, theGamma, GammaEnergy);
//
//        val Egsmall = 2.0 * MeV;
//        if (GammaEnergy < Egsmall) {
//
//            epsil = epsil0 + (0.5 - epsil0) * generator.nextDouble();
//
//        } else {
//            // now comes the case with GammaEnergy >= 2. MeV
//
//            // Extract Coulomb factor for this Element
//            var FZ = 8.0 * (element.ionParamElm.logZ3);
//            val midEnergy = 50.0 * MeV;
//            if (GammaEnergy > midEnergy) {
//                FZ += 8.0 * (element.coulomb); }
//
//            // limits of the screening variable
//            val screenfac = 136.0 * epsil0 / (element.ionParamElm.Z3);
//            val screenmax = exp((42.24 - FZ) / 8.368) + 0.952;
//            val screenmin = min(4.0 * screenfac, screenmax);
//
//            // limits of the energy sampling
//            val epsil1 = 0.5 - 0.5 * sqrt(1.0 - screenmin / screenmax);
//            val epsilmin = max(epsil0, epsil1);
//            val epsilrange = 0.5 - epsilmin;
//
//            //
//            // sample the energy rate of the created electron (or positron)
//            //
//            //G4double epsil, screenvar, greject ;
//            var screenvar: Double
//            var greject: Double
//
//            val F10 = screenFunction1(screenmin) - FZ;
//            val F20 = screenFunction2(screenmin) - FZ;
//            val NormF1 = max(F10 * epsilrange * epsilrange, 0.0);
//            val NormF2 = max(1.5 * F20, 0.0);
//
//            do {
//                if (NormF1 / (NormF1 + NormF2) > generator.nextDouble()) {
//                    epsil = 0.5 - epsilrange * pow(generator.nextDouble(), (1 / 3).toDouble());
//                    screenvar = screenfac / (epsil * (1 - epsil));
//                    greject = (screenFunction1(screenvar) - FZ) / F10;
//
//                } else {
//                    epsil = epsilmin + epsilrange * generator.nextDouble();
//                    screenvar = screenfac / (epsil * (1 - epsil));
//                    greject = (screenFunction2(screenvar) - FZ) / F20;
//                }
//
//                // Loop checking, 03-Aug-2015, Vladimir Ivanchenko
//            } while (greject < generator.nextDouble());
//
//        }   //  end of epsil sampling
//
//        //
//        // fixe charges randomly
//        //
//
//        var ElectTotEnergy: Double
//        var PositTotEnergy: Double
//        if (generator.nextDouble() > 0.5) {
//
//            ElectTotEnergy = (1.0 - epsil) * GammaEnergy;
//            PositTotEnergy = epsil * GammaEnergy;
//
//        } else {
//
//            PositTotEnergy = (1.0 - epsil) * GammaEnergy;
//            ElectTotEnergy = epsil * GammaEnergy;
//        }
//
//        //
//        // scattered electron (positron) angles. ( Z - axis along the parent photon)
//        //
//        //  universal distribution suggested by L. Urban
//        // (Geant3 manual (1993) Phys211),
//        //  derived from Tsai distribution (Rev Mod Phys 49,421(1977))
//
//        val a1 = 1.6;
//        val a2 = a1 / 3.0;
//        val uu = -ln(generator.nextDouble() * generator.nextDouble());
//        val u = if (0.25 > generator.nextDouble()) uu * a1 else uu * a2;
//
//        val thetaEle = u * electron_mass_c2 / ElectTotEnergy;
//        val sinte = sin(thetaEle);
//        val coste = cos(thetaEle);
//
//        val thetaPos = u * electron_mass_c2 / PositTotEnergy;
//        val sintp = sin(thetaPos);
//        val costp = cos(thetaPos);
//
//        val phi = twopi * generator.nextDouble();
//        val sinp = sin(phi);
//        val cosp = cos(phi);
//
//        //
//        // kinematic of the created pair
//        //
//        // the electron and positron are assumed to have a symetric
//        // angular distribution with respect to the Z axis along the parent photon.
//
//        val ElectKineEnergy = max(0.0, ElectTotEnergy - electron_mass_c2);
//
//        val ElectDirection = Vector3D(sinte * cosp, sinte * sinp, coste);
//        ElectDirection.rotateUz(GammaDirection);
//
//        // create G4DynamicParticle object for the particle1
//        val aParticle1 = Particle(Electron, ElectKineEnergy, ElectDirection);
//
//        // the e+ is always created (even with Ekine=0) for further annihilation.
//
//        val PositKineEnergy = max(0.0, PositTotEnergy - electron_mass_c2);
//        val PositDirection = Vector3D(-sintp * cosp, -sintp * sinp, costp);
//        PositDirection.rotateUz(GammaDirection);
//
//        // create G4DynamicParticle object for the particle2
//        val aParticle2 = Particle(Positron, PositKineEnergy, PositDirection);
//
//        // kill incident photon
//        particle.kineticEnergy = 0.0;
//        particle.trackStatus = TrackStatus.stop
//        return listOf(aParticle1, aParticle2)
//    }
//
//    override fun ComputeCrossSectionPerAtom(energy: Double, element: Element): Double {
//        var xSection = 0.0;
//        val Z = element.Z
//        if (Z < 0.9 || energy <= 2.0 * electron_mass_c2) {
//            return xSection; }
//
//
//        val energyLimit = 1.5 * MeV;
//        val a0 = 8.7842e+2 * microbarn
//        val a1 = -1.9625e+3 * microbarn
//        val a2 = 1.2949e+3 * microbarn
//        val a3 = -2.0028e+2 * microbarn
//        val a4 = 1.2575e+1 * microbarn
//        val a5 = -2.8333e-1 * microbarn
//
//        val b0 = -1.0342e+1 * microbarn
//        val b1 = 1.7692e+1 * microbarn
//        val b2 = -8.2381 * microbarn
//        val b3 = 1.3063 * microbarn
//        val b4 = -9.0815e-2 * microbarn
//        val b5 = 2.3586e-3 * microbarn
//
//        val c0 = -4.5263e+2 * microbarn
//        val c1 = 1.1161e+3 * microbarn
//        val c2 = -8.6749e+2 * microbarn
//        val c3 = 2.1773e+2 * microbarn
//        val c4 = -2.0467e+1 * microbarn
//        val c5 = 6.5372e-1 * microbarn
//
//        var energy_temp = energy
//        val energySave = energy;
//        if (energy < energyLimit) {
//            energy_temp = energyLimit; }
//
//        var X = ln(energy_temp / electron_mass_c2)
//        val X2 = X * X
//        val X3 = X2 * X
//        val X4 = X3 * X
//        val X5 = X4 * X
//
//        val F1 = a0 + a1 * X + a2 * X2 + a3 * X3 + a4 * X4 + a5 * X5
//        val F2 = b0 + b1 * X + b2 * X2 + b3 * X3 + b4 * X4 + b5 * X5
//        val F3 = c0 + c1 * X + c2 * X2 + c3 * X3 + c4 * X4 + c5 * X5;
//
//        xSection = (Z + 1.0) * (F1 * Z + F2 * Z * Z + F3);
//
//        if (energySave < energyLimit) {
//            X = (energySave - 2.0 * electron_mass_c2) / (energyLimit - 2.0 * electron_mass_c2);
//            xSection *= X * X;
//        }
//
//        xSection = max(xSection, 0.0);
//        return xSection;
//    }
//}