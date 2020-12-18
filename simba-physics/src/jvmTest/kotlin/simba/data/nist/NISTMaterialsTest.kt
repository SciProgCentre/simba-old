package simba.data.nist

import hep.dataforge.context.Context
import hep.dataforge.context.Global
import hep.dataforge.names.asName
import simba.data.material.ElementsPlugin
import simba.data.material.IsotopeAnnotation
import simba.data.material.IsotopesPlugin
import simba.physics.testConfiguration
import kotlin.test.Test
import kotlin.test.assertEquals

class NISTMaterialsTest {


    val context = Context(
        "SimbaTest".asName(),
        Global,
        testConfiguration.toMeta()
    )

    @Test
    fun testAllIsotopes(){
        val nistDataLoader = NISTIsotopeLoader(context = context)
        nistDataLoader.allItem().forEach {
            println(it)
        }
    }

    @Test
    fun testLoadIsotope(){
        val nistDataLoader = NISTIsotopeLoader(context = context)
        val data = nistDataLoader.load(IsotopeAnnotation(107, 270))
        assertEquals(data?.data?.atomicMass, 270.13336)
    }

    @Test
    fun testAllElements(){
        val isotopePlugin = IsotopesPlugin(context = context)
        isotopePlugin.attach(context)
        isotopePlugin.register(NISTIsotopeLoader)
        context.plugins.load(isotopePlugin)
        val elementLoader = NISTElementLoader(context = context)
        elementLoader.allItem().forEach {
            println(it)
        }
    }

    @Test
    fun testAllMaterials(){
        val isotopePlugin = IsotopesPlugin(context = context)
        isotopePlugin.attach(context)
        isotopePlugin.register(NISTIsotopeLoader)
        context.plugins.load(isotopePlugin)

        val elementPlugin = ElementsPlugin(context = context)
        elementPlugin.attach(context)
        elementPlugin.register(NISTElementLoader)
        context.plugins.load(elementPlugin)

        val materialLoader = NISTMaterialLoader(context = context)
        materialLoader.allItem().forEach {
            println(it)
        }
    }
}