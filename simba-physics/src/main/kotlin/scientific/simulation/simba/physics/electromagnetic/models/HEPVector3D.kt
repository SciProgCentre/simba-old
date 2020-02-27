package scientific.simulation.simba.physics.electromagnetic.models

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D
import kotlin.math.sqrt


fun Vector3D.test(){
    println("test extensions")
}

operator fun Vector3D.times(alpha : Double): Vector3D{
    return Vector3D(alpha, this)
}

operator fun Double.times(vector : Vector3D): Vector3D{
    return Vector3D(this, vector)
}

operator fun Vector3D.minus(vector: Vector3D) : Vector3D{
    return this.subtract(vector)
}
operator fun Vector3D.plus(vector: Vector3D) : Vector3D{
    return this.add(vector)
}
operator fun Vector3D.times(vector: Vector3D) : Vector3D {
    return Vector3D(
            this.x * vector.x,
            this.y * vector.y,
            this.z * vector.z
            )
}



fun Vector3D.rotateUz(vector: Vector3D): Vector3D {
    // NewUzVector must be normalized !

    var u1 = vector.x;
    var u2 = vector.y;
    var u3 = vector.z;
    var up = u1*u1 + u2*u2;

    var x : Double
    var y : Double
    var z : Double

    if (up>0) {
        up = sqrt(up);
        var px = this.x
        var py = this.y
        var pz = this.z
        x = (u1 * u3 * px - u2 * py) / up + u1 * pz;
        y = (u2 * u3 * px + u1 * py) / up + u2 * pz;
        z = -up * px + u3 * pz;
        return Vector3D(x,y,z);
    }

    if (u3 < 0.0) {
        return Vector3D(-this.x, this.y, -this.z)
        }      // phi=0  teta=pi
    return this

//    return null;
}