import tables
from tables import open_file


def test_1():
    with open_file("test_1.hdf5", "w", title="First test file") as h5file:
        h5file.create_group(h5file.root, "TEST", title="first test group")
        h5file.create_group(h5file.root, "LONGTEST", title="group with 8-byte name")
        h5file.create_group(h5file.root, "VERYLONGTEST", title="group with over 8-byte name")
    return 0



if __name__ == '__main__':
    test_1()

