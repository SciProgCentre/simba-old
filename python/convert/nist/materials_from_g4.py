import json


COMPAUND_PATH  = "/home/zelenyy/npm/simulations/mc-engine-kotlin/mcengine-geant4/src/main/resources/data/G4CompaundMaterials.txt"
SIMPLE_PATH = "/home/zelenyy/npm/simulations/mc-engine-kotlin/mcengine-geant4/src/main/resources/data/G4SimpleMaterials.txt"

def compaund(path, output):
    with open(path) as fin:
        for i in range(5):
            fin.readline()
        result = []
        while True:
            try:
                line = fin.readline().split()
                n = int(line[0])
                name = "NIST" + line[1][2:]
                temp = []

                # is_mass_fraction = False
                for i in range(n):
                    line = fin.readline().split()
                    Z = int(line[0])
                    try:
                        number = int(line[1])
                        temp.append({
                            "Z" : int(line[0]),
                            "numberOfAtom" : number
                        })
                    except Exception:
                        massFraction = float(line[1])
                        temp.append({
                            "Z" : int(line[0]),
                            "massFraction" : massFraction
                        })
                result.append({
                    "name" : name,
                    "composition" : temp
                })
                print(name)
            except Exception:
                break
    with open(output, "w") as fout:
        json.dump(result, fout, indent=4)
    return 0

def simple(path, output):
    with open(path) as fin:
        for i in range(6):
            fin.readline()
        result = []
        while True:
            try:
                line = fin.readline().split()
                Z = int(line[0]) - 1
                name = "NIST" + line[1][2:]
                result.append({
                    "name" : name,
                    "composition" : [
                        {
                            "Z" : Z,
                            "massFraction" : 1.0
                        }
                    ]
                })
                print(name)
            except Exception:
                break
    with open(output, "w") as fout:
        json.dump(result, fout, indent=4)
    return 0


def main():
    # simple(SIMPLE_PATH, "data_src/materials/materials/NIST_simple.data.json")
    compaund(COMPAUND_PATH, "data_src/materials/materials/NIST_compaund.data.json")
    return 0

if __name__ == '__main__':
    main()
