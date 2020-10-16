package scientific.simba.physics.nist

import hep.dataforge.context.Global
import hep.dataforge.meta.Meta
import hep.dataforge.meta.set
import hep.dataforge.meta.update
import scientific.simba.physics.material.IsotopeAnnotation
import scientific.simba.physics.testConfiguration
import kotlin.test.Test
import kotlin.test.assertEquals

class NISTIsotopeTest {

    init {
        Global.configure {
            this.update(testConfiguration.toMeta())
        }
    }

    @Test
    fun test(){
        val nistDataLoader = NISTIsotopeLoader()
        nistDataLoader.allItem().forEach {
            println(it)
        }
    }

    @Test
    fun testLoad(){
        val nistDataLoader = NISTIsotopeLoader()
        val data = nistDataLoader.load(IsotopeAnnotation(107, 270))
        assertEquals(data?.data?.atomicMass, 270.13336)
    }

}