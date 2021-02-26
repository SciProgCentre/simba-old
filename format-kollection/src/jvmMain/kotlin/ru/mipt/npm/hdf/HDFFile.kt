package ru.mipt.npm.hdf

import mu.KotlinLogging
import ru.mipt.npm.abt.*
import ru.mipt.npm.hdf.disk.infrastructure.LocalHeap
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.SeekableByteChannel
import java.nio.file.Path

open class HDFBlock: BlockType

interface HDFReader<T>: Reader<T, HDFBlock>

interface Layout

val logger = KotlinLogging.logger {  }



class HDFParser: FileParser<HDFBlock> {

    override fun buildTree(source: SeekableByteChannel): ABTNode<HDFBlock> {
        val superblock = Superblock.read(source)

       return LeafNode<HDFBlock>(HDFBlock(), 0, 0)
    }


companion object{}
}


interface HDF{
    val superblock: Superblock
}

class HDFChannel(
    private val channel: SeekableByteChannel,
    override val superblock: Superblock
) : HDF

class HDFFile(private val hdfChannel: HDFChannel) : HDF by hdfChannel {

    companion object{

        val HDF_HEADER = ubyteArrayOf(137u, 72u, 68u, 70u, 13u, 10u, 26u, 10u)
        val HDF_HEADER_SIZE = 8L

        fun checkHeader(source: SeekableByteChannel) : Boolean{
            val header = ByteBuffer.allocate(HDF_HEADER_SIZE.toInt())
            source.read(header)
            return header.array().toUByteArray().contentEquals(HDF_HEADER)
        }

        /**
         * The superblock may begin at certain predefined offsets within the HDF5 file, allowing a block of unspecified content for users to place additional information at the beginning (and end) of the HDF5 file without limiting the HDF5 Library’s ability to manage the objects within the file itself. This feature was designed to accommodate wrapping an HDF5 file in another file format or adding descriptive information to an HDF5 file without requiring the modification of the actual file’s information. The superblock is located by searching for the HDF5 format signature at byte offset 0, byte offset 512, and at successive locations in the file, each a multiple of two of the previous location; in other words, at these byte offsets: 0, 512, 1024, 2048, and so on.
         */
        fun findStartPosition(source: SeekableByteChannel) : Long{
            val position = source.position()
            val START_OFFSET = 512L
            var offset = 0L
            while (position + offset < source.size()) {
                source.position(position + offset)
                if (checkHeader(source)){
                    source.position(position)
                    return position + offset
                }
                offset = if (position == 0L) START_OFFSET else 2*offset
            }
            error("Cann't open file as HDF file. Bad signature")
        }

        fun parse(source: SeekableByteChannel) : HDFFile{
            logger.debug { "Source size: ${source.size()}" }
            val position = findStartPosition(source)
            logger.debug { "Find HDF file in $position position" }
            source.position(position)
//            HDFParser().buildTree(source)
            val superblock = Superblock.read(source)
            val address = superblock.rootGroupSymbolTableEntry?.objectHeaderCache
            if (address != null){
                println(address.nameHeapAddress)
                source.position(address.nameHeapAddress.toLong())
                val heap = LocalHeap.read(source, superblock.sizes)
                println(heap.addressDataSegment)
                println(heap.dataSegmentSize)
                println(heap.data(source))
            }
            return HDFFile(HDFChannel(source,superblock))
        }

        fun open(file: File) = parse(RandomAccessFile(file, "r").channel)

        fun open(path: Path) = open(path.toFile())

        fun open(name: String) = parse(RandomAccessFile(name, "r").channel)
    }
}