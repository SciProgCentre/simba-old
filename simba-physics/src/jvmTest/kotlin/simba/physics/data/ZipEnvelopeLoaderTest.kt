package simba.physics.data

import hep.dataforge.context.Context
import hep.dataforge.context.Global
import hep.dataforge.meta.get
import hep.dataforge.meta.string
import hep.dataforge.names.asName
import kotlinx.io.text.readUtf8String
import simba.physics.testConfiguration
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertTrue

class ZipEnvelopeLoaderTest {

    val context = Context(
        "SimbaTest".asName(),
        Global,
        testConfiguration.toMeta()
    )

    @Test
    fun testManifest(){
        val filename = "materials.zip.df"
        val path = context.resolveDataPath(filename)
        val zip = ZipEnvelopeLoader.open(context, path)
        val envelope = zip.load("manifest.df")
        assertTrue(envelope?.meta.get("description").string == "Material database")
    }

    @Test
    fun testBinary(){
        val filename = "materials.zip.df"
        val path = context.resolveDataPath(filename)
        val zip = ZipEnvelopeLoader.open(context, path)
        val envelope = zip.load("isotopes/NIST_isotopes.df")
        envelope?.data?.read {
            println(readUtf8String())
        }
    }
}