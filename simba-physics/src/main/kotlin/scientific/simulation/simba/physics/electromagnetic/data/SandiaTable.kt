package ru.mipt.npm.mcengine.geant4.physics.electromagnetic.data


import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.data.SandiaTableData.cumulInterval
import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.data.SandiaTableData.getSandiaCofPerAtom
import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.data.SandiaTableData.ionizationPotentials
import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.data.SandiaTableData.nbOfIntervals
import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.data.SandiaTableData.sandiaTable
import ru.mipt.npm.mcengine.geant4.physics.electromagnetic.vecNbOfAtomsPerVolume
import ru.mipt.npm.mcengine.material.Material
import ru.mipt.npm.mcengine.utils.eV
import ru.mipt.npm.mcengine.utils.keV
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class SandiaTable(val material: Material) {
//TODO(Привести таблицу в человеческий вид)

    val matSandiaMatrix: List<List<Double>>

    init {
        //get list of elements
        val elements = material.elements
        var NbElm = elements.size
        var Z = Array<Int>(NbElm, { 0 })               //Atomic number

        //determine the maximum number of energy-intervals for this material
        var MaxIntervals = 0;
//        var elm : Int
        var z: Int

        // here we compute only for a mixture, so no waring or exception
        // if z is out of validity interval
        for (elm in 0..NbElm - 1) {
            z = elements[elm].Z
            if (z < 1) {
                z = 1; } else if (z > 100) {
                z = 100; }
            Z[elm] = z;
            MaxIntervals += nbOfIntervals[z];
        }
        //copy the Energy bins in a tmp1 array
        //(take care of the Ionization Potential of each element)
        var tmp1 = Array(MaxIntervals, { 0.0 })
        var IonizationPot: Double
        var interval1 = 0

        for (elm in 0..NbElm - 1) {
            z = Z[elm];
            IonizationPot = ionizationPotentials[z] * eV;
            for (row in cumulInterval[z - 1]..cumulInterval[z] - 1) {
                tmp1[interval1] = max(sandiaTable[row][0] * keV,
                        IonizationPot);
                ++interval1;
            }
        }
        //sort the energies in strickly increasing values in a tmp2 array
        //(eliminate redondances)

        var tmp2 = Array(MaxIntervals, { 0.0 })
        var Emin: Double
        var interval2 = 0

        do {
            Emin = Double.MAX_VALUE;

            for (i1 in 0..MaxIntervals - 1) {
                Emin = min(Emin, tmp1[i1]);               //find the minimum
            }
            if (Emin < Double.MAX_VALUE) {
                tmp2[interval2] = Emin;
                ++interval2;
            }
            //copy Emin in tmp2
            for (i1 in 0..MaxIntervals - 1) {
                if (tmp1[i1] <= Emin) {
                    tmp1[i1] = Double.MAX_VALUE; }   //eliminate from tmp1
            }
            // Loop checking, 07-Aug-2015, Vladimir Ivanchenko
        } while (Emin < Double.MAX_VALUE);

        //create the sandia matrix for this material

        var fMatSandiaMatrix = Array(interval2, { Array(5, { 0.0 }) })
        var interval: Int;

        //ready to compute the Sandia coefs for the material

        val NbOfAtomsPerVolume = material.vecNbOfAtomsPerVolume

        val prec = 1.0e-03 * eV;
        var coef: Double
        var oldsum = 0.0
        var newsum = 0.0
        var fMatNbOfIntervals = 0; // TODO {to field}

        for (interval in 0..interval2 - 1) {
            Emin = tmp2[interval]
            fMatSandiaMatrix[fMatNbOfIntervals][0] = tmp2[interval]
            for (k in 1..4) {
                fMatSandiaMatrix[fMatNbOfIntervals][k] = 0.0;
            }
            for (elm in 0..NbElm - 1) {
                val fSandiaCofPerAtom = getSandiaCofPerAtom(Z[elm], Emin + prec);

                for (j in 1..4) {
                    coef = NbOfAtomsPerVolume[elm] * fSandiaCofPerAtom[j - 1];
                    fMatSandiaMatrix[fMatNbOfIntervals][j] += coef;
                    newsum += abs(coef);
                }
            }
            //check for null or redondant intervals

            if (newsum != oldsum) {
                oldsum = newsum;
                ++fMatNbOfIntervals;
            }
        }
        this.matSandiaMatrix = fMatSandiaMatrix.map { it.toList() }
    }


    fun getSandiaCofForMaterial(energy: Double, density : Double): List<Double> {
        var interval = 0;
        if (energy > matSandiaMatrix[0][0]) {
//            interval = fMatNbOfIntervals - 1;
            interval = matSandiaMatrix.size - 1
            // Loop checking, 07-Aug-2015, Vladimir Ivanchenko
            while ((interval > 0) && (energy < matSandiaMatrix[interval][0])) {
                --interval
            }
        }
        return matSandiaMatrix[interval].slice(1..4).map { it*density } //Мнй добавлдное умнжение на плотность

    }
}


