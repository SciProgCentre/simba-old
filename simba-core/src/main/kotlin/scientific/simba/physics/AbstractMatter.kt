package scientific.simba.physics


interface Isotope {
    val name: String
    val Z: Int
    val N: Int
    val atomicMass: Double
    val m: Int  // isomer
}

data class Ingredient<T>(val ingredient: T, val massFraction: Double)

interface Element {
    val name: String
    val composition: List<Ingredient<Isotope>>
    val Z : Int
    val Aeff: Double
}

val Element.isotopes
    get() = composition.map { it.ingredient }

interface Material {
    val name: String
    val composition: List<Ingredient<Element>>
}

val Material.elements
    get() = composition.map { it.ingredient }
