import csv
import os
from dataclasses import dataclass

from pathlib import Path
from typing import List
import numpy as np
import json

@dataclass
class Isotope:
    atomic_number : int
    symbol : str
    mass_number :  int
    relative_atomic_mass: float
    isotopic_composition : float = None
    # standard_atomic_weight =isotopes_info[5].split()[-1]
    # notes = isotopes_info[6].split()[-1]

def parse_info(isotopes_info):
    atomic_number = int(isotopes_info[0].split()[-1])
    symbol = isotopes_info[1].split()[-1]
    mass_number =  int(isotopes_info[2].split()[-1])
    relative_atomic_mass = isotopes_info[3].split()[-1]


    relative_atomic_mass = float(relative_atomic_mass.split("(")[0])

    isotopic_composition = isotopes_info[4].split("=")
    if len(isotopic_composition) == 2 and isotopic_composition[-1] != "\n":
        isotopic_composition = float(isotopic_composition[-1].split("(")[0])
    else:
        isotopic_composition = None

    standard_atomic_weight =isotopes_info[5].split()[-1]
    notes = isotopes_info[6].split()[-1]
    return Isotope(atomic_number, symbol, mass_number, relative_atomic_mass, isotopic_composition)

def write_isotope_table(path, isotopes : List[Isotope]):
    with open(path,"w", newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(["Symbol", "Z", "A", "Relative atomic mass"])
        for item in isotopes:
            writer.writerow([item.symbol, item.atomic_number, item.mass_number, item.relative_atomic_mass])
    return True

def write_elements_json(path, isotopes : List[Isotope]):
    z_list = np.unique([i.atomic_number for i in isotopes])
    result = []
    for z in z_list:
        temp = []
        for i in isotopes:
            if i.atomic_number == z:
                temp.append(i)
        data = get_element_dict(temp)
        if isinstance(data, dict):
            result.append(data)
        else:
            for d in data:
                result.append(d)
    with open(path, "w") as fout:
        json.dump(result, fout, indent=4)
    return 0


def get_element_dict(isotopes):
    result = {}

    isotope = isotopes[0]
    result["name"] = "NIST_"+isotope.symbol
    result["Z"] = isotope.atomic_number

    composition = []
    for isotope in isotopes:
        if isotope.isotopic_composition is not None:
            composition.append({
                "A" : isotope.mass_number,
                "massFraction" : isotope.isotopic_composition
            })
    if len(composition) > 0:
        result["composition"] = composition
        return result
    else:
        result = []
        for isotope in isotopes:
            result.append({
                "name" : "NIST_" + isotope.symbol + "_" + str(isotope.mass_number),
                "Z" : isotope.atomic_number,
                "composition" : [
                    {
                        "A" : isotope.mass_number,
                        "massFraction" : 1.0
                    }
                ]
            })
        return result

def main():

    path = os.path.join("data_src", "src", "NIST", "AtomicWeightsAndIsotopicCompositionsForAllElements.txt")

    isotopes = []
    with open(path) as fin:
        while True:
            try:
                isotopes_info = [fin.readline() for i in range(7)]
                fin.readline()
                isotopes.append(parse_info(isotopes_info))
            except Exception:
                break

    # write_isotope_table(Path("data_src/materials/isotopes/NIST_isotopes.data.csv"), isotopes)
    write_elements_json("data_src/materials/elements/NIST_elements.data.json", isotopes)
    return 0

if __name__ == '__main__':
    main()
