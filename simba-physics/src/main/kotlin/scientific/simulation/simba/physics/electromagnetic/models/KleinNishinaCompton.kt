package scientific.simulation.simba.physics.electromagnetic.models

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import org.apache.commons.math3.random.RandomGenerator
import kotlin.math.*


class KleinNishinaCompton(val lowEnergyLimit: Double = 0.0, val generator : RandomGenerator) : HEPPhysicalModel {
    val lowestSecondaryEnergy: Double = 100.0*eV

    init {
    }

    override fun ComputeCrossSectionPerAtom(energy :Double, element : Element): Double {
        var xSection = 0.0
        if (energy <= lowEnergyLimit) { return xSection }

        val Z = element.Z
        val a = 20.0
        val b = 230.0
        val c = 440.0

        val d1= 2.7965e-1* barn
        val d2=-1.8300e-1* barn
        val d3= 6.7527   * barn
        val d4=-1.9798e+1* barn
        val e1= 1.9756e-5* barn
        val e2=-1.0205e-2* barn
        val e3=-7.3913e-2* barn
        val e4= 2.7079e-2* barn
        val f1=-3.9178e-7* barn
        val f2= 6.8241e-5* barn
        val f3= 6.0480e-5* barn
        val f4= 3.0274e-4* barn

        val p1Z = Z*(d1 + e1*Z + f1*Z*Z)
        val p2Z = Z*(d2 + e2*Z + f2*Z*Z)
        val p3Z = Z*(d3 + e3*Z + f3*Z*Z)
        val p4Z = Z*(d4 + e4*Z + f4*Z*Z)

        val T0  = if (Z < 1.5)  15.0* keV else 40.0* keV

        val X   = max(energy, T0) / electron_mass_c2
        xSection = p1Z* ln(1.0+2.0*X) /X + (p2Z + p3Z*X + p4Z*X*X)/(1.0 + a*X + b*X*X + c*X*X*X)

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


    override fun SampleSecondaries(particle: Particle, element: Element): List<Particle> {
        val gamEnergy0 = particle.kineticEnergy

        // do nothing below the threshold
        if(gamEnergy0 <= lowEnergyLimit) { return emptyList() }

        val E0_m = gamEnergy0 / electron_mass_c2;

        val gamDirection0 = particle.momentumDirection;

        //
        // sample the energy rate of the scattered gamma
        //

        var epsilon : Double
        var epsilonsq: Double
        var onecost : Double
        var sint2 : Double
        var greject : Double

        val eps0       = 1.0/(1.0 + 2.0*E0_m)
        val epsilon0sq = eps0*eps0
        val alpha1     = - ln(eps0)
        val alpha2     = alpha1 + 0.5*(1.0 - epsilon0sq)
        val nlooplim = 1000
        var nloop = 0
        do {
            ++nloop;
            // false interaction if too many iterations
            if(nloop > nlooplim) { return emptyList() }

            if ( alpha1 > alpha2*generator.nextDouble() ) {
                epsilon   = exp(-alpha1*generator.nextDouble())  // eps0**r
                epsilonsq = epsilon*epsilon;

            } else {
                epsilonsq = epsilon0sq + (1.0- epsilon0sq)*generator.nextDouble()
                epsilon   = sqrt(epsilonsq)
            };

            onecost = (1.0- epsilon)/(epsilon*E0_m)
            sint2   = onecost*(2.0-onecost)
            greject = 1.0 - epsilon*sint2/(1.0+ epsilonsq)

            // Loop checking, 03-Aug-2015, Vladimir Ivanchenko
        } while (greject < generator.nextDouble())

        //
        // scattered gamma angles. ( Z - axis along the parent gamma)
        //

        if(sint2 < 0.0) { sint2 = 0.0; }
        val cosTeta = 1.0 - onecost
        val sinTeta = sqrt (sint2)
        val Phi     = twopi * generator.nextDouble()

        //
        // update G4VParticleChange for the scattered gamma
        //

        var gamDirection1 = Vector3D(sinTeta*cos(Phi), sinTeta*sin(Phi), cosTeta)
        gamDirection1 = gamDirection1.rotateUz(gamDirection0)
        val gamEnergy1 = epsilon*gamEnergy0
        var edep = 0.0;
        if(gamEnergy1 > lowestSecondaryEnergy) {
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
        var dp : Particle? = null
        if(eKinEnergy > lowestSecondaryEnergy) {
            var eDirection = gamEnergy0*gamDirection0 - gamEnergy1*gamDirection1;
            eDirection = eDirection.normalize();

            // create G4DynamicParticle object for the electron.
            dp = Particle(Electron, eKinEnergy, eDirection);

        } else {
            edep += eKinEnergy;
        }
        // energy balance
        if(edep > 0.0) {
            //TODO(deposit)
//            fParticleChange->ProposeLocalEnergyDeposit(edep);
        }


        if (dp == null) {
            return emptyList()
        }
        else{
            return listOf(dp)
        }
    }
}




