package scientific.simulation.simba.physics.electromagnetic.models

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import org.apache.commons.math3.random.RandomGenerator
import scientific.simulation.simba.physics.particles.Electron
import kotlin.math.*

class DeltaAngle {

    var nprob = 26
    var prob: Array<Double> = Array(nprob, {0.0})

    fun sampleDirection(rnd: RandomGenerator, dynamicParticle: DynamicParticle, kineticEnergy: Double, Z: Int) : Vector3D{

        var nShells: Int = 0
        var idx: Int = 0
        //G4int nShells = G4AtomicShells::GetNumberOfShells(Z); //FIXME(Что это?)
        //G4int idx = fShellIdx; //FIXME(Что это?)

        // if idx is not properly defined sample shell index
        if(idx < 0 || idx >= nShells) {
            if(nShells> nprob) {
                nprob = nShells
                prob = Array(nprob, {0.0})
            }
            var sum: Double = 0.0
            for(idx in 0 until nShells) {
                //sum += G4AtomicShells::GetNumberOfElectrons(Z, idx) //FIXME()
                ///G4AtomicShells::GetBindingEnergy(Z, idx)//FIXME()
                prob[idx] = sum
            }
            sum *= rnd.nextDouble()
            for(idx in 0 until nShells) {
                if(sum <= prob[idx]) {break}
            }
        }

        var bindingEnergy: Double = 0.0 // G4AtomicShells::GetBindingEnergy(Z, idx)//FIXME()
        var cost: Double = 0.0
        /*
        G4cout << "E(keV)= " << kinEnergyFinal/keV
               << " Ebind(keV)= " << bindingEnergy
               << " idx= " << idx << " nShells= " << nShells << G4endl;
        */
        var n: Int = 0
        var isOK: Boolean = false
        val nmax: Int = 100
        do {
            ++n
            // the atomic electron
            var x: Double = -ln(rnd.nextDouble())
            var eKinEnergy: Double = bindingEnergy*x
            var ePotEnergy: Double = bindingEnergy*(1.0 + x)
            var e: Double = kineticEnergy + ePotEnergy + electron_mass_c2
            var p: Double = sqrt((e + electron_mass_c2)*(e - electron_mass_c2))

            var totEnergy: Double = dynamicParticle.totalEnergy
            var totMomentum: Double = dynamicParticle.totalMomentum
            if(dynamicParticle.definition == Electron) {
                totEnergy += ePotEnergy
                totMomentum = sqrt((totEnergy + electron_mass_c2)
                        *(totEnergy - electron_mass_c2))
            }

            var eTotEnergy: Double = eKinEnergy + electron_mass_c2
            var eTotMomentum: Double = sqrt(eKinEnergy*(eTotEnergy + electron_mass_c2))
            var costet: Double = 2*rnd.nextDouble() - 1
            var sintet: Double = sqrt((1 - costet)*(1 + costet))

            var cost: Double = 1.0
            if(n >= nmax) {
                /*
                G4ExceptionDescription ed;
                ed << "### G4DeltaAngle Warning: " << n
                   << " iterations - stop the loop with cost= 1.0 "
                   << " for " << dp->GetDefinition()->GetParticleName() << "\n"
                   << " Ekin(MeV)= " << dp->GetKineticEnergy()/MeV
                   << " Efinal(MeV)= " << kinEnergyFinal/MeV
                   << " Ebinding(MeV)= " << bindingEnergy/MeV;
                G4Exception("G4DeltaAngle::SampleDirection","em0044",
                            JustWarning, ed,"");
                */
                if(0.0 ==  bindingEnergy) {isOK = true}
                bindingEnergy = 0.0
            }

            var x0: Double = p*(totMomentum + eTotMomentum*costet)
            /*
            G4cout << " x0= " << x0 << " p= " << p
                   << "  ptot= " << totMomentum << " pe= " <<  eTotMomentum
                   << " e= " << e << " totMom= " <<  totMomentum
                   << G4endl;
            */
            if(x0 > 0.0) {
                var x1: Double = p*eTotMomentum*sintet
                var x2: Double = totEnergy*(eTotEnergy - e) - e*eTotEnergy
                - totMomentum*eTotMomentum*costet + electron_mass_c2*electron_mass_c2
                var y: Double = -x2/x0
                if(abs(y) <= 1.0) {
                    cost = -(x2 + x1*sqrt(1.0 - y*y))/x0
                    if(abs(cost) <= 1.0) {isOK = true}
                    else {cost = 1.0}
                }

                /*
                G4cout << " Ekin(MeV)= " << dp->GetKineticEnergy()
                       << " e1(keV)= " <<  eKinEnergy/keV
                       << " e2(keV)= " << (e - electron_mass_c2)/keV
                       << " 1-cost= " << 1-cost
                       << " x0= " << x0 << " x1= " << x1 << " x2= " << x2
                       << G4endl;
                */
            }

            // Loop checking, 03-Aug-2015, Vladimir Ivanchenko
        } while(!isOK)

        var sint: Double = sqrt((1 - cost)*(1 + cost))
        var phi: Double  = twopi*rnd.nextDouble()

        val localDirection = Vector3D(sint*cos(phi), sint*sin(phi), cost)

        return localDirection.rotateUz(dynamicParticle.momentumDirection)


    }
}