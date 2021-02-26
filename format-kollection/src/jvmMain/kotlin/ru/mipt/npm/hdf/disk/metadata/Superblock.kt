package ru.mipt.npm.hdf

import ru.mipt.npm.hdf.disk.FieldReader
import ru.mipt.npm.hdf.disk.Sizes
import java.nio.channels.SeekableByteChannel


class SuperBlockType : HDFBlock()

/**
 * The superblock is composed of the format signature, followed by a superblock version number and information that is specific to each version of the superblock.
 * @property  version
 * @property  rootGroupSymbolTableEntryVersion This value is used to determine the format of the information in the Root Group Symbol Table Entry.
 * @property groupLeafNodeK  Each leaf node of a group B-tree will have at least this many entries but not more than twice this many. If a group has a single leaf node then it may have fewer entries. This value must be greater than zero. See the description of B-trees below.
 */
class Superblock(
    val version: UByte,
    val sizes: Sizes,
    val baseAddress: ULong,
    val endFileAddress: ULong,
    val fileFreeSpaceStorageVersion: UByte? = null,
    val rootGroupSymbolTableEntryVersion: UByte? = null,
    val sharedHeaderMessageFormatVersion: UByte? = null,
    val fileConsistencyFlag: UByte? = null,
    val fileFreeSpaceInfoAddres: ULong? = null,
    val driverInformationAddress: ULong? = null,
    val extensionAddress: ULong? = null,
    val rootGroupObjectHeaderAddress: ULong? = null,
    val checksum: ULong? = null,
    val groupLeafNodeK: UShort? = null,
    val groupInternalNodeK: UShort? = null,
    val indexedStorageInternalNodeK: UShort? = null,
    val rootGroupSymbolTableEntry: SymbolTableEntry? = null,
) {

    companion object {

        fun superblockSize(version: UInt, sizes: Sizes) = when {
            version == 0u ->  HDFFile.HDF_HEADER_SIZE + 16 + 4 * sizes.offset.size
            version == 1u -> HDFFile.HDF_HEADER_SIZE + 20 + 4 * sizes.offset.size
            version == 2u || version == 3u -> HDFFile.HDF_HEADER_SIZE + 4 + 4 * sizes.offset.size + 4
            else -> error("Unknown version of superblock")
        }

        fun read(source: SeekableByteChannel): Superblock {
            val byteReader = UByteReader()
            val fileStart = source.position()
            logger.debug { "HDF file start: $fileStart" }
            source.position(fileStart + HDFFile.HDF_HEADER_SIZE)
            val superblockVersion = byteReader.read(source).toUInt()
            logger.debug { "Superblock version: $superblockVersion" }
            val sizeOfOffset: UByte
            val sizeOfLengths: UByte
            if (superblockVersion == 0u || superblockVersion == 1u) {
                source.position(fileStart + HDFFile.HDF_HEADER_SIZE + 5L)
                sizeOfOffset = byteReader.read(source)
                sizeOfLengths = byteReader.read(source)
            } else if (superblockVersion == 2u || superblockVersion == 3u) {
                sizeOfOffset = byteReader.read(source)
                sizeOfLengths = byteReader.read(source)
            } else error("Unknown version of superblock")
            logger.debug { "Size of offset: $sizeOfOffset" }
            logger.debug { "Size of lengths: $sizeOfLengths" }
            source.position(fileStart)
            val sizes = Sizes(sizeOfOffset, sizeOfLengths)
            val sbBuff = buffer(superblockSize(superblockVersion,sizes).toInt())
            source.readAndFlip(sbBuff)
            val reader = sizes.offset.reader
            if (superblockVersion == 0u || superblockVersion == 1u) {
                sbBuff.position(HDFFile.HDF_HEADER_SIZE.toInt() + 1)
                val fileFreeSpaceVersion = sbBuff.getUByte()
                val rootGroupVersion = sbBuff.getUByte()
                logger.debug { "Version of Root Group Symbol Table Entry: $rootGroupVersion" }
                sbBuff.get() // Reserved (zero)
                val headerFormatVersion = sbBuff.getUByte()
                sbBuff.get()
                sbBuff.get()
                sbBuff.get() // Reserved (zero)
                val groupLeafNodeK = sbBuff.getUShort()
                val groupInternalNodeK = sbBuff.getUShort()
                sbBuff.getInt() // This field is unused and should be ignored.
                val indexedStorageInternalNodeK: UShort? = if (superblockVersion == 1u) sbBuff.run {
                    val temp = getUShort()
                    getShort() // Reserved (zero)
                    temp
                } else null

                val baseAddress = reader.read(sbBuff).toULong()
                require(fileStart.toULong() == baseAddress){"Adress of file start non consistent"}

                val freeSpaceAddress = reader.read(sbBuff).toULong()
                logger.debug { "Free space: $freeSpaceAddress" }
                val endOfFile = reader.read(sbBuff).toULong()
                logger.debug { "End of file: $endOfFile" }
                val driverInfoAdress = reader.read(sbBuff).toULong()
                logger.info { "Driver Information Block Address: ${driverInfoAdress}" }
                logger.debug { "Superblock end: ${source.position()}" }
                return Superblock(
                    version = superblockVersion.toUByte(),
                    sizes = sizes,
                    baseAddress = baseAddress,
                    endFileAddress = endOfFile,
                    fileFreeSpaceStorageVersion = fileFreeSpaceVersion,
                    rootGroupSymbolTableEntryVersion = rootGroupVersion,
                    sharedHeaderMessageFormatVersion = headerFormatVersion,
                    groupLeafNodeK = groupLeafNodeK,
                    groupInternalNodeK = groupInternalNodeK,
                    indexedStorageInternalNodeK = indexedStorageInternalNodeK,
                    fileFreeSpaceInfoAddres = freeSpaceAddress,
                    driverInformationAddress = driverInfoAdress,
                    rootGroupSymbolTableEntry = SymbolTableEntry.read(source, sizes)
                )
            } else if (superblockVersion == 2u || superblockVersion == 3u) {
                sbBuff.position(HDFFile.HDF_HEADER_SIZE.toInt() + 3)
                val fileConsistencyFlag = sbBuff.getUByte()
                val baseAddress = reader.read(sbBuff).toULong()
                require(fileStart.toULong() == baseAddress){"Adress of file start non consistent"}
                val sbExtAddress = reader.read(sbBuff).toULong()
                val endOfFile = reader.read(sbBuff).toULong()
                logger.debug { "End of file: $endOfFile" }
                val rootGroupObjectHeaderAddress = reader.read(sbBuff).toULong()
                val checksum = sbBuff.getULong() //TODO(Check hash of superblock)
                return Superblock(
                    version = superblockVersion.toUByte(),
                    sizes = sizes,
                    baseAddress = baseAddress,
                    endFileAddress = endOfFile,
                    extensionAddress = sbExtAddress,
                    rootGroupObjectHeaderAddress = rootGroupObjectHeaderAddress
                )
            } else error("Unknown version of superblock")
        }
    }
}