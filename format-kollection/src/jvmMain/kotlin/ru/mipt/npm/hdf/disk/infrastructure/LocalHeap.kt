package ru.mipt.npm.hdf.disk.infrastructure

import ru.mipt.npm.hdf.*
import ru.mipt.npm.hdf.disk.FieldReader
import ru.mipt.npm.hdf.disk.Sizes
import java.nio.ByteBuffer
import java.nio.channels.SeekableByteChannel

val ByteBuffer.stringList: List<String>
    get() {
        val data = mutableListOf<String>()
        val word = mutableListOf<Char>()
        while (this.hasRemaining()){
            val next = this.get()
            if (next.toInt() != 0){
                word.add(next.toChar())
            } else{
                if (word.isNotEmpty()){
                    data.add(word.joinToString(""))
                    word.clear()
                }
            }
        }
        return data
    }

class LocalHeap(
    val version: UByte,
    val dataSegmentSize: ULong,
    val offsetHeadFreeList: ULong,
    val addressDataSegment: ULong
) {

    fun data(source: SeekableByteChannel) : List<String> {
        val buff  = buffer(offsetHeadFreeList.toInt()) //FIXME(Unsafe cast)
        source.position(addressDataSegment.toLong())
        source.readAndFlip(buff)
        return buff.stringList
    }

    companion object{

        fun size(sizes: Sizes) = 8 + 2*sizes.length.size + sizes.offset.size

        fun read(source: SeekableByteChannel, sizes: Sizes) : LocalHeap{
            val buff = buffer(size(sizes))
            source.readAndFlip(buff)
            val temp = buff.ascii(4)
            require( temp == "HEAP"){"Bad local heap signature"}
            val version = buff.getUByte()
            buff.skip(3)
            return LocalHeap(
                version,
                sizes.length.reader.read(buff).toULong(),
                sizes.length.reader.read(buff).toULong(),
                sizes.offset.reader.read(buff).toULong(),
            )
        }
    }
}