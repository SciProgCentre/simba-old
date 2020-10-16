import json


COMPAUND_PATH  = ""
SIMPLE_PATH = ""

def compaund(path, output):
    with open(path) as fin:
        fin.readlines(5)
        result = []
        while True:
            try:
                line = fin.readline().split()
                n = int(line[0])
                name = "NIST" + line[1][2:]
                temp = []
                for i in range(n):
                    line = fin.readline().split()
                    temp.append({
                        "Z" : int(line[0]),
                        "massFraction" : float(line[1])
                    })
                result.append({
                    "name" : name,
                    "composition" : temp
                })
            except Exception:
                break
    with open(output, "w") as fout:
        json.dump(result, fout, indent=4)
    return 0

def simple(path, output):
    with open(path) as fin:
        fin.readlines(6)
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
        except Exception:
            break
    with open(output, "w") as fout:
        json.dump(result, fout, indent=4)
    return 0


def main():

    return 0

if __name__ == '__main__':
    main()
