package simba.physics



interface Isotope {
    val name: String
    val Z: Int
    val A: Int
    val atomicMass: Double
    val m: Int  // isomer
}

data class CommonIsotope(
    override val name: String,
    override val Z: Int,
    override val A: Int,
    override val atomicMass: Double,
    override val m: Int
) : Isotope

data class Ingredient<T>(val ingredient: T, val massFraction: Double = 1.0)

interface Element {
    val name: String
    val composition: List<Ingredient<Isotope>>
    val Z : Int
    val Aeff: Double
}

data class CommonElement(
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

data class CommonMaterial(override val name: String, override val composition: List<Ingredient<Element>>) : Material

val Material.elements
    get() = composition.map { it.ingredient }
