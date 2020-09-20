package ru.mipt.npm.mcengine.geant4

import junit.framework.TestCase
import ru.mipt.npm.mcengine.geant4.gdml.GDMLIO
import javax.xml.parsers.DocumentBuilderFactory

class GDMLIOTest : TestCase(){

    fun testRead(){

        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                javaClass.getResourceAsStream("/gdml/test.gdml")
        )
        assertEquals("Default", doc.getElementsByTagName("setup").item(0).attributes.getNamedItem("name").textContent)

//        val elements = doc.getElementsByTagName("material")
//
//        for (i in 0..elements.length - 1){
//            println(elements.item(i).attributes.getNamedItem("name"))
//        }
    }

    fun testGDMLRead(){
        val doc = GDMLIO.read(javaClass.getResource("/gdml/test.gdml").toURI()).document
        assertEquals("Default", doc.getElementsByTagName("setup").item(0).attributes.getNamedItem("name").textContent)
    }

}