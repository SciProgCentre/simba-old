package simba.geometry

//interface Field<FieldVector>{
//    //  Each type/class of field should respond this accordingly
//    //  For example:
//    //    - an electric field     should return "true"
//    //    - a pure magnetic field should return "false"
//    val doesFieldChangeEnergy : Boolean
//    // Given the position vector and time,
//    // return the value of the field.
//    fun getField(coord : Vector3D, time : Double) : FieldVector
//}
//
//abstract class Field3D : Field<Vector3D>
//
//abstract class ElectricField : Field3D() {
//    override val doesFieldChangeEnergy = true
//}
//
//abstract class MagneticField : Field3D() {
//    override val doesFieldChangeEnergy = false
//}
//
//class UniformElectricField(val E : Vector3D) : ElectricField() {
//    override fun getField(coord: Vector3D, time: Double): Vector3D = E
//
//}
//class UniformMagneticField(val B : Vector3D) : MagneticField() {
//    override fun getField(coord: Vector3D, time: Double): Vector3D = B
//
//}
//
//
//
