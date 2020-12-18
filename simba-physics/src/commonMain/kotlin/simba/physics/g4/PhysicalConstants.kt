package simba.physics.g4


// ----------------------------------------------------------------------
// HEP coherent Physical Constants
//
// This file has been provided by Geant4 (simulation toolkit for HEP).
//
// The basic units are :
//  	Millimeter
// 		Nanosecond
// 		Mega electron Volt
// 		positon charge
// 		Degree Kelvin
//      amount of substance (Mole)
//      luminous intensity (Candela)
// 		Radian
//      Steradian
//
// Below is a non exhaustive list of Physical CONSTANTS,
// computed in the Internal HEP System Of Units.
//
// Most of them are extracted from the Particle Data Book :
//        Phys. Rev. D  volume 50 3-1 (1994) page 1233
//
//        ...with a meaningful (?) name ...
//
// You can add your own constants.
//
// Author: M.Maire

//
//
//
const val Avogadro = 6.02214179e+23 / mole;


const val c_light = 2.99792458e+8 * m / s;
const val c_squared = c_light * c_light;


const val h_Planck = 6.62606896e-34 * joule * s;
const val hbar_Planck = h_Planck / twopi;
const val hbarc = hbar_Planck * c_light;
const val hbarc_squared = hbarc * hbarc;

//
//
//
const val electron_charge = -eplus; // see SystemOfUnits.h
const val e_squared = eplus * eplus;


const val electron_mass_c2 = 0.510998910 * MeV;
const val proton_mass_c2 = 938.272013 * MeV;
const val neutron_mass_c2 = 939.56536 * MeV;
const val amu_c2 = 931.494028 * MeV;
const val amu = amu_c2 / c_squared;


const val mu0 = 4 * pi * 1.0e-7 * henry / m;
const val epsilon0 = 1.0 / (c_squared * mu0);


const val elm_coupling = e_squared / (4 * pi * epsilon0);
const val fine_structure_const = elm_coupling / hbarc;
const val classic_electr_radius = elm_coupling / electron_mass_c2;
const val electron_Compton_length = hbarc / electron_mass_c2;
const val Bohr_radius = electron_Compton_length / fine_structure_const;

const val alpha_rcl2 = fine_structure_const * classic_electr_radius * classic_electr_radius;

const val twopi_mc2_rcl2 = twopi * electron_mass_c2 * classic_electr_radius * classic_electr_radius;
//
//
//
const val k_Boltzmann = 8.617343e-11 * MeV / kelvin;

//
//
//
const val STP_Temperature = 273.15 * kelvin;
const val STP_Pressure = 1.0 * atmosphere;
const val kGasThreshold = 10.0 * mg / cm3;

//
//
//
const val universe_mean_density = 1.0e-25 * g / cm3;

//  positive Bohr Magnetron
const val muB = 0.5 * eplus * hbar_Planck / (electron_mass_c2 / c_squared)

const val NTP_Temperature = 293.15;



