package ru.mipt.npm.hdf.disk.data

import ru.mipt.npm.hdf.*
import ru.mipt.npm.hdf.disk.Reader
import ru.mipt.npm.hdf.disk.Sizes
import java.nio.ByteBuffer
import java.nio.channels.SeekableByteChannel

class ObjectHeader(
    val version: UByte,
    val numberHeaderMessages: UShort,
    val referenceCount: UInt,
    val headerSize: UInt,
    val messages: List<HeaderMessage> = emptyList()
) {

    data class HeaderMessage(val type: UShort, val size: UShort,
                             val flag: UByte, val data: ByteBuffer? =  null
    )


    companion object: Reader<ObjectHeader> {
        override fun read(source: SeekableByteChannel, sizes: Sizes): ObjectHeader{
            val buff = buffer(16)
            source.readAndFlip(buff)
            val version = buff.getUByte()
            val number = buff.run{
                get()
                getUShort()
            }
            val count = buff.getUInt()
            val size  = buff.getUInt()
            val messageBuff = buffer(size.toInt())
            source.readAndFlip(messageBuff)
            return ObjectHeader(
                version,
                number,
                count,
                size,
                List(number.toInt()){
                    val type = messageBuff.getUShort()
                    val size = messageBuff.getUShort()
                    val flags = messageBuff.getUByte()
                    messageBuff.skip(3)
                    val buff : ByteBuffer?
                    if (size.toInt() != 0) {
                        buff = buffer(size.toInt())
                        source.readAndFlip(buff)
                    } else buff = null
                    HeaderMessage(type, size, flags, buff)
                }
            )

        }
    }
}