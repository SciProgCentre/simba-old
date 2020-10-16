package scientific.simba.physics



interface Isotope {
    val name: String
    val Z: Int
    val A: Int
    val atomicMass: Double
    val m: Int  // isomer
}

data class Ingredient<T>(val ingredient: T, val massFraction: Double = 1.0)

interface Element {
    val name: String
    val composition: List<Ingredient<Isotope>>
    val Z : Int
    val Aeff: Double
}

data class SimpleElement(
    override val name: String,
    override val Z: Int,
    override val Aeff: Double,
    override val composition: List<Ingredient<Isotope>> = emptyList()
) : Element

val Element.isotopes
    get() = composition.map { it.ingredient }

interface Material {
    val name: String
    val composition: List<Ingredient<Element>>
}

val Material.elements
    get() = composition.map { it.ingredient }
