package simba.physics.g4

import simba.physics.ConstsByUnit
import simba.physics.UnitsSpace


/**
 * HEP coherent system of Units from GEANT4
 *  The basic units are :
 *  Millimeter
 *  Nanosecond
 *  Mega electron Volt
 *  positron charge
 *  Degree Kelvin
 *  the amount of substance
 *  luminous intensity
 *  Radian
 *  Steradian
 */
object G4_UNITS : UnitsSpace() {
    override val pi: Double = 3.14159265358979323846
    override val meter: Double = 1.0e3
    override val radian: Double  = 1.0
    override val steradian: Double   = 1.0
    override val second: Double     = 1.0e9
    override val coulomb: Double = 1.0 / e_SI
    override val electronvolt: Double = 1.0e-6
    override val joule: Double = electronvolt / e_SI
    override val volt: Double = electronvolt
    override val kelvin: Double = 1.0
    override val mole: Double = 1.0
    override val candela: Double = 1.0
        

}

val G4_CONSTS = ConstsByUnit(G4_UNITS)