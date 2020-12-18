package simba.physics.g4.electromagnetic.models

import org.apache.commons.math3.random.RandomGenerator
import simba.physics.*
import simba.physics.g4.Electron
import simba.physics.g4.eV
import simba.physics.g4.electromagnetic.models.data.AtomicShells
import simba.physics.g4.electromagnetic.models.data.SandiaTable

interface AtomicDeExcitation{

}

//
//class PEEffectFluoModel(
//    val minimalEnergy: Double = 1.0 * eV,
//    val angularDistribution : AngularDistrubition = SauterGavrilaAngularDistribution(),
//    val atomicShellsData : Map<Int, AtomicShells>,
//    val sandiaTable: SandiaTable,
//    val atomDeExcitation: AtomicDeExcitation? = null
//) : HEPDiscreteModel {
//
//    override fun sampleSecondaries(
//        rnd: RandomGenerator,
//        particle: HEPParticle,
//        element: Element,
//        material: Material?
//    ): List<HEPParticle> {
//        val energy = particle.kineticEnergy
//        var edep = energy
//        val atomicShells = atomicShellsData[element.Z] // FIXME()
//        var aParticle: HEPParticle? = null
//        if (atomicShells != null){
//            // Photo electron
//            val nShells = atomicShells.size
//            val i = atomicShells.bindingEnergies.indexOfFirst { energy >= it}
//            // Normally one shell is available
//            if (i != -1) {
//                var bindingEnergy = atomicShells.bindingEnergies[i]
//                edep = bindingEnergy
//                var esec = 0.0;
//
//                // sample deexcitation
//                // TODO(loss table)
//                if (atomDeExcitation != null){
//
//                }
////            if(fAtomDeexcitation) {
////                G4int index = couple->GetIndex();
////                if(fAtomDeexcitation->CheckDeexcitationActiveRegion(index)) {
////                    G4int Z = G4lrint(anElement->GetZ());
////                    G4AtomicShellEnumerator as = G4AtomicShellEnumerator(i);
////                    const G4AtomicShell* shell = fAtomDeexcitation->GetAtomicShell(Z, as);
////                    G4double eshell = shell->BindingEnergy();
////                    if(eshell > bindingEnergy && eshell <= energy) {
////                        bindingEnergy = eshell;
////                        edep = eshell;
////                    }
////                    G4int nbefore = fvect->size();
////                    fAtomDeexcitation->GenerateParticles(fvect, shell, Z, index);
////                    G4int nafter = fvect->size();
////                    for (G4int j=nbefore; j<nafter; ++j) {
////                    G4double e = ((*fvect)[j])->GetKineticEnergy();
////                    if(esec + e > edep) {
////                        // correct energy in order to have energy balance
////                        e = edep - esec;
////                        ((*fvect)[j])->SetKineticEnergy(e);
////                        esec += e;
////                        // delete the rest of secondaries (should not happens)
////                        for (G4int jj=nafter-1; jj>j; --jj) {
////                            delete (*fvect)[jj];
////                            fvect->pop_back();
////                        }
////                        break;
////                    }
////                    esec += e;
////                }
////                    edep -= esec;
////                }
////            }
//                // create photo electron
//                //
//                var elecKineEnergy = energy - bindingEnergy;
//                if (elecKineEnergy > minimalEnergy) {
//                    aParticle = HEPParticle(
//                        Electron, elecKineEnergy,
//                        angularDistribution.sampleDirection(rnd, particle),
//                        particle.position
//                    )
//                } else {
//                    edep += elecKineEnergy
//                    elecKineEnergy = 0.0
//                }
//            }
//        }
//
//
//        // kill primary photon
//        particle.kineticEnergy = 0.0;
//        if (edep > 0.0) {
////            fParticleChange->ProposeLocalEnergyDeposit(edep); //TODO (edep)
//        }
//        return aParticle.asList()
//    }
//
//    override fun computeCrossSectionPerAtom(energy: Double, element: Element): Double {
//        val SandiaCof = sandiaTable.sandiaCoefficient(element, energy)
//        val energy2 = energy * energy
//        val energy3 = energy * energy2
//        val energy4 = energy2 * energy2
//        return SandiaCof[0] / energy + SandiaCof[1] / energy2 +
//                SandiaCof[2] / energy3 + SandiaCof[3] / energy4
//    }
//
////    fun ComputeCrossSectionPerMaterial(energy: Double, material: Material, density: Double): Double {
////        // This method may be used only if G4MaterialCutsCouple pointer
////        //   has been set properly
////        val energy_ = max(energy, material.sandiaTable.matSandiaMatrix[0][0])
////        val SandiaCof = material.sandiaTable.getSandiaCofForMaterial(energy, density);
////
////        val energy2 = energy_ * energy_;
////        val energy3 = energy_ * energy2;
////        val energy4 = energy2 * energy2;
////
////        return SandiaCof[0] / energy_ + SandiaCof[1] / energy2 +
////                SandiaCof[2] / energy3 + SandiaCof[3] / energy4
////    }
//
//}