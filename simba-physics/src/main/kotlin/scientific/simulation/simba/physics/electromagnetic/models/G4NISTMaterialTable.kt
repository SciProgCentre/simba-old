//package ru.mipt.npm.mcengine.geant4.material
//
//import ru.mipt.npm.mcengine.material.MaterialsTable
//import ru.mipt.npm.mcengine.material.Material
//import ru.mipt.npm.mcengine.utils.cm3
//import ru.mipt.npm.mcengine.utils.gram
//import ru.mipt.npm.mcengine.utils.CSV_DELIMITER
//
//object G4NISTMaterialTable : MaterialsTable {
//    private val NameTable = HashMap<String, Material>()
//
////    private val densityFile = "/data/G4MaterialDensityTable.txt"
//    private val simplefile = "/data/G4SimpleMaterials.txt"
////    private val compaundChemicalfile = "/data/G4CompaundMaterialsChemical.txt"
////    private val compaundWeightfile = "/data/G4CompaundMaterialsWeigth.txt"
//    private val compaundWeightfile = "/data/G4CompaundMaterials.txt"
//    val densityTable: MutableMap<String, Double> = HashMap() //TODO(неизменяемая)
////
////    init {
////        val temp = javaClass.getResourceAsStream(densityFile)
////                .bufferedReader().readLines().filter { it[0] != '#' && it.isNotEmpty() }
////                .map {
////                    val line = it.split(CSV_DELIMITER)
////                    Pair(line[0], line[1].toDouble())
////                }
////        densityTable = mapOf(*temp.toTypedArray())
////    }
//
//
//    init {
//        //Simple materials
//        //TODO(Переписать все !!)
//
//        javaClass.getResourceAsStream(simplefile)
//                .bufferedReader().readLines().filter { it[0] != '#' && it.isNotEmpty() }
//                .map { it.split("\\s+".toRegex()).filter { it.isNotEmpty() } }.filter { it[0].toInt() < G4ElementsTable.maxZ }.forEach {
//                    val Z = it[0].toInt() - 1 //Смотри файл
//                    val material = Material(it[1], G4ElementsTable.getElement(Z))
//                    NameTable[it[1]] = material
//                    densityTable[it[1]] = it[2].toDouble()
//                }
//    }
//
////    init {
////        //Compaund Chemical
////        javaClass.getResourceAsStream(compaundChemicalfile).bufferedReader().readLines().filter { it[0] != '#' && it.isNotEmpty() }.chunked(5).forEach {
////            val nComp = it[0].toInt()
////            val name = it[1]
////            densityTable[name] = it[2].toDouble()
////            val ionPotencial = it[3].toDouble()
////            val elements = it[3].split(CSV_DELIMITER).map { G4ElementsTable.getElement(it.toInt()) }
////            val nElemets = it[4].split(CSV_DELIMITER).map { it.toInt() }
////            assert(nComp == elements.size)
////            assert(nComp == nElemets.size)
////            val massFractions = (nElemets zip elements).map { it.first * it.second.Aeff }
////
////
////            NameTable[name] = Material(name, elements zip massFractions)
////
////        }
////    }
//
////    init {
////        //Compaund Weight
////        javaClass.getResourceAsStream(compaundWeightfile).bufferedReader().readLines().filter { it[0] != '#' && it.isNotEmpty() }.chunked(5).forEach {
////            val nComp = it[0].toInt()
////            val name = it[1]
////            val ionPotencial = it[2].toDouble()
////            val elements = it[3].split(CSV_DELIMITER).map { G4ElementsTable.getElement(it.toInt()) }
////            val massFractions = it[4].split(CSV_DELIMITER).map{it.toDouble()}
////            assert(nComp == elements.size)
////            assert(nComp ==massFractions.size)
////            NameTable[name] = Material(name, elements zip massFractions)
////
////        }
////    }
//    init{
//    val lines = javaClass.getResourceAsStream(compaundWeightfile).bufferedReader().readLines().filter { it[0] != '#' && it.isNotEmpty() }
//    var flag = true
//    var ncomp = 0
//    var name = ""
//    var count = 0
//    var z = emptyList<Int>().toMutableList()
//    var w = emptyList<Double>().toMutableList()
//    for (i in 0..lines.size-1){
//        if (flag){
////            println(lines[i])
//            val temp = lines[i].split(" ").filter { it.isNotEmpty()}
////            println(temp)
//            ncomp = temp[0].toInt()
//            name = temp[1]
//            densityTable[name] = temp[2].toDouble()
//            flag = false
//            count = 0
//            z = emptyList<Int>().toMutableList()
//            w = emptyList<Double>().toMutableList()
//        }
//        else{
//            count++
//
//            val temp = lines[i].split(" ").filter { it.isNotEmpty()}
//            z.add(temp[0].toInt())
//            w.add(temp[1].toDouble())
//
//            if (count == ncomp){
//                flag = true
//                if (z.max()!! < 90){
//                NameTable[name] = Material(name, z.map { G4ElementsTable.getElement(it) } zip w)}
//            }
//
//        }
//    }
//}
//
//    override fun getMaterial(name: String): Material {
//        return NameTable[name] ?: error("Material $name doesn't exist")
//    }
//
//    fun printTable() {
//        NameTable.toSortedMap().forEach { println("${it.key}") }
//    }
//
//}