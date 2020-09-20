package ru.mipt.npm.mcengine.geant4.physics.electromagnetic

import ru.mipt.npm.mcengine.Settings
import ru.mipt.npm.mcengine.utils.TeV
import ru.mipt.npm.mcengine.utils.keV

//enum class NuclearFormfactorType {
//    fNoneNF , // = 0
//    fExponentialNF,
//    fGaussianNF,
//    fFlatNF
//}
//
//enum class MscStepLimitType {
//    fMinimal , //= 0
//    fUseSafety,
//    fUseSafetyPlus,
//    fUseDistanceToBoundary
//
//}

data class EmParameters  (
//        val lossFluctuation : Boolean = true,
//        val buildCSDARange : Boolean = false,
//        val flagLPM : Boolean = true,
//        val spline : Boolean = true,
//        val cutAsFinalRange : Boolean = false,
//        val applyCuts : Boolean = false,
//        val fluo : Boolean = false,
//        val beardenFluoDir : Boolean = false,
//        val auger : Boolean = false,
//        val augerCascade : Boolean = false,
//        val pixe : Boolean = false,
//        val deexIgnoreCut : Boolean = false,
//        val lateralDisplacement : Boolean = true,
//        val lateralDisplacementAlg96  : Boolean = true,
//        val muhadLateralDisplacement  : Boolean= false,
//        val latDisplacementBeyondSafety  : Boolean= false,
//        val useAngGeneratorForIonisation  : Boolean= false,
//        val useMottCorrection  : Boolean= false,
//        val integral  : Boolean = true,
//        val birks  : Boolean = false,
//        val dnaFast  : Boolean = false,
//        val dnaStationary  : Boolean = false,
//        val dnaMsc  : Boolean = false,
//        val gammaShark : Boolean = false,
//
//        val minSubRange : Double = 1.0,
        val minKinEnergy : Double = 0.1*keV,
        val maxKinEnergy : Double = 100.0*TeV//,
//        val maxKinEnergyCSDA : Double = 1.0*GeV,
//        val lowestElectronEnergy : Double = 1.0*keV,
//        val lowestMuHadEnergy : Double = 1.0*keV,
//        val lowestTripletEnergy : Double = 1.0*MeV,
//        val linLossLimit : Double = 0.01,
//        val bremsTh : Double = maxKinEnergy,
//        val lambdaFactor : Double = 0.8,
//        val factorForAngleLimit : Double = 1.0,
//        val thetaLimit : Double = pi,
//        val rangeFactor : Double = 0.04,
//        val rangeFactorMuHad : Double = 0.2,
//        val geomFactor : Double = 2.5,
//        val skin : Double = 1.0,
//        val dRoverRange : Double = 0.2,
//        val finalRange : Double = mm,
//        val dRoverRangeMuHad : Double = 0.2,
//        val finalRangeMuHad  : Double= 0.1*mm,
//        val factorScreen : Double = 1.0,
//
//        val nbins  : Int = 84,
//        val nbinsPerDecade : Int = 7,
//    val verbose : Int = 1 ,
//    val workerVerbose : Int = 0,

//        val mscStepLimit: MscStepLimitType = MscStepLimitType.fUseSafety,
//        val mscStepLimitMuHad: MscStepLimitType = MscStepLimitType.fMinimal,
//        val nucFormfactor: NuclearFormfactorType = NuclearFormfactorType.fExponentialNF,
//
//        val namePIXE : String = "Empirical",
//        val nameElectronPIXE : String = "Livermore"
    //G4EmSaturation* emSaturation,
    //G4StateManager* fStateManager,
) : Settings