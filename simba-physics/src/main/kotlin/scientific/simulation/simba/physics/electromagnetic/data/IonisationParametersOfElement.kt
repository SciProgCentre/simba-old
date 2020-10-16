//package ru.mipt.npm.mcengine.materials
//
//import ru.mipt.npm.mcengine.materials.table.IonisationParameters.vFermiList
//import ru.mipt.npm.mcengine.materials.table.IonisationParameters.lFactorList
//import ru.mipt.npm.mcengine.utils.electron_mass_c2
//import ru.mipt.npm.mcengine.utils.MeV
//import ru.mipt.npm.mcengine.utils.eV
//import ru.mipt.npm.mcengine.utils.proton_mass_c2
//import ru.mipt.npm.mcengine.utils.twopi_mc2_rcl2
//import java.lang.Math.pow
//import kotlin.math.ln
//import kotlin.math.sqrt
//
//class IonisationParametersOfElement(Z : Double) {
//    init{
//        if (Z < 1 && Z>92){
//            //TODO {exeption}
//            error("Z must be from 1 to 92")
//        }
//    }
//
//
//
//    val Z3 = pow(Z,  1.0/3.0)
//    val ZZ3 = pow(Z*(Z+1),  1.0/3.0)
//    val logZ3 = ln(Z)/3
//    // obsolete parameters for ionisation
//    val tau0 = 0.1*Z3* MeV / proton_mass_c2
//    val taul = 2* MeV / proton_mass_c2
//    val vFermi = vFermiList[Z.toInt() - 1] //TODO{ограниено 92 элементом}
//    val lFactor = lFactorList[Z.toInt() - 1]//TODO{ограниено 92 элементом}
//
//    // compute the Bethe-Bloch formula for energy = fTaul*particle mass
//    val meanExcitationEnergy : Double = 1.0 // TODO {NIST import}
//    val rate = meanExcitationEnergy/ electron_mass_c2
//    val w = taul*(taul + 2)
//    val betheBlochLow = 2.0*Z* twopi_mc2_rcl2 *((taul+1.0)*(taul+1.0)*ln(2.0*w/rate)/w - 1.0)
//
//    // TODO {magic const}
//    val taum = 0.035*Z3* MeV / proton_mass_c2
//    val Clow = sqrt(taul)*betheBlochLow
//    val Alow =  6.458040 * Clow/tau0
//    val Blow = -3.229020*Clow/(tau0*sqrt(taum))
//
//    val rate_ = 0.001*meanExcitationEnergy/ eV
//    var rate_2 =rate_*rate_
//    val shellCorrectionVector = listOf(
//            ( 0.422377   + 3.858019*rate_)*rate_2,
//            ( 0.0304043  - 0.1667989*rate_)*rate_2,
//            (-0.00038106 + 0.00157955*rate_)*rate_2
//    )
//    /* Заккоментированно в GEANT4
//    fShellCorrectionVector[0] = ( 1.10289e5 + 5.14781e8*rate)*rate2 ;
//    fShellCorrectionVector[1] = ( 7.93805e3 - 2.22565e7*rate)*rate2 ;
//    fShellCorrectionVector[2] = (-9.92256e1 + 2.10823e5*rate)*rate2 ;
//    */
//
//
//}