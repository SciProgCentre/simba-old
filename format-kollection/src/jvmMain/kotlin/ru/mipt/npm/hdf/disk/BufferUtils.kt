package ru.mipt.npm.hdf


import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.SeekableByteChannel

fun buffer(size: Int, byteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN) = ByteBuffer.allocate(size).order(byteOrder)

fun ByteBuffer.getUByte() = get().toUByte()

fun ByteBuffer.getUShort() = short.toUShort()

fun ByteBuffer.getUInt() = int.toUInt()

fun ByteBuffer.getULong() = long.toULong()

fun ByteBuffer.ascii(size: Int) = Array(size){get().toChar()}.joinToString("")

fun ByteBuffer.skip(n: Int) = this.position(this.position() + n)
class UByteReader(byteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN){
    val buffer: ByteBuffer = buffer(1, byteOrder)
    fun read(source: SeekableByteChannel) = buffer.run {
        clear()
        source.read(buffer)
        flip()
        get().toUByte()
    }
}

class UShortReader(byteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN){
    val buffer: ByteBuffer = buffer(2, byteOrder)
    fun read(source: SeekableByteChannel) = buffer.run {
        clear()
        source.read(buffer)
        flip()
        short.toUShort()
    }
}

class UIntReader(byteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN){
    val buffer: ByteBuffer = buffer(4, byteOrder)
    fun read(source: SeekableByteChannel) = buffer.run {
        clear()
        source.read(buffer)
        flip()
        int.toUInt()
    }
}

class ULongReader(byteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN){
    val buffer: ByteBuffer = buffer(8, byteOrder)
    fun read(source: SeekableByteChannel) = buffer.run {
        clear()
        source.read(buffer)
        flip()
        getLong().toULong()
    }
}

fun SeekableByteChannel.readAndFlip(buffer: ByteBuffer) {
    read(buffer)
    buffer.flip()
}
