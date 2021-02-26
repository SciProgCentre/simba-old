package ru.mipt.npm.hdf.disk

import java.nio.ByteBuffer
import java.nio.channels.SeekableByteChannel


fun undefinedAddress(size: Int) = when(size){
    1 -> UByte.MAX_VALUE.toULong()
    2 -> UShort.MAX_VALUE.toULong()
    4 -> UInt.MAX_VALUE.toULong()
    8 -> ULong.MAX_VALUE
    else -> error("Bad size of field") // TODO(Custom size reader)
}

fun interface FieldReader{
    fun read(buffer: ByteBuffer) : Long
    companion object{
        fun get(size: Int) = when(size){
            1 -> FieldReader { it.get().toLong() }
            2 -> FieldReader { it.short.toLong() }
            4 -> FieldReader { it.int.toLong() }
            8 -> FieldReader { it.long }
            else -> error("Bad size of field") // TODO(Custom size reader)
        }

        fun get(size: UByte) = get(size.toInt())
    }
}

class FieldSize(size: UByte){
    val size: Int = size.toInt()
    val reader: FieldReader = FieldReader.get(size)
    val undefinedAddress = undefinedAddress(size.toInt())
}

/**
 * @property  offset This value contains the number of bytes used to store addresses in the file. The values for the addresses of objects in the file are offsets relative to a base address, usually the address of the superblock signature. This allows a wrapper to be added after the file is created without invalidating the internal offset locations.
 * @property  length This value contains the number of bytes used to store the size of an object.
 */
class Sizes(offset: UByte, length: UByte){
    val offset = FieldSize(offset)
    val length = FieldSize(length)
}

interface Reader<T>{
    fun read(source: SeekableByteChannel, sizes: Sizes) : T
}