package simba.physics.g4.electromagnetic.models

import kscience.kmath.geometry.Vector3D
import kscience.kmath.prob.RandomGenerator
import simba.math.*
import simba.physics.*
import simba.physics.g4.*
import kotlin.math.*


class KleinNishinaCompton(
    parametrisation: Parametrisation? = null,
    val lowEnergyLimit: Double = 0.0
) : HEPDiscreteModel {
    val parametrisation = parametrisation ?: DEFOULT_PARAMETRASATION

    companion object {
        val DEFOULT_PARAMETRASATION = Parametrisation(
            Parametrisation.Coefficient(20.0, 230.0, 440.0),
            Parametrisation.Polynom(2.7965e-1 * barn, 1.9756e-5 * barn, -3.9178e-7 * barn),
            Parametrisation.Polynom(-1.8300e-1 * barn, -1.0205e-2 * barn, 6.8241e-5 * barn),
            Parametrisation.Polynom(6.7527 * barn, -7.3913e-2 * barn, 6.0480e-5 * barn),
            Parametrisation.Polynom(-1.9798e+1 * barn, 2.7079e-2 * barn, 3.0274e-4 * barn),
        )
        val LOWEST_SECONDARY_ENERGY: Double = 100.0 * eV
    }

    data class Parametrisation(
        val coefficient: Coefficient,
        val p1: Polynom, val p2: Polynom, val p3: Polynom, val p4: Polynom
    ) {
        data class Coefficient(val a: Double, val b: Double, val c: Double) {
            operator fun invoke(X: Double) = 1 + a * X + b * X * X + c * X * X * X
        }

        data class Polynom(val d: Double, val e: Double, val f: Double) {
            operator fun invoke(Z: Int) = Z * (d + e * Z + f * Z * Z)
        }

        operator fun invoke(Z: Int, X: Double) = p1(Z) * ln(1.0 + 2.0 * X) / X + (p2(Z) + p3(Z) * X + p4(Z) * X * X) / coefficient(X)

    }


    override fun computeCrossSectionPerAtom(energy: Double, element: Element): Double {
        var xSection = 0.0
        if (energy <= lowEnergyLimit) {
            return xSection
        }
        val Z = element.Z
        val T0 = if (Z < 1.5) 15.0 * keV else 40.0 * keV
        val X = max(energy, T0) / electron_mass_c2
        xSection = parametrisation(Z, X)
        //  modification for low energy. (special case for Hydrogen)
//        if (energy < T0) {
//            static const G4double dT0 = KeV;
//            X = (T0+dT0) / Electron_mass_c2 ;
//            G4double sigma = p1Z*G4Log(1.+2*X)/X
//            + (p2Z + p3Z*X + p4Z*X*X)/(1. + a*X + b*X*X + c*X*X*X);
//            G4double   c1 = -T0*(sigma-xSection)/(xSection*dT0);
//            G4double   c2 = 0.150;
//            if (Z > 1.5) { c2 = 0.375-0.0556*G4Log(Z); }
//            G4double    y = G4Log(energy/T0);
//            xSection *= G4Exp(-y*(c1+c2*y));
//        }
        // G4cout<<"e= "<< energy<<" Z= "<<Z<<" cross= " << xSection << G4endl;
        return xSection;

    }


    override fun sampleSecondaries(rnd: RandomGenerator, particle: HEPParticle, element: Element, material: Material?): List<HEPParticle> {
        val gamEnergy0 = particle.kineticEnergy

        // do nothing below the threshold
        if (gamEnergy0 <= lowEnergyLimit) {
            return emptyList()
        }

        val E0_m = gamEnergy0 / electron_mass_c2;

        val gamDirection0 = particle.momentumDirection;

        //
        // sample the energy rate of the scattered gamma
        //

        var epsilon: Double
        var epsilonsq: Double
        var onecost: Double
        var sint2: Double
        var greject: Double

        val eps0 = 1.0 / (1.0 + 2.0 * E0_m)
        val epsilon0sq = eps0 * eps0
        val alpha1 = -ln(eps0)
        val alpha2 = alpha1 + 0.5 * (1.0 - epsilon0sq)
        val nlooplim = 1000
        var nloop = 0
        do {
            ++nloop;
            // false interaction if too many iterations
            if (nloop > nlooplim) {
                return emptyList()
            }

            if (alpha1 > alpha2 * rnd.nextDouble()) {
                epsilon = exp(-alpha1 * rnd.nextDouble())  // eps0**r
                epsilonsq = epsilon * epsilon;

            } else {
                epsilonsq = epsilon0sq + (1.0 - epsilon0sq) * rnd.nextDouble()
                epsilon = sqrt(epsilonsq)
            };

            onecost = (1.0 - epsilon) / (epsilon * E0_m)
            sint2 = onecost * (2.0 - onecost)
            greject = 1.0 - epsilon * sint2 / (1.0 + epsilonsq)

        } while (greject < rnd.nextDouble())

        //
        // scattered gamma angles. ( Z - axis along the parent gamma)
        //

        if (sint2 < 0.0) {
            sint2 = 0.0; }
        val cosTeta = 1.0 - onecost
        val sinTeta = sqrt(sint2)
        val Phi = twopi * rnd.nextDouble()

        //
        // update G4VParticleChange for the scattered gamma
        //

        var gamDirection1 = Vector3D(sinTeta * cos(Phi), sinTeta * sin(Phi), cosTeta)
        gamDirection1 = gamDirection1.rotateUz(gamDirection0)
        val gamEnergy1 = epsilon * gamEnergy0
        var edep = 0.0;
        if (gamEnergy1 > LOWEST_SECONDARY_ENERGY) {
            particle.momentumDirection = gamDirection1
            particle.kineticEnergy = gamEnergy1
        } else {
            particle.kineticEnergy = 0.0
            edep = gamEnergy1
        }

        //
        // kinematic of the scattered electron
        //

        val eKinEnergy = gamEnergy0 - gamEnergy1;
        var dp: HEPParticle? = null
        if (eKinEnergy > LOWEST_SECONDARY_ENERGY) {
            var eDirection = gamEnergy0 * gamDirection0 - gamEnergy1 * gamDirection1;
            eDirection = eDirection.normalize

            // create G4DynamicParticle object for the electron.
            dp = HEPParticle(Electron, eKinEnergy, eDirection, particle.position);

        } else {
            edep += eKinEnergy;
        }
        // energy balance
        if (edep > 0.0) {
            //TODO(deposit)
//            fParticleChange->ProposeLocalEnergyDeposit(edep);
        }
        return dp.asList()
    }
}




