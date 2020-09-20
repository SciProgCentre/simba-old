package ru.mipt.npm.mcengine.geant4.material

import ru.mipt.npm.mcengine.material.IsotopesTable
import ru.mipt.npm.mcengine.material.Isotope
import ru.mipt.npm.mcengine.utils.amu_c2
import ru.mipt.npm.mcengine.utils.CSV_DELIMITER

object G4IsotopesTable : IsotopesTable {
    private val datafile = "/data/G4IsotopesTable.csv"
    private val nameTable : Map<String, Isotope>
    private val ZNTable : Map<Int, Map<Int, Isotope>>
    init {
        val data = javaClass.getResourceAsStream(datafile)
                .bufferedReader().readLines().filter { it[0] != '#' && it.isNotEmpty() }
                .map { it.split(CSV_DELIMITER)}.map{
            Isotope(it[0], it[1].toInt(), it[2].toInt(),it[3].toDouble()/ amu_c2)
        }
        nameTable = mapOf<String, Isotope>(*data.map{Pair("${it.name}_${it.N}", it)}.toTypedArray())
        val temp = HashMap<Int, HashMap<Int, Isotope>>()
        data.forEach{
            if (!temp.containsKey(it.Z.toInt())){
                temp[it.Z.toInt()] = HashMap<Int, Isotope>()
            }
            temp[it.Z.toInt()]!![it.N.toInt()] = it
        }
        ZNTable = temp.toMap() //TODO(переделать)
    }

    override fun getIsotope(name: String) : Isotope {
        return nameTable[name] ?: error("Isotope with name = $name don't exist!")
    }

    override fun getIsotope(name: String, nucleons: Int): Isotope {
        return getIsotope(name+"_$nucleons")
    }

    override fun getIsotope(Z: Int, nucleons: Int) : Isotope =
            ZNTable[Z]?.get(nucleons) ?: error("Isotope with Z = $Z and N = $nucleons don't exist!")

    fun printTable() = nameTable.forEach{println("${it.key} : ${it.value}")}

}