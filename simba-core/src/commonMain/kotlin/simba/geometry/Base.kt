package simba.geometry


import kscience.kmath.geometry.Euclidean3DSpace.norm
import kscience.kmath.geometry.Vector3D
import kscience.kmath.geometry.r
import kotlin.math.abs
import kotlin.math.PI

interface Solid{
    /**
     *  Определяет расположена ли [point] внутри объема
     *  @param point Точка относительно центра объема
     *  @return true if point into solid включительно
     */
    fun inSolid(point : Vector3D): Boolean
}

class Box(val x: Double, val y: Double, val z: Double) : Solid {
    override fun inSolid(point: Vector3D): Boolean {
        return abs(point.x) <= x / 2 && abs(point.y) <= y / 2 && abs(point.z) <= z / 2
    }
}


/**
 *  Build a sphere, or a spherical shell section
 *  @param innerRadius Inner radius
 *  @param outerRadius Outer radius
 *  @param startPhi Starting Phi angle of the segment in radians
 *  @param deltaPhi Delta Phi angle of the segment in radians
 *  @param startTheta Starting Theta angle of the segment in radians
 *  @param deltaTheta Delta Theta angle of the segment in radians
 *
 */
class Sphere(
        val innerRadius : Double = 0.0,
        val outerRadius : Double,
        val startPhi : Double = 0.0,
        val deltaPhi : Double = 2*PI,
        val startTheta : Double = 0.0,
        val deltaTheta : Double = PI
) : Solid {
    override fun inSolid(point: Vector3D): Boolean {
        TODO("Not yet implemented")
    }
}

/**
 * Build a full solid sphere
 * @param radius Outer radius
 */
class Orb(val radius : Double) : Solid {
    override fun inSolid(point: Vector3D): Boolean {
        return point.norm() < radius
    }
}


//interface DensityDistribution{
//    fun getDesnity(position : Vector3D) : Double
//    fun getMaxDensity() : Double
//}
//
//class UniformDensityDistribution(val density : Double) : DensityDistribution {
//    override fun getMaxDensity(): Double = density
//    override fun getDesnity(position: Vector3D): Double = density
//}
//

////Сделать класс шаблонным относительно векторов полей
//class Volume(
//        val solid : Solid,
//        val material: Material,
//        val densityDistribution: DensityDistribution,
//        val electricField: ElectricField? = null,
//        val centerPoint : Vector3D
//){
//    fun inVolume(point: Vector3D) : Boolean{
//        return solid.inSolid(point - centerPoint)
//    }
//}
//
