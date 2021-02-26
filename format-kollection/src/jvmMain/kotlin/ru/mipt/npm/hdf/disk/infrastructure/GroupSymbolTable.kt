package ru.mipt.npm.hdf.disk.infrastructure

import ru.mipt.npm.hdf.*
import ru.mipt.npm.hdf.disk.Reader
import ru.mipt.npm.hdf.disk.Sizes
import java.nio.channels.SeekableByteChannel


class GroupSymbolTable(
    val sizes: Sizes,
    val version: UByte,
    val size: UInt,
    val tablePosition: Long,
) : HDFBlock(){

    private val entrySize = SymbolTableEntry.size(sizes)

    private fun getPosition(indx: UInt) = tablePosition + entrySize*indx.toLong()

    fun entry(source: SeekableByteChannel, indx: UInt) : SymbolTableEntry {
        require(indx < size){"Out of boundary of symbol table"}
        source.position(getPosition(indx))
        return SymbolTableEntry.read(source, sizes)
    }

    fun entries(source: SeekableByteChannel, start: UInt = 0u, stop: UInt = UInt.MAX_VALUE) = sequence{
        require(start < size){"Out of boundary of symbol table"}
        val _stop = if (stop > size) size else stop
        source.position(tablePosition)
        for (i in start until _stop){
            yield(SymbolTableEntry.read(source, sizes))
        }

    }

    companion object : Reader<GroupSymbolTable>{
        override fun read(source: SeekableByteChannel, sizes: Sizes) : GroupSymbolTable {
            val buff = buffer(8)
            source.readAndFlip(buff)
            require(buff.ascii(4) == "SNOD"){"Bad symbol table signature"}
            return GroupSymbolTable(
                sizes,
                buff.getUByte(),
                buff.run {
                    get()
                    getUInt()
                },
                source.position()
            )
        }
    }
}