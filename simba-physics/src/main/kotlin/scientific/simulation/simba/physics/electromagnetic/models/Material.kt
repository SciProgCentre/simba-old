package scientific.simulation.simba.physics.electromagnetic.models



/*
atomic_mass in amu (а.е.м) TODO(Уточнить дефолтные единицы измерения)
 */
data class Isotope(val name: String,
                   val Z: Int,
                   val N: Int,
                   val atomicMass: Double = 0.0,
                   val m: Int = 0) // isomer


class Element(
        val name: String,
        val composition : List<Pair<Isotope, Double>>
){
    constructor(name : String, isotope : Isotope): this(name, listOf(isotope to 1.0))
    val isotopes = composition.map { it.first }
    val Z : Int

    init{
        Z = isotopes.first().Z
        assert(isotopes.all { it.Z == Z})
    }

    val Aeff: Double
    init{
        var wtSum = 0.0;
        var aeff = 0.0;
        composition.forEach({
            aeff += it.first.atomicMass * it.second
            wtSum += it.second })
        assert(wtSum > 0.0)
        if (wtSum > 0.0) {
            aeff /= wtSum; }
        Aeff = aeff
    }
}


class Material(
        val name : String,
        val composition : List<Pair<Element, Double>>
){
    constructor(name: String, element: Element) : this(name, listOf(element to 1.0))

    val elements = composition.map { it.first }
//    val massFractions = elementComposition.map{it.second}
    val ratioWeightMolMass = composition.map { it.second/(it.first.Aeff*amu)  }
    fun ElectronDensity(): Double{
        TODO()
    }
}

enum class State {
    Undefined,
    Solid,
    Liquid,
    Gas }




