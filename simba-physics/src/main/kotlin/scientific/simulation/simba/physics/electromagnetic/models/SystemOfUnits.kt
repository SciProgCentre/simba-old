package scientific.simulation.simba.physics.electromagnetic.models

// HEP coherent system of Units
//
// This file has been provided to CLHEP by Geant4 (simulation toolkit for HEP).
//
// The basic units are :
// Millimeter              (Millimeter)
// Nanosecond              (Nanosecond)
// Mega electron Volt      (MeV)
// positron charge         (Eplus)
// Degree Kelvin           (Kelvin)
// the amount of substance (Mole)
// luminous intensity      (Candela)
// Radian                  (Radian)
// Steradian               (Steradian)
//
// Below is a non exhaustive list of derived and pratical units
// (i.0e. mostly the SI units).
// You can add your own units.
//
// The SI numerical value of the positron charge is defined here,
// as it is needed for conversion factor : positron charge = E_SI (Coulomb)
//
// The others physical constants are defined in the header file :
// PhysicalConstants.h
//
// Authors: M.Maire, S.Giani
//

const val pi = 3.14159265358979323846
const val twopi = 2 * pi
const val halfpi = pi / 2
const val pi2 = pi * pi

//
// Length [L]
//
const val millimeter = 1.0
const val millimeter2 = millimeter * millimeter
const val millimeter3 = millimeter * millimeter * millimeter

const val centimeter = 10.0* millimeter
const val centimeter2 = centimeter * centimeter
const val centimeter3 = centimeter * centimeter * centimeter

const val meter = 1000.0* millimeter
const val meter2 = meter * meter
const val meter3 = meter * meter * meter

const val kilometer = 1000.0* meter
const val kilometer2 = kilometer * kilometer
const val kilometer3 = kilometer * kilometer * kilometer

const val parsec = 3.0856775807e+16 * meter

const val micrometer = 1.0e-6 * meter
const val nanometer = 1.0e-9 * meter
const val angstrom = 1.0e-10 * meter
const val fermi = 1.0e-15 * meter

const val barn = 1.0e-28 * meter2
const val millibarn = 1.0e-3 * barn
const val microbarn = 1.0e-6 * barn
const val nanobarn = 1.0e-9 * barn
const val picobarn = 1.0e-12 * barn

// symbols
const val nm = nanometer
const val um = micrometer

const val mm = millimeter
const val mm2 = millimeter2
const val mm3 = millimeter3

const val cm = centimeter
const val cm2 = centimeter2
const val cm3 = centimeter3

const val liter = 1.0e3 * cm3
const val L = liter
const val dL = 1.0e-1 * liter
const val cL = 1.0e-2 * liter
const val mL = 1.0e-3 * liter

const val m = meter
const val m2 = meter2
const val m3 = meter3

const val km = kilometer
const val km2 = kilometer2
const val km3 = kilometer3

const val pc = parsec

//
// Angle
//
const val radian = 1.0
const val milliradian = 1.0e-3 * radian
const val degree = (pi / 180.0) * radian

const val steradian = 1.0

// symbols
const val rad = radian
const val mrad = milliradian
const val sr = steradian
const val deg = degree

//
// Time [T]
//
const val nanosecond = 1.0
const val second = 1.0e+9 * nanosecond
const val millisecond = 1.0e-3 * second
const val microsecond = 1.0e-6 * second
const val picosecond = 1.0e-12 * second

const val hertz = 1.0/ second
const val kilohertz = 1.0e+3 * hertz
const val megahertz = 1.0e+6 * hertz

// symbols
const val ns = nanosecond
const val s = second
const val ms = millisecond
const val us = microsecond
const val ps = picosecond

//
// Electric charge [Q]
//
const val eplus = 1.0// positron charge
const val e_SI = 1.602176487e-19// positron charge in Coulomb
const val coulomb = eplus / e_SI// Coulomb = 6.24150 e+18 * Eplus

//
// Energy [E]
//
const val megaelectronvolt = 1.0
const val electronvolt = 1.0e-6 * megaelectronvolt
const val kiloelectronvolt = 1.0e-3 * megaelectronvolt
const val gigaelectronvolt = 1.0e+3 * megaelectronvolt
const val teraelectronvolt = 1.0e+6 * megaelectronvolt
const val petaelectronvolt = 1.0e+9 * megaelectronvolt

const val joule = electronvolt / e_SI// Joule = 6.24150 e+12 * MeV

// symbols
const val MeV = megaelectronvolt
const val eV = electronvolt
const val keV = kiloelectronvolt
const val GeV = gigaelectronvolt
const val TeV = teraelectronvolt
const val PeV = petaelectronvolt

//
// Mass [E][T^2][L^-2]
//
const val kilogram = joule * second * second / (meter * meter)
const val gram = 1.0e-3 * kilogram
const val milligram = 1.0e-3 * gram

// symbols
const val kg = kilogram
const val g = gram
const val mg = milligram

//
// Power [E][T^-1]
//
const val watt = joule / second// Watt = 6.24150 e+3 * MeV/Ns

//
// Force [E][L^-1]
//
const val newton = joule / meter// Newton = 6.24150 e+9 * MeV/Mm

//
// Pressure [E][L^-3]
//
const val hep_pascal = newton / m2   // Pascal = 6.24150 e+3 * MeV/Mm3
const val pascal = hep_pascal // a trick to avoid warnings
const val bar = 100000 * pascal // Bar    = 6.24150 e+8 * MeV/Mm3
const val atmosphere = 101325 * pascal // atm    = 6.32420 e+8 * MeV/Mm3

//
// Electric current [Q][T^-1]
//
const val ampere = coulomb / second // Ampere = 6.24150 e+9 * Eplus/Ns
const val milliampere = 1.0e-3 * ampere
const val microampere = 1.0e-6 * ampere
const val nanoampere = 1.0e-9 * ampere

//
// Electric potential [E][Q^-1]
//
const val megavolt = megaelectronvolt / eplus
const val kilovolt = 1.0e-3 * megavolt
const val volt = 1.0e-6 * megavolt

//
// Electric resistance [E][T][Q^-2]
//
const val ohm = volt / ampere// Ohm = 1.60217e-16*(MeV/Eplus)/(Eplus/Ns)

//
// Electric capacitance [Q^2][E^-1]
//
const val farad = coulomb / volt// Farad = 6.24150e+24 * Eplus/Megavolt
const val millifarad = 1.0e-3 * farad
const val microfarad = 1.0e-6 * farad
const val nanofarad = 1.0e-9 * farad
const val picofarad = 1.0e-12 * farad

//
// Magnetic Flux [T][E][Q^-1]
//
const val weber = volt * second// Weber = 1000*Megavolt*Ns

//
// Magnetic Field [T][E][Q^-1][L^-2]
//
const val tesla = volt * second / meter2// Tesla =0.001*Megavolt*Ns/Mm2

const val gauss = 1.0e-4 * tesla
const val kilogauss = 1.0e-1 * tesla

//
// Inductance [T^2][E][Q^-2]
//
const val henry = weber / ampere// Henry = 1.60217e-7*MeV*(Ns/Eplus)**2

//
// Temperature
//
const val kelvin = 1.0

//
// Amount of substance
//
const val mole = 1.0

//
// Activity [T^-1]
//
const val becquerel = 1.0/ second
const val curie = 3.7e+10 * becquerel
const val kilobecquerel = 1.0e+3 * becquerel
const val megabecquerel = 1.0e+6 * becquerel
const val gigabecquerel = 1.0e+9 * becquerel
const val millicurie = 1.0e-3 * curie
const val microcurie = 1.0e-6 * curie
const val Bq = becquerel
const val kBq = kilobecquerel
const val MBq = megabecquerel
const val GBq = gigabecquerel
const val Ci = curie
const val mCi = millicurie
const val uCi = microcurie

//
// Absorbed dose [L^2][T^-2]
//
const val gray = joule / kilogram
const val kilogray = 1.0e+3 * gray
const val milligray = 1.0e-3 * gray
const val microgray = 1.0e-6 * gray

//
// Luminous intensity [I]
//
const val candela = 1.0

//
// Luminous flux [I]
//
const val lumen = candela * steradian

//
// Illuminance [I][L^-2]
//
const val lux = lumen / meter2

//
// Miscellaneous
//
const val perCent = 0.01
const val perThousand = 0.001
const val perMillion = 0.000001

