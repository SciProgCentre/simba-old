package simba.physics.g4.electromagnetic.models
import simba.physics.Element
import simba.physics.g4.fine_structure_const

//  Compute Coulomb correction factor (Phys Rev. D50 3-1 (1994) page 1254)
val Element.coulomb: Double
//    TODO("Rewrite this")
    get() = {
        val k1 = 0.0083
        val k2 = 0.20206
        val k3 = 0.0020
        val k4 = 0.0369
        val Zeff = Z.toDouble() // TODO()
        val az2 = (fine_structure_const * Zeff) * (fine_structure_const * Zeff)
        val az4 = az2 * az2
        (k1 * az4 + k2 + 1.0 / (1.0 + az2)) * az2 - (k3 * az4 + k4) * az4
    }()
//
//
////
////  Compute Tsai's Expression for the Radiation Length
////  (Phys Rev. D50 3-1 (1994) page 1254)
//val Element.radTsai: Double
//    get() = {
//        val Zeff = Z.toDouble() // TODO()
//        val Lrad_light = listOf(5.31, 4.79, 4.74, 4.71)
//        val Lprad_light = listOf(6.144, 5.621, 5.805, 5.924)
//        val logZ3 = ln(Zeff) / 3.0;
//        val Lrad: Double
//        var Lprad: Double
//        val log184 = ln(184.15);
//        val log1194 = ln(1194.0);
//        if (Z - 1 <= 3) {
//            Lrad = Lrad_light[Z - 1]
//            Lprad = Lprad_light[Z - 1]
//        }
//        else {
//            Lrad = log184 - logZ3
//            Lprad = log1194 - 2 * logZ3
//        }
//
//        4 * alpha_rcl2 * Zeff * (Zeff * (Lrad - simba.physics.g4.electromagnetic.models.getCoulomb) + Lprad)
//    }()
//
//// End element extensions //
//
//
////Extensions Materials //
///*
//Вектор типа концентраций элементов (без плотности)
// */
//
//val Material.vecNbOfAtomsPerVolume: List<Double>
//    get() = elementComposition.map { it.second / it.first.Aeff } //Без плотности!!!
//
//val Material.sandiaTable: SandiaTable
//    get() = SandiaTable(this)
//
////  End materials extensions//