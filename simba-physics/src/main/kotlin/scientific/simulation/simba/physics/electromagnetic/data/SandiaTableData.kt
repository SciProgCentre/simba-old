//// class description
////
//// File for static data of PhotoAbsorption cross section coefficients
//// according to the Sandia parametrisation
////
//// const G4double G4SandiaTable::fSandiaTable[981][5]
////
//// These are the coefficients from F. Biggs, R. Lighthill 'Analytical
//// Approximation for X-ray Cross Sections III', SAND-0070, May 1990.
////
//// fSandiaTable [n][0]    - left energy borders in   keV   !!!
//// fSandiaTable [n][1-4]  - Photoelectric cross-section coefficients
////                          in cm^2 keV^(1-4)/g , respectively
////
//// The first energy intervals and coefficients for Xe are corrected to
//// correspond perfectly to the data of J.B. West, J. Morton 'Absolute
//// Photoionization Cross-sections Tables for Xenon in the VUV and the Soft
//// X-ray regions', Atomic Data and Nuclear Data Tables 22(1978)103-107. The
//// coefficients are checked to correspond perfectly to the data from B.L.
//// Henke et al.,'Low-Energy X-Ray Interaction Coefficients:Photoabsorption,
//// Scattering, and Reflection' Atomic Data and Nuclear Data Tables, 27
//// (1982)1-144.
//// The coeficients for Carbon are checked to correspond perfectly to the
//// data of B.L. Henke et al. (as Xe). The first three energy intervals
//// and coefficients for C are corrected to correspond perfectly to the data
//// of Gallagher et al., J.Phys.Chem.Ref.Data, 17(1)(1988).
//// The coefficients for Oxygen are checked to correspond perfectly to the
//// data of B.L. Henke et al. (as Xe). The first two energy intervals and
//// coefficients for O are corrected to correspond perfectly to the data of
//// Gallagher et al. (as C).
//// The coeficients for Hydrogen are checked to correspond perfectly to the
//// data of B.L. Henke et al. (as Xe). The first three energy intervals
//// and coefficients for H are corrected to correspond perfectly to the data
//// of L.C. Lee et al., J. of Chem.Phys.,67(3)(1977).
//// The first energy intervals and coefficients for He, Ne, Ar, and Kr are
//// corrected to correspond perfectly to the data of G.V. Marr and J.B. West,
//// Absolute Photoionization Cross-section Tables for Helium, Neon, Argon,
//// and Krypton in the VUV Spectral Regions, Atomic Data and Nuclear Data
//// Tables, 18(1976)497-508.
////------------------------------------------------------------------------------
////
////
//// The most of ionization energies are taken from S. Ruben, Handbook of the
//// Elements, 3rd ed. (Open Court, La Salle, IL, 1985). 28 of the ionization
//// energies have been changed slightly to bring them up to date (changes
//// from W.C. Martin and B.N. Taylor of the National Institute os Standards
//// and Technology, January 1990). Here the ionization energy is the least
//// energy necessary to remove to infinity one electron from an atom of the
//// element.
//
//object SandiaTableData {
////    val numberOfElements = 100;
////    val intervalLimit = 100;
////    val numberOfIntervals = 980;
////    val H2OlowerInt = 23;
//
//    //cheker const
//    //TODO (Real Sandia table size = 965, sum of nbofInterval =977)
////    val NUMBER_OF_SANDIA_COEFF = 5
////    val NUMBER_OF_TABLE_ROW = 981
////    val NUMBER_OF_H2O_ROW = 23
//
//    //    const G4double G4SandiaTable::fSandiaTable[981][5]
//    val sandiaTable: List<List<Double>> = javaClass.getResourceAsStream("/data/SandiaTable.txt")
//            .bufferedReader().readLines().filter { it[0] != '#' && it.isNotEmpty() }
//            .map { it.split(CSV_DELIMITER).map { it.toDouble() } }//Array<Array<Int>> = Array(981, { Array(5, { 0 }) })
//
////    init {
////                assert(sandiaTable.size == NUMBER_OF_TABLE_ROW)
////                for (i in 0..NUMBER_OF_TABLE_ROW - 1) assert(sandiaTable[i].size == NUMBER_OF_SANDIA_COEFF)
////    }
////....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo.... ....oooOO0OOooo....
//
//    // Array of interval numbers in Sandia table. This value is the number (integer)
//// of energy interval in which Sandia coeeficients are parametrized for each
//// element.
//    val nbOfIntervals = listOf(//101
//            0,  // nonexisting 'zero' element
//
////    H,                                                     He,         (2)
//            10, 7,
//
////   Li,    Be,     B,    C,     N,     O,     F,            Ne,        (10)
//            7, 7, 7, 10, 8, 8, 7, 8,
//
////   Na,    Mg,    Al,    Si,    P,     S,    Cl,            Ar,        (18)
//            7, 7, 8, 7, 7, 7, 7, 7,
//
////    K,    Ca,    Sc,    Ti,    V,    Cr,    Mn,     Fe,    Co,    Ni, (28)
//            7, 7, 6, 6, 7, 6, 6, 7, 7, 8,
//
////   Cu,    Zn,    Ga,    Ge,    As,    Se,    Br,           Kr,        (36)
//            7, 9, 9, 9, 9, 9, 10, 10,
//
////   Rb,    Sr,    Y,     Zr,    Nb,    Mo,    Tc,    Ru,    Rh,    Pd, (46)
//            10, 10, 10, 9, 10, 10, 10, 10, 10, 10,
//
////   Ag,    Cd,    In,    Sn,    Sb,    Te,     J,           Xe,        (54)
//            10, 11, 11, 11, 11, 11, 12, 13,
//
////   Cs,    Ba,    La,    Ce,    Pr,    Nd,    Pm,    Sm,    Eu,    Gd, (64)
//            11, 11, 12, 13, 13, 12, 13, 11, 13, 12,
//
////   Tb,    Dy,    Ho,    Er,    Tm,    Yb,    Lu,    Hf,    Ta,     W, (74)
//            12, 12, 12, 12, 10, 10, 10, 10, 11, 11,
//
////   Re,    Os,    Ir,    Pt,    Au,    Hg,    Tl,    Pb,    Bi,    Po, (84)
//            10, 10, 10, 11, 10, 10, 11, 11, 13, 11,
//
////   At,   Rn,     Fr,    Ra,    Ac,    Th,    Pa,    U,     Np     Pu, (94)
//            11, 11, 11, 11, 11, 10, 10, 11, 10, 11,
//
////   Am,    Cm,    Bk,    Cf,    Es,    Fm                             (100)
//            11, 11, 11, 11, 12, 12
//
//    )
////....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo.... ....oooOO0OOooo....
//
//    // The ration of atomic number to atomic mass for first 100 elements
//// (which assumes A in amu/atom or in g/mole)
//    val ZtoAratio = listOf( //101
//            0.0,  // nonexisting 'zero' element
//
////    H,                                                             He,         (2)
//            0.9921, 0.4997,
//
////   Li,     Be,      B,      C,      N,      O,      F,             Ne,        (10)
//            0.4322, 0.4438, 0.4625, 0.4995, 0.4998, 0.5, 0.4737, 0.4956,
//
////   Na,     Mg,     Al,     Si,      P,      S,     Cl,             Ar,        (18)
//            0.4785, 0.4937, 0.4818, 0.4985, 0.4843, 0.4991, 0.4795, 0.4506,
//
////    K,     Ca,     Sc,     Ti,      V,     Cr,     Mn,     Fe,     Co,     Ni,(28)
//            0.4860, 0.4990, 0.4671, 0.4595, 0.4515, 0.4616, 0.4551, 0.4656, 0.4581, 0.4771,
//
////   Cu,     Zn,     Ga,     Ge,     As,     Se,     Br,             Kr,        (36)
//            0.4564, 0.4589, 0.4446, 0.4408, 0.4405, 0.4306, 0.4380, 0.4296,
//
////   Rb,     Sr,      Y,     Zr,     Nb,     Mo,     Tc,     Ru,     Rh,     Pd,(46)
//            0.4329, 0.4337, 0.4387, 0.4385, 0.4413, 0.4378, 0.4388, 0.4353, 0.4373, 0.4322,
//
////   Ag,     Cd,     In,     Sn,     Sb,     Te,      J,             Xe,        (54)
//            0.4357, 0.4270, 0.4268, 0.4213, 0.4189, 0.4075, 0.4176, 0.4113,
//
////   Cs,     Ba,     La,     Ce,     Pr,     Nd,     Pm,     Sm,     Eu,     Gd,(64)
//            0.4138, 0.4078, 0.4104, 0.4139, 0.4187, 0.4160, 0.4207, 0.4123, 0.4146, 0.4070,
//
////   Tb,     Dy,     Ho,     Er,     Tm,     Yb,     Lu,     Hf,     Ta,      W,(74)
//            0.4090, 0.4062, 0.4062, 0.4066, 0.4084, 0.4045, 0.4058, 0.4034, 0.4034, 0.4025,
//
//// Re,       Os,     Ir,     Pt,     Au,     Hg,     Tl,     Pb,     Bi,     Po,(84)
//            0.4028, 0.3996, 0.4006, 0.3998, 0.4011, 0.3988, 0.3963, 0.3958, 0.3972, 0.4019,
//
////   At,     Rn,     Fr,     Ra,     Ac,     Th,     Pa,      U,     Np,     Pu,(94)
//            0.4048, 0.3874, 0.3901, 0.3893, 0.3920, 0.3879, 0.3939, 0.3865, 0.3923, 0.3852,
//
////   Am,     Cm,     Bk,     Cf,     Es,     Fm                                (100)
//            0.3909, 0.3887, 0.3927, 0.3904, 0.3929, 0.3891
//
//    )
////....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo.... ....oooOO0OOooo....
//
////....oooOO0OOooo........oooOO0OOooo........oooOO0OOooo.... ....oooOO0OOooo....
//
//    // Ionization potentials of first 100 elements in eV   !!!
////
//// The most of ionization energies are taken from S. Ruben, Handbook of the
//// Elements, 3rd ed. (Open Court, La Salle, IL, 1985). 28 of the ionization
//// energies have been changed slightly to bring them up to date (changes
//// from W.C. Martin and B.N. Taylor of the National Institute os Standards
//// and Technology, January 1990). Here the ionization energy is the least
//// energy necessary to remove to infinity one electron from an atom of the
//// element.
//    val ionizationPotentials = listOf( //101
//            0.0,  // nonexisting 'zero' element
//
////   H,                                                     He,         (2)
//            13.60, 24.59,
//
////  Li,    Be,     B,     C,     N,     O,     F,           Ne,        (10)
//            5.39, 9.32, 8.30, 11.26, 14.53, 13.62, 17.42, 21.56,
//
////  Na,    Mg,    Al,    Si,     P,     S,    Cl,           Ar,        (18)
//            5.14, 7.65, 5.99, 8.15, 10.49, 10.36, 12.97, 15.76,
//
////   K,    Ca,    Sc,    Ti,     V,    Cr,    Mn,    Fe,    Co,    Ni, (28)
//            4.34, 6.11, 6.56, 6.83, 6.75, 6.77, 7.43, 7.90, 7.88, 7.64,
//
////  Cu,    Zn,    Ga,    Ge,    As,    Se,    Br,           Kr,        (36)
//            7.73, 9.39, 6.00, 7.90, 9.82, 9.75, 11.81, 14.00,
//
////  Rb,    Sr,     Y,    Zr,    Nb,    Mo,    Tc,    Ru,    Rh,    Pd, (46)
//            4.18, 5.69, 6.22, 6.63, 6.76, 7.09, 7.28, 7.36, 7.46, 8.34,
//
////  Ag,    Cd,    In,    Sn,    Sb,    Te,     J,           Xe,        (54)
//            7.58, 8.99, 5.79, 7.34, 8.64, 9.01, 10.45, 12.13,
//
////  Cs,    Ba,    La,    Ce,    Pr,    Nd,    Pm,    Sm,    Eu,    Gd, (64)
//            3.89, 5.21, 5.58, 5.54, 5.46, 5.52, 5.55, 5.64, 5.67, 6.15,
//
////  Tb,    Dy,    Ho,    Er,    Tm,    Yb,    Lu,    Hf,    Ta,     W, (74)
//            5.86, 5.94, 6.02, 6.11, 6.18, 6.25, 5.43, 6.83, 7.89, 7.98,
//
////  Re,    Os,    Ir,    Pt,    Au,    Hg,    Tl,    Pb,    Bi,    Po, (84)
//            7.88, 8.70, 9.10, 9.00, 9.23, 10.44, 6.11, 7.42, 7.29, 8.42,
//
////  At,    Rn,    Fr,    Ra,    Ac,    Th,    Pa,     U,    Np,    Pu, (94)
//            9.65, 10.75, 3.97, 5.28, 5.17, 6.08, 5.89, 6.19, 6.27, 6.06,
//
////  Am,    Cm,    Bk,    Cf,    Es,    Fm                             (100)
//            5.99, 6.02, 6.23, 6.30, 6.42, 6.50
//
//    )    // ............... end of fIonizationPotentials array
//
//    val H2Olower: List<List<Double>> = javaClass.getResourceAsStream("/data/SandiaTableH20.txt")
//            .bufferedReader().readLines()
//            .map { it.split(CSV_DELIMITER).map { it.toDouble() } }// Array<Array<Int>> = Array(23, { Array(5, {0}) }) //
//
////    init {
////                assert(H2Olower.size == NUMBER_OF_H2O_ROW)
////        for (i in 0..NUMBER_OF_H2O_ROW -1) assert(H2Olower[i].size == NUMBER_OF_SANDIA_COEFF)
////    }
//
//    val cumulInterval: List<Int>
//
//    init {
//        val cumulInterval = Array<Int>(101, { 1 })
//        for (Z in 1..100) {
//            cumulInterval[Z] = cumulInterval[Z - 1] + nbOfIntervals[Z]
//        }
//        this.cumulInterval = cumulInterval.toList()
//    }
//
//    val funitc = listOf(
//            keV,
//            cm2 * keV / g,
//            cm2 * keV * keV / g,
//            cm2 * keV * keV * keV / g,
//            cm2 * keV * keV * keV * keV / g
//    )
//
//    fun getSandiaCofPerAtom(Z: Int, energy: Double): Array<Double> {
//        val coeff = Array(4, { 0.0 })
//        val Emin = sandiaTable[cumulInterval[Z - 1]][0] * keV
//        //G4double Iopot = fIonizationPotentials[Z]*eV;
//        //if (Emin  < Iopot) Emin = Iopot;
//        val energy_: Double
//        var row = 0
//        if (energy <= Emin) {
//            energy_ = Emin
//
//        } else {
//            var interval = nbOfIntervals[Z] - 1
//            row = cumulInterval[Z - 1] + interval
//            // Loop checking, 07-Aug-2015, Vladimir Ivanchenko
//            while ((interval > 0) && (energy < sandiaTable[row][0] * keV)) {
//                --interval
//                row = cumulInterval[Z - 1] + interval
//            }
//        }
//
//        val AoverAvo = Z * amu / ZtoAratio[Z]
//
//        for (i in 0..3) coeff[i] = AoverAvo * funitc[i + 1] * sandiaTable[row][i + 1]
//        return coeff
//    }
//}