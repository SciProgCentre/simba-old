package simba.data

import hep.dataforge.context.Context
import hep.dataforge.io.*
import kotlinx.io.Binary
import kotlinx.io.asBinary
import java.nio.file.Path
import java.util.zip.ZipFile

class DefaultFormatPeeker(val context: Context){
    fun peek(binary: Binary): EnvelopeFormat?{
        val formats = context.io.envelopeFormatFactories.mapNotNull { factory ->
            binary.read {
                factory.peekFormat(context.io, this@read)
            }
        }
        return when (formats.size) {
            0 -> null
            1 -> formats.first()
            else -> error("Envelope format binary recognition clash")
        }
    }
}

class ZipEnvelopeLoader(
    val context: Context, val zipFile: ZipFile,
    val formatPeeker: (Binary) -> EnvelopeFormat? = DefaultFormatPeeker(context
    )::peek
) {


    fun isFile(name :String) = !isNode(name)

    fun isNode(name :String)= zipFile.getEntry(name).isDirectory

    fun load(name: String): Envelope? {
        val entry = zipFile.getEntry(name)
        if (entry == null || entry.isDirectory) {
            return null
        }
        val bytes = zipFile.getInputStream(entry).readAllBytes()
        val binary = bytes.asBinary()
//        val text = bytes.inputStream().bufferedReader().readText() // Смотрю содержимое в дебагерре
        return formatPeeker(binary)?.let {
            val partialEnvelope = binary.read {
                it.run { readPartial(this@read) }
            }
            val offset: Int = partialEnvelope.dataOffset.toInt() // Посчитан не правильно
            val size: Int = partialEnvelope.dataSize?.toInt() ?: (binary.size.toInt() - offset)
            val cut_bytes = bytes.sliceArray(offset..(offset+size-1))
//            val text1 = cut_bytes.inputStream().bufferedReader().readText() // Смотрю содержимое в дебагерре
            SimpleEnvelope(partialEnvelope.meta, cut_bytes.asBinary())
        }
    }

    companion object{
        fun open(context: Context, path: Path, formatPeeker: (Binary) -> EnvelopeFormat? = DefaultFormatPeeker(context
        )::peek) : ZipEnvelopeLoader {
            return ZipEnvelopeLoader(context, ZipFile(path.toFile()), formatPeeker)
        }
    }
}