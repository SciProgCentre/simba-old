package ru.mipt.npm.mcengine.geant4

import junit.framework.TestCase
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import ru.mipt.npm.mcengine.extensions.minus
import ru.mipt.npm.mcengine.extensions.plus
import ru.mipt.npm.mcengine.extensions.rotateUz

class HEPVector3DKtTest : TestCase() {

//    fun testTimes() {
//        val a = Vector3D(1.0, 2.0, 3.0)
//        assertEquals( (2.0*a).z, 6.0)
//    }

    fun testMinus() {
        val a = Vector3D(1.0, 2.0, 3.0)
        val b = Vector3D(-1.0, -2.0, -3.0)

        assertEquals((a-b).x, 2.0)
    }

    fun testPlus() {
        val a = Vector3D(1.0, 2.0, 3.0)
        val b = Vector3D(-1.0, -2.0, -3.0)

        assertEquals((a+b).x, 0.0)
    }


    fun testRotateUz() {
        val a = Vector3D(0.0, 0.0, 1.0)
        val b = Vector3D(1.0, 0.0, 0.0)
        assertEquals(a.rotateUz(b).x, 1.0)
    }
}