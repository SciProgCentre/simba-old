//package ru.mipt.npm.mcengine.materials.table
//
//import junit.framework.TestCase
//
//class G4ElementsTableTest : TestCase() {
//
//    fun testPrintTable() {
//        G4ElementsTable.printTable()
//    }
//
//    fun testGetElement(){
//        for (Z in 1..G4ElementsTable.maxZ){
//            val element = G4ElementsTable.getElement(Z)
//            assertEquals(element.Aeff, G4ElementsTable.Aeff[Z]!!, 1e-3)//TODO(переменная точность)
//        }
//    }
//}