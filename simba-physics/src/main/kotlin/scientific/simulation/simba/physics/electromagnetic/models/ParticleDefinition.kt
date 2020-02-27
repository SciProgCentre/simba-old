package scientific.simulation.simba.physics.electromagnetic.models

interface ParticleDefinition{
    val name: String
    val mass: Double
    val width: Double
    val charge: Double
    val iParity: Int
    val iConjugation: Int
    val iIsospin: Int
    val iIsospinZ: Int
    val gParity: Int
    val pType: String
    val lepton: Int
    val baryon: Int
    val encoding: Int
    val stable: Boolean
    val lifetime: Double
    val decaytable: Any?
    val shortlived: Boolean
    val subType: String
    val anti_encoding: Int
    val magneticMoment: Double
}