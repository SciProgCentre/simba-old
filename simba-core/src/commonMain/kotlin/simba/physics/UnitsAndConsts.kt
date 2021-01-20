package simba.physics

import kotlin.math.PI


abstract class UnitsSpace {

    abstract val pi: Double
    val twopi = 2 * pi
    val halfpi = pi / 2
    val pi2 = pi * pi

    //
    // Length [L]
    //
    abstract val meter: Double

    val millimeter = 1e-3 * meter
    val millimeter2 = millimeter * millimeter
    val millimeter3 = millimeter * millimeter * millimeter

    val centimeter = 0.01 * meter
    val centimeter2 = centimeter * centimeter
    val centimeter3 = centimeter * centimeter * centimeter


    val meter2 = meter * meter
    val meter3 = meter * meter * meter

    val kilometer = 1000.0 * meter
    val kilometer2 = kilometer * kilometer
    val kilometer3 = kilometer * kilometer * kilometer

    val parsec = 3.0856775807e+16 * meter

    val micrometer = 1.0e-6 * meter
    val nanometer = 1.0e-9 * meter
    val angstrom = 1.0e-10 * meter
    val fermi = 1.0e-15 * meter

    val barn = 1.0e-28 * meter2
    val millibarn = 1.0e-3 * barn
    val microbarn = 1.0e-6 * barn
    val nanobarn = 1.0e-9 * barn
    val picobarn = 1.0e-12 * barn

    // symbols
    val nm = nanometer
    val um = micrometer

    val mm = millimeter
    val mm2 = millimeter2
    val mm3 = millimeter3

    val cm = centimeter
    val cm2 = centimeter2
    val cm3 = centimeter3

    val liter = 1.0e3 * cm3
    val L = liter
    val dL = 1.0e-1 * liter
    val cL = 1.0e-2 * liter
    val mL = 1.0e-3 * liter

    val m = meter
    val m2 = meter2
    val m3 = meter3

    val km = kilometer
    val km2 = kilometer2
    val km3 = kilometer3

    val pc = parsec

    //
    // Angle
    //
    abstract val radian: Double
    val milliradian = 1.0e-3 * radian
    val degree = (pi / 180.0) * radian
    abstract val steradian: Double

    // symbols
    val rad = radian
    val mrad = milliradian
    val sr = steradian
    val deg = degree

    //
    // Time [T]
    //
    abstract val second: Double
    val millisecond = 1.0e-3 * second
    val microsecond = 1.0e-6 * second
    val nanosecond = 1.0e-9 * second
    val picosecond = 1.0e-12 * second

    val hertz = 1.0 / second
    val kilohertz = 1.0e+3 * hertz
    val megahertz = 1.0e+6 * hertz

    // symbols
    val ns = nanosecond
    val s = second
    val ms = millisecond
    val us = microsecond
    val ps = picosecond

    //
    // Electric charge [Q]
    //
    abstract val coulomb: Double

    //
    // Energy [E]
    //
    internal val e_SI = 1.602176487e-19// positron charge in Coulomb
    abstract val electronvolt: Double
    val kiloelectronvolt = 1.0e+3 * electronvolt
    val megaelectronvolt = 1.0e+6 * electronvolt
    val gigaelectronvolt = 1.0e+3 * megaelectronvolt
    val teraelectronvolt = 1.0e+6 * megaelectronvolt
    val petaelectronvolt = 1.0e+9 * megaelectronvolt

    abstract val joule: Double

    // symbols
    val MeV = megaelectronvolt
    val eV = electronvolt
    val keV = kiloelectronvolt
    val GeV = gigaelectronvolt
    val TeV = teraelectronvolt
    val PeV = petaelectronvolt

    //
    // Mass [E][T^2][L^-2]
    //
    val kilogram = joule * second * second / (meter * meter)
    val gram = 1.0e-3 * kilogram
    val milligram = 1.0e-3 * gram

    // symbols
    val kg = kilogram
    val g = gram
    val mg = milligram

    //
    // Power [E][T^-1]
    //
    val watt = joule / second

    //
    // Force [E][L^-1]
    //
    val newton = joule / meter

    //
    // Pressure [E][L^-3]
    //
    val pascal = newton / m2
    val bar = 100000 * pascal
    val atmosphere = 101325 * pascal

    //
    // Electric current [Q][T^-1]
    //
    val ampere = coulomb / second
    val milliampere = 1.0e-3 * ampere
    val microampere = 1.0e-6 * ampere
    val nanoampere = 1.0e-9 * ampere

    //
    // Electric potential [E][Q^-1]
    //
    abstract val volt: Double
    val kilovolt = 1.0e+3 * volt
    val megavolt = 1.0e+6 * volt

    //
    // Electric resistance [E][T][Q^-2]
    //
    val ohm = volt / ampere// Ohm = 1.60217e-16*(MeV/Eplus)/(Eplus/Ns)

    //
    // Electric capacitance [Q^2][E^-1]
    //
    val farad = coulomb / volt// Farad = 6.24150e+24 * Eplus/Megavolt
    val millifarad = 1.0e-3 * farad
    val microfarad = 1.0e-6 * farad
    val nanofarad = 1.0e-9 * farad
    val picofarad = 1.0e-12 * farad

    //
    // Magnetic Flux [T][E][Q^-1]
    //
    val weber = volt * second// Weber = 1000*Megavolt*Ns

    //
    // Magnetic Field [T][E][Q^-1][L^-2]
    //
    val tesla = volt * second / meter2// Tesla =0.001*Megavolt*Ns/Mm2

    val gauss = 1.0e-4 * tesla
    val kilogauss = 1.0e-1 * tesla

    //
    // Inductance [T^2][E][Q^-2]
    //
    val henry = weber / ampere// Henry = 1.60217e-7*MeV*(Ns/Eplus)**2

    //
    // Temperature
    //
    abstract val kelvin: Double

    //
    // Amount of substance
    //
    abstract val mole: Double

    //
    // Activity [T^-1]
    //
    val becquerel = 1.0 / second
    val curie = 3.7e+10 * becquerel
    val kilobecquerel = 1.0e+3 * becquerel
    val megabecquerel = 1.0e+6 * becquerel
    val gigabecquerel = 1.0e+9 * becquerel
    val millicurie = 1.0e-3 * curie
    val microcurie = 1.0e-6 * curie
    val Bq = becquerel
    val kBq = kilobecquerel
    val MBq = megabecquerel
    val GBq = gigabecquerel
    val Ci = curie
    val mCi = millicurie
    val uCi = microcurie

    //
    // Absorbed dose [L^2][T^-2]
    //
    val gray = joule / kilogram
    val kilogray = 1.0e+3 * gray
    val milligray = 1.0e-3 * gray
    val microgray = 1.0e-6 * gray

    //
    // Luminous intensity [I]
    //
    abstract val candela: Double

    //
    // Luminous flux [I]
    //
    val lumen = candela * steradian

    //
    // Illuminance [I][L^-2]
    //
    val lux = lumen / meter2

    //
    // Miscellaneous
    //
    val perCent = 0.01
    val perThousand = 0.001
    val perMillion = 0.000001
}


abstract class ConstsSpace {
    abstract val pi: Double
    val twopi = 2 * pi

    abstract val avogadro: Double

    abstract val light_speed: Double
    val light_speed_squared = light_speed * light_speed
    val c_light = light_speed
    val c_squared = light_speed_squared

    abstract val h_Planck: Double
    val hbar_Planck = h_Planck / twopi
    val hbarc = hbar_Planck * c_light
    val hbarc_squared = hbarc * hbarc

    abstract val electron_mass_c2 : Double
    abstract val proton_mass_c2 : Double
    abstract val neutron_mass_c2 : Double
    abstract val amu_c2 : Double

    abstract val electron_charge : Double
    val e_squared = electron_charge * electron_charge

    abstract val mu0: Double
    val epsilon0 = 1.0 / (c_squared * mu0)


    val elm_coupling = e_squared / (4.0 * pi * epsilon0)
    val fine_structure_ = elm_coupling / hbarc
    val classic_electr_radius = elm_coupling / electron_mass_c2
    val electron_Compton_length = hbarc / electron_mass_c2
    val Bohr_radius = electron_Compton_length / fine_structure_

    val alpha_rcl2 = fine_structure_ * classic_electr_radius * classic_electr_radius

    val twopi_mc2_rcl2 = twopi * electron_mass_c2 * classic_electr_radius * classic_electr_radius

    abstract val k_Boltzmann : Double

    abstract val STP_Temperature : Double
    abstract val STP_Pressure : Double
    abstract val NTP_Temperature : Double
//    val kGasThreshold = 10.0 * mg / cm3
//    val universe_mean_density = 1.0e-25 * g / cm3

    //  positive Bohr Magnetron
    val muB = 0.5 * electron_charge * hbar_Planck / (electron_mass_c2 / c_squared)


}

class ConstsByUnit(units: UnitsSpace) : ConstsSpace() {
    override val pi: Double = units.pi
    override val avogadro = 6.02214179e+23 / units.mole

    override val light_speed: Double = 2.99792458e+8 * units.m / units.s
    override val h_Planck = 6.62606896e-34 * units.joule * units.s

    override val electron_charge = -units.e_SI* units.coulomb // see SystemOfUnits.h


    override val electron_mass_c2 = 0.510998910 * units.MeV
    override val proton_mass_c2 = 938.272013 * units.MeV
    override val neutron_mass_c2 = 939.56536 * units.MeV
    override val amu_c2 = 931.494028 * units.MeV
    val amu = amu_c2 / c_squared

    override val mu0 = 4 * pi * 1.0e-7 * units.henry / units.m

    override val k_Boltzmann = 8.617343e-11 * units.MeV / units.kelvin

    override val STP_Temperature = 273.15 * units.kelvin
    override val STP_Pressure = 1.0 * units.atmosphere
    override val NTP_Temperature = 293.15 * units.kelvin
//    val kGasThreshold = 10.0 * mg / cm3
//    val universe_mean_density = 1.0e-25 * g / cm3

}

object SI_UNITS : UnitsSpace() {
    override val pi: Double = PI

    override val meter: Double = 1.0

    override val radian: Double = 1.0

    override val steradian: Double = 1.0

    override val second: Double = 1.0

    override val coulomb: Double = 1.0

    override val joule: Double = 1.0

    override val volt: Double = 1.0

    override val electronvolt: Double = e_SI * coulomb * volt

    override val kelvin: Double = 1.0

    override val mole: Double = 1.0

    override val candela: Double = 1.0

}

val SI_CONSTS = ConstsByUnit(SI_UNITS)