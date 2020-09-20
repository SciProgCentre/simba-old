package ru.mipt.npm.mcengine.geant4.gdml

import javafx.concurrent.Worker
import ru.mipt.npm.mcengine.utils.meter
import ru.mipt.npm.mcengine.utils.ps

class A(var a : Int)

//fun inc(a: A){
//    a.apply {
//        this.a++
//    }
//}


fun main(args: Array<String>) {
//        val a = A(1)
//        inc(a)
//        print(a.a)

    val doc = GDMLDocument().apply {
        val boxWorld = Box("BoxWorld", 10.0, 10.0, 10.0)
        val box = Box("Box", 1.0, 1.0, 1.0)
        val material = Material("G4_AIR")
        val boxVol = volume("Box", material, box)


        volume(WORLD_DEFAULT_NAME, material, boxWorld) {
            physvol( boxVol)

            auxiliary("det", "score")
            }

        volume(WORLD_DEFAULT_NAME){
            physvol{
                volume("Box1", Material("G4_Pb"), Box("Box1", 1.0, 1.0, 1.0)){
                    physvol(boxVol)
                    auxiliary("ElectricField", "Uniform"){
                        auxiliary("Ex", 0.0)
                        auxiliary("Ey", 0.0)
                        auxiliary("Ez", 1e-4)

                    }
                }
            }
        }
        }

//    val doc = GDMLDocument().buildGeant {
//        val boxWorld = Box("BoxWorld", 150.0, 150.0, 150.0)
//        val box = Box("Box", 2.0, 2.0, 2.0)
//        val material = Material("G4_AIR")
//        val boxVol = volume("BoxVol", material, box)
//
//
//        volume(WORLD_DEFAULT_NAME, material, boxWorld) {
//
//            for (i in -15..14) {
//                for (j in -15..14) {
//                    physvol("BoxPhys_${i}_$j", boxVol, position("Pos_${i}_$j", i.toDouble(), j.toDouble()))
//                }
//            }
//
//
//        }
//    }

    println(doc)
//    doc.saveGDML("kotlin.gdml")



}

