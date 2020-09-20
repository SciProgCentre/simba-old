package ru.mipt.npm.mcengine.geant4.material

import ru.mipt.npm.mcengine.material.Element
import ru.mipt.npm.mcengine.material.ElementsTable
import ru.mipt.npm.mcengine.material.Isotope
import ru.mipt.npm.mcengine.utils.CSV_DELIMITER

object G4ElementsTable : ElementsTable {
    private val datafile = "/data/G4ElementsTable.csv"
    private val nameTable : Map<String, Element>
    private val ZTable : Map<Int, Element>
    val Aeff = HashMap<Int, Double>()

    val maxZ = 92  //TODO(проблема в ионизационых параметрах)
    init{
        //TODO(Переписать все !!)

        val elements = javaClass.getResourceAsStream(datafile)
                .bufferedReader().readLines().filter { it[0] != '#' && it.isNotEmpty() }
                .map { it.split(CSV_DELIMITER)}.filter { it[1].toInt() <= maxZ
                }.map {
                    val Z = it[1].toInt()
                    Aeff[Z] = it[2].toDouble()
                    val n = it[3].toInt()

                    val isoComp = ArrayList<Pair<Isotope, Double>>()
                    for (i in 1..n){
                        val nN = i+3
                        val nRelAbundance = 3 + i + n
                        val relAbundance = it[nRelAbundance].toDouble()
                        if (relAbundance != 0.0){
                            val pair = Pair(
                                    G4IsotopesTable.getIsotope(Z, it[nN].toInt()),
                                    relAbundance
                            )
                            isoComp.add(pair)
                        }
                    }
//                    println("${relativeAbundance.size} _ ${isotops.size}")
                    Element(it[0],isoComp) }

        nameTable = mapOf(*elements.map{Pair(it.name, it)}.toTypedArray())
        ZTable = mapOf(*elements.map{Pair(it.Z, it)}.toTypedArray())
    }
    override fun getElement(name: String): Element = nameTable[name] ?: error("Element with name $name does not exist")

    override fun getElement(Z: Int): Element = ZTable[Z] ?: error("Element with Z = $Z does not exist")

    fun printTable(){
        for (Z in 1..maxZ){
            val element = ZTable[Z]!!
            println("${element.name}\t${element.Z}\t${element.Aeff}")
        }
    }
}