package ru.mipt.npm.mcengine.GEANT4.model.electromagnetic

import org.apache.commons.math3.random.RandomGenerator
import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.data.SandiaTableData
import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.fAtomicShells
import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.fNbOfAtomicShells
import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.model.SauterGavrilaAngularDistribution
import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.sandiaTable
import ru.mipt.npm.mcengine.material.Element
import ru.mipt.npm.mcengine.particles.Particle
import ru.mipt.npm.mcengine.particles.TrackStatus
import ru.mipt.npm.mcengine.utils.eV
import ru.mipt.npm.mcengine.material.Material
import ru.mipt.npm.mcengine.particles.Electron
import ru.mipt.npm.mcengine.physics.LongStepPhysicalModel
import kotlin.math.max

class PEEffectFluoModel(generator: RandomGenerator, atomDeexcitation: Boolean = true, val minimalEnergy: Double = 1.0 * eV) : LongStepPhysicalModel() {
    val angularDistribution = SauterGavrilaAngularDistribution(generator)
    override fun SampleSecondaries(particle: Particle, element: Element): List<Particle> {
        var energy = particle.kineticEnergy

        //
        // Photo electron
        //

        // Select atomic shell
        var nShells = element.fNbOfAtomicShells
        var i =0

        do {
            if (energy >= element.fAtomicShells[i]) { break; }
            i++
        } while (i < nShells)

        var edep = energy
        var aParticle : Particle? = null
        // Normally one shell is available
        if (i < nShells) {

            var bindingEnergy = element.fAtomicShells[i]
            edep = bindingEnergy;
            var esec = 0.0;

            // sample deexcitation
            // TODO(loss table)
//            if(fAtomDeexcitation) {
//                G4int index = couple->GetIndex();
//                if(fAtomDeexcitation->CheckDeexcitationActiveRegion(index)) {
//                    G4int Z = G4lrint(anElement->GetZ());
//                    G4AtomicShellEnumerator as = G4AtomicShellEnumerator(i);
//                    const G4AtomicShell* shell = fAtomDeexcitation->GetAtomicShell(Z, as);
//                    G4double eshell = shell->BindingEnergy();
//                    if(eshell > bindingEnergy && eshell <= energy) {
//                        bindingEnergy = eshell;
//                        edep = eshell;
//                    }
//                    G4int nbefore = fvect->size();
//                    fAtomDeexcitation->GenerateParticles(fvect, shell, Z, index);
//                    G4int nafter = fvect->size();
//                    for (G4int j=nbefore; j<nafter; ++j) {
//                    G4double e = ((*fvect)[j])->GetKineticEnergy();
//                    if(esec + e > edep) {
//                        // correct energy in order to have energy balance
//                        e = edep - esec;
//                        ((*fvect)[j])->SetKineticEnergy(e);
//                        esec += e;
//                        // delete the rest of secondaries (should not happens)
//                        for (G4int jj=nafter-1; jj>j; --jj) {
//                            delete (*fvect)[jj];
//                            fvect->pop_back();
//                        }
//                        break;
//                    }
//                    esec += e;
//                }
//                    edep -= esec;
//                }
//            }
            // create photo electron
            //
            var elecKineEnergy = energy - bindingEnergy;
            if (elecKineEnergy > minimalEnergy) {
                aParticle = Particle(Electron, elecKineEnergy,
                        angularDistribution.SampleDirection(particle))
            } else {
                edep += elecKineEnergy
                elecKineEnergy = 0.0
            }
            //TODO (а надо ли)
//            if(abs(energy - elecKineEnergy - esec - edep) > CLHEP::eV) {
//                G4cout << "### G4PEffectFluoModel dE(eV)= "
//                << (energy - elecKineEnergy - esec - edep)/eV
//                << " shell= " << i
//                << "  E(keV)= " << energy/keV
//                << "  Ebind(keV)= " << bindingEnergy/keV
//                << "  Ee(keV)= " << elecKineEnergy/keV
//                << "  Esec(keV)= " << esec/keV
//                << "  Edep(keV)= " << edep/keV
//                << G4endl;
//            }
        }

        // kill primary photon
        particle.kineticEnergy = 0.0;
        particle.trackStatus = TrackStatus.stop
        if(edep > 0.0) {
//            fParticleChange->ProposeLocalEnergyDeposit(edep); //TODO (edep)
        }
        if (aParticle == null) {
            return emptyList()
        }
        else{
            return listOf(aParticle)
        }

    }

    override fun ComputeCrossSectionPerAtom(energy: Double, element: Element): Double {
        val SandiaCof = SandiaTableData.getSandiaCofPerAtom(element.Z, energy)
        val energy2 = energy*energy
        val energy3 = energy*energy2
        val energy4 = energy2*energy2
        return SandiaCof[0]/energy  + SandiaCof[1]/energy2 +
                SandiaCof[2]/energy3 + SandiaCof[3]/energy4
    }

    fun ComputeCrossSectionPerMaterial(energy: Double, material: Material, density : Double): Double{
        // This method may be used only if G4MaterialCutsCouple pointer
        //   has been set properly
        val energy_ = max(energy, material.sandiaTable.matSandiaMatrix[0][0])
        val SandiaCof = material.sandiaTable.getSandiaCofForMaterial(energy, density);

        val energy2 = energy_*energy_;
        val energy3 = energy_*energy2;
        val energy4 = energy2*energy2;

        return SandiaCof[0]/energy_  + SandiaCof[1]/energy2 +
                SandiaCof[2]/energy3 + SandiaCof[3]/energy4
    }

}