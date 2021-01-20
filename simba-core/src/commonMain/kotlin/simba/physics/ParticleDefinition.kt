package simba.physics

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
    //    val pType: String
    val lepton: Int
    val baryon: Int

    val stable: Boolean
    val lifetime: Double
    //    val decaytable: Any?
    val shortlived: Boolean
//    val subType: String

    val magneticMoment: Double
}

interface PDGID{
    val encoding: Int
    val anti_encoding: Int
}

object NeutralFake: ParticleDefinition {
    override val name: String = "NEUTRAL_FAKE"
    override val mass: Double = 0.0
    override val width: Double  = 0.0
    override val charge: Double  = 0.0
    override val iParity: Int  = 0
    override val iConjugation: Int   = 0
    override val iIsospin: Int = 0
    override val iIsospinZ: Int = 0
    override val gParity: Int = 0
    override val lepton: Int = 0
    override val baryon: Int = 0
    override val stable: Boolean = true
    override val lifetime: Double  = 0.0
    override val shortlived: Boolean = false
    override val magneticMoment: Double  = 0.0
}

abstract class AbstractElectron : ParticleDefinition, PDGID{
    override val encoding: Int = 11
    override val anti_encoding: Int = -11
}
abstract class AbstractPositron : ParticleDefinition, PDGID{
    override val encoding: Int = -11
    override val anti_encoding: Int = 11
}
abstract class AbstractGamma : ParticleDefinition, PDGID{
    override val encoding: Int = 22
    override val anti_encoding: Int = 22
}
abstract class AbstractProton : ParticleDefinition, PDGID


class UnitsDefinition(val units: UnitsSpace, val consts : ConstsSpace){


    val ELECTRON  = object : AbstractElectron() {
        override val name: String = "electron"
        override val mass: Double = consts.electron_mass_c2
        override val width: Double = 0.0 * units.MeV
        override val charge: Double = consts.electron_charge
        override val iParity: Int = 1
        override val iConjugation: Int = 0
        override val iIsospin: Int = 0
        override val iIsospinZ: Int = 0
        override val gParity: Int = 0
        //    override val pType: String
        override val lepton: Int = 1
        override val baryon: Int = 0
        override val stable: Boolean = true
        override val lifetime: Double = -1.0
        //    override val decaytable: Any?
        override val shortlived: Boolean = false
        //    override val subType: String
        override val magneticMoment: Double = -consts.muB * 1.00115965218076
    }

    val POSITRON =  object : AbstractPositron(){
        override val name: String = "positron"
        override val mass: Double = consts.electron_mass_c2
        override val width: Double = 0.0 * units.MeV
        override val charge: Double = -consts.electron_charge
        override val iParity: Int =1
        override val iConjugation: Int =0
        override val iIsospin: Int =0
        override val iIsospinZ: Int =0
        override val gParity: Int = 0
        override val lepton: Int = -1
        override val baryon: Int = 0
        override val stable: Boolean = true
        override val lifetime: Double = -1.0
        override val shortlived: Boolean = false
        override val magneticMoment: Double = consts.muB * 1.00115965218076

    }
}
