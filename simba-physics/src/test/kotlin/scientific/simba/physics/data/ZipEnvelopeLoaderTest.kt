package scientific.simba.physics.data

import hep.dataforge.context.Global
import hep.dataforge.meta.get
import hep.dataforge.meta.string
import hep.dataforge.meta.update
import kotlinx.io.text.readUtf8String
import scientific.simba.physics.testConfiguration
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertTrue

class ZipEnvelopeLoaderTest {

    init {
        Global.configure {
            this.update(testConfiguration.toMeta())
        }
    }

    @Test
    fun testManifest(){
        val filename = "materials.zip.df"
        val path = Paths.get(Global.properties["dataLocation"].string ?: ".", filename)
        val zip = ZipEnvelopeLoader.open(Global, path)
        val envelope = zip.load("manifest.df")
        assertTrue(envelope?.meta.get("description").string == "Material database")
    }

    @Test
    fun testBinary(){
        val filename = "materials.zip.df"
        val path = Paths.get(Global.properties["dataLocation"].string ?: ".", filename)
        val zip = ZipEnvelopeLoader.open(Global, path)
        val envelope = zip.load("isotopes/NIST_isotopes.df")
        envelope?.data?.read {
            println(readUtf8String())
        }
    }
}