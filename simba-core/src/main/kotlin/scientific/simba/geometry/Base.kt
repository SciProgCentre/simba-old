package scientific.simba.geometry

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import kotlin.math.abs

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
