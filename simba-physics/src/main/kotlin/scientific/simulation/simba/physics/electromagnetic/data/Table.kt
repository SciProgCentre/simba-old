package ru.mipt.npm.mcengine.material

interface IsotopesTable{
    fun getIsotope(name: String) : Isotope
    fun getIsotope(name: String, nucleons: Int) : Isotope
    fun getIsotope(Z: Int, nucleons: Int) : Isotope
}

interface ElementsTable {
    fun getElement(name: String): Element
    fun getElement(Z: Int): Element
}

interface MaterialsTable {
    fun getMaterial(name : String) : Material
}