package simba.physics.g4

import simba.physics.AbstractElectron
import simba.physics.AbstractPositron

object Electron : AbstractElectron() {
    override val name: String = "electron"

    override val mass: Double = electron_mass_c2

    override val width: Double = 0.0 * MeV

    override val charge: Double = -1.0 * eplus

    override val iParity: Int = 1

    override val iConjugation: Int = 0

    override val iIsospin: Int = 0

    override val iIsospinZ: Int = 0

    override val gParity: Int = 0

//    override val pType: String

    override val lepton: Int = 1

    override val baryon: Int = 0

    override val stable: Boolean = true

    override val lifetime: Double = -1.0

//    override val decaytable: Any?

    override val shortlived: Boolean = false

//    override val subType: String

    override val magneticMoment: Double = -muB * 1.00115965218076
}

////val Positron = ParticlesDefinition(
////    "e+", electron_mass_c2, 0.0 * MeV, +1.0 * eplus,
////    1, 0, 0,
////    0, 0, 0,
////    "lepton", -1, 0, -11,
////    true, -1.0, null,
////    false, "e", magneticMoment = muB * 1.00115965218076
////)

object Positron : AbstractPositron(){
    override val name: String = "positron"
    override val mass: Double = electron_mass_c2
    override val width: Double = 0.0 * MeV
    override val charge: Double = +1.0 * eplus
    override val iParity: Int =1
    override val iConjugation: Int =0
    override val iIsospin: Int =0
    override val iIsospinZ: Int =0
    override val gParity: Int = 0
    override val lepton: Int = -1
    override val baryon: Int = 0
    override val stable: Boolean = true
    override val lifetime: Double = -1.0
    override val shortlived: Boolean = false
    override val magneticMoment: Double = muB * 1.00115965218076

}