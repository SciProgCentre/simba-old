package ru.mipt.npm.hdf.disk.infrastructure

import ru.mipt.npm.hdf.*
import ru.mipt.npm.hdf.disk.FieldReader
import ru.mipt.npm.hdf.disk.Reader
import ru.mipt.npm.hdf.disk.Sizes
import java.nio.ByteBuffer
import java.nio.channels.SeekableByteChannel

enum class NodeType{
    GROUP,
    DATA
}

fun NodeTypeFromUByte(i : UByte) = when(i.toUInt()){
    0u -> NodeType.GROUP
    1u -> NodeType.DATA
    else -> error("Unknown node type")
}

class BTreeNodes(
    val sizes: Sizes,
    val nodeType: NodeType,
    val nodeLevel: UByte,
    val numberEntries: UShort,
    val addressLeft : ULong,
    val addressRight: ULong,
    val childStartAddress: ULong
) {

    fun left(source: SeekableByteChannel) = if (addressLeft != sizes.offset.undefinedAddress){
        source.position(addressLeft.toLong())
        BTreeNodes.read(source, sizes)
    } else null

    fun right(source: SeekableByteChannel) = if (addressRight != sizes.offset.undefinedAddress){
        source.position(addressRight.toLong())
        BTreeNodes.read(source, sizes)
    } else null

    data class GroupChild(val key: ULong, val point: ULong)

    fun groupChildren(source: SeekableByteChannel) : List<GroupChild>?{
        if (nodeType == NodeType.GROUP){
            // N+1 Keys and N child pointer
            val size = (numberEntries+1u).toInt()*sizes.length.size + numberEntries.toInt()*sizes.offset.size
            val buff = buffer(size)
            sizes.length.reader.read(buff) // That means that key[0] for group trees is sometimes unused
            return List(numberEntries.toInt()){
                GroupChild(
                    sizes.length.reader.read(buff).toULong(),
                    sizes.offset.reader.read(buff).toULong()
                )
            }

        }
        else return null
    }


    companion object : Reader<BTreeNodes>{

        fun size(sizes: Sizes) = 8 + 2 * sizes.offset.size

        override fun read(source: SeekableByteChannel, sizes: Sizes): BTreeNodes {
            val buff = buffer(size(sizes))
            source.readAndFlip(buff)
            require(buff.ascii(4) == "TREE")
            return BTreeNodes(
                sizes,
                NodeTypeFromUByte(buff.getUByte()),
                buff.getUByte(),
                buff.getUShort(),
                sizes.offset.reader.read(buff).toULong(),
                sizes.offset.reader.read(buff).toULong(),
                source.position().toULong()
            )
        }
    }
}
