package ru.mipt.npm.hdf

import ru.mipt.npm.hdf.disk.FieldReader
import ru.mipt.npm.hdf.disk.Reader
import ru.mipt.npm.hdf.disk.Sizes
import java.nio.ByteBuffer
import java.nio.channels.SeekableByteChannel

/**
 * The cache type is determined from the object header. It also determines the format for the scratch-pad space
 */
enum class CacheType{
    /**
     *  0 - No data is cached by the group entry. This is guaranteed to be the case when an object header has a link count greater than one.
     */
    NONE,

    /**
     * 1 - Group object header metadata is cached in the scratch-pad space. This implies that the symbol table entry refers to another group.
     */
    METADATA,

    /**
     *  2 -  The entry is a symbolic link. The first four bytes of the scratch-pad space are the offset into the local heap for the link value. The object header address will be undefined.
     */
    SYMBOLIC_LINK
}

fun CacheTypeFromInt(i : Int) = when(i.toUInt()){
    0u -> CacheType.NONE
    1u -> CacheType.METADATA
    2u -> CacheType.SYMBOLIC_LINK
    else -> CacheType.NONE
}

/**
 * @property cacheSpace This space is used for different purposes, depending on the value of the Cache Type field. Any metadata about an object represented in the scratch-pad space is duplicated in the object header for that object. Furthermore, no data is cached in the group entry scratch-pad space if the object header for the object has a link count greater than one.
 */
class SymbolTableEntry(
    val linkNameOffset : ULong,
    val objectHeaderAddress: ULong,
    val cacheType : Any? = null,
    val cacheSpace : ByteBuffer,
    val sizes: Sizes,
) {

    /**
     * @property bTreeAddress This is the file address for the root of the group’s B-tree.
     * @property nameHeapAddress This is the file address for the group’s local heap, in which are stored the group’s symbol names.
     */
    data class ObjectHeaderCache(val bTreeAddress: ULong, val nameHeapAddress: ULong)

    val objectHeaderCache = if (cacheType == CacheType.METADATA) cacheSpace.run {
        val reader = sizes.offset.reader
        ObjectHeaderCache(reader.read(this).toULong(), reader.read(this).toULong())
    } else null

    /**
     * The value of a symbolic link (that is, the name of the thing to which it points) is stored in the local heap. This field is the 4-byte offset into the local heap for the start of the link value, which is null terminated.
     */
    val symbolLink = if (cacheType == CacheType.SYMBOLIC_LINK) cacheSpace.getUInt() else null

    companion object : Reader<SymbolTableEntry>{
        fun size(sizes: Sizes) = 2*sizes.offset.size+4+4+16

        override fun read(source: SeekableByteChannel, sizes: Sizes) : SymbolTableEntry{
            val buff = buffer(size(sizes))
            source.readAndFlip(buff)
            return SymbolTableEntry(
                sizes.offset.reader.read(buff).toULong(),
                sizes.offset.reader.read(buff).toULong(),
                CacheTypeFromInt(buff.int),
                buff.run{
                    int
                    slice().order(order())
                },
                sizes
            )
        }
    }
}