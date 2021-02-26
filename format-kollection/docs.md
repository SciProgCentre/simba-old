# Module root.kt

# Package ru.mipt.npm.hdf

Documentation based on official [HDF documentation](https://portal.hdfgroup.org/display/HDF5)

# Package ru.mipt.npm.hdf.disk

Disk level.

Values for all fields on this level should be treated as unsigned integers, unless otherwise noted in the description of a field. Additionally, all metadata fields are stored in little-endian byte order. 

All checksums used in the format are computed with the Jenkins’ lookup3 algorithm. 
Whenever a bit flag or field is mentioned for an entry, bits are numbered from the lowest bit position in the entry. 

# Package ru.mipt.npm.hdf.disk.metadata

Contains basic information for identifying and defining information about the file

# Package ru.mipt.npm.hdf.disk.infrastructure

Contains the information about the pieces of a file shared by many objects in the file (such as B-trees and heaps).

# Package ru.mipt.npm.hdf.disk.data

The rest of the file and contains all of the data objects with each object partitioned into header information, also known as metadata, and data.


