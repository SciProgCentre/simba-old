package simba.math



import kscience.kmath.geometry.Euclidean3DSpace
import kscience.kmath.geometry.Euclidean3DSpace.norm
import kscience.kmath.geometry.Vector3D
import kscience.kmath.geometry.r
import kscience.kmath.operations.sqr
import kotlin.math.sqrt



operator fun Vector3D.times(alpha : Double): Vector3D{
    return this.context.run { multiply(this@times, alpha)}
}

operator fun Double.times(vector : Vector3D): Vector3D{
    return vector.context.run { multiply(vector, this@times)}
}
//
//operator fun Vector3D.minus(vector: Vector3D) : Vector3D{
//    return this.subtract(vector)
//}
//operator fun Vector3D.plus(vector: Vector3D) : Vector3D{
//    return this.add(vector)
//}
//operator fun Vector3D.times(vector: Vector3D) : Vector3D {
//    return Vector3D(
//            this.x * vector.x,
//            this.y * vector.y,
//            this.z * vector.z
//            )
//}
//
//
//
fun Vector3D.rotateUz(vector: Vector3D): Vector3D {
    // NewUzVector must be normalized !

    val u1 = vector.x;
    val u2 = vector.y;
    val u3 = vector.z;
    var up = u1*u1 + u2*u2;

    if (up>0) {
        up = sqrt(up);
        val px = this.x
        val py = this.y
        val pz = this.z
        val x = (u1 * u3 * px - u2 * py) / up + u1 * pz;
        val y = (u2 * u3 * px + u1 * py) / up + u2 * pz;
        val z = -up * px + u3 * pz;
        return Vector3D(x,y,z);
    }

    if (u3 < 0.0) {
        return Vector3D(-this.x, this.y, -this.z)
        }      // phi=0  theta=pi
    return this
}

/** Null vector (coordinates: 0, 0, 0). */
val Vector3D.ZERO : Vector3D
    get() =  Vector3D(0.0, 0.0, 0.0);

/** First canonical vector (coordinates: 1, 0, 0). */
val Vector3D.PLUS_I : Vector3D 
get() =  Vector3D(1.0, 0.0, 0.0);

/** Opposite of the first canonical vector (coordinates: -1, 0, 0). */
val Vector3D.MINUS_I : Vector3D 
get() =  Vector3D(-1.0, 0.0, 0.0);

/** Second canonical vector (coordinates: 0, 1, 0). */
val Vector3D.PLUS_J : Vector3D 
get() =  Vector3D(0.0, 1.0, 0.0);

/** Opposite of the second canonical vector (coordinates: 0, -1, 0). */
val Vector3D.MINUS_J : Vector3D 
get() =  Vector3D(0.0, -1.0, 0.0);

/** Third canonical vector (coordinates: 0, 0, 1). */
val Vector3D.PLUS_K : Vector3D 
get() =  Vector3D(0.0, 0.0,  1.0);

/** Opposite of the third canonical vector (coordinates: 0, 0, -1).  */
val Vector3D.MINUS_K : Vector3D 
get() =  Vector3D(0.0, 0.0, -1.0);

// CHECKSTYLE: stop ConstantName
/** A vector with all coordinates set to NaN. */
val Vector3D.NaN : Vector3D 
get() =  Vector3D(Double.NaN, Double.NaN, Double.NaN);
// CHECKSTYLE: resume ConstantName

/** A vector with all coordinates set to positive infinity. */
val Vector3D.POSITIVE_INFINITY : Vector3D
get() =  Vector3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

/** A vector with all coordinates set to negative infinity. */
val Vector3D.NEGATIVE_INFINITY : Vector3D
get() =  Vector3D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);

val Vector3D.normalize : Vector3D
    get() = if (r == 0.0) error("CANNOT_NORMALIZE_A_ZERO_NORM_VECTOR") //TODO(Exception)
       else (1/r) * this

val Vector3D.normSquare : Double
    get() = x*x + y*y +z*z