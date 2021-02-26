package ru.mipt.npm.abt

import java.nio.channels.SeekableByteChannel

interface BlockType

interface ABTNode<B : BlockType> {
    val type: B
    val startOffset: Long
    val endOffset: Long
    val parent: ABTNode<B>?
    val children: List<ABTNode<B>>
}

val <B : BlockType>ABTNode<B>.size: Long
    get() = endOffset - startOffset

abstract class AbstractNode<B : BlockType>(
    override val type: B,
    override val startOffset: Long,
    override val endOffset: Long
) : ABTNode<B> {
    final override var parent: ABTNode<B>? = null
        internal set
}

open class LeafNode<B : BlockType>(
    type: B,
    startOffset: Long,
    endOffset: Long
) : AbstractNode<B>(type, startOffset, endOffset) {
    override val children: List<ABTNode<B>>
        get() = emptyList()

}

open class LazyNode<B : BlockType>(
    type: B,
    startOffset: Long,
    endOffset: Long,
    private val childrenProducer: Sequence<ABTNode<B>>
) : AbstractNode<B>(type, startOffset, endOffset) {
    override val children: List<ABTNode<B>> by lazy {
        childrenProducer.toList()
    }
}

interface Parser<B: BlockType>{
    fun parse(source: SeekableByteChannel) : ABTNode<B>
}

interface FileParser<B : BlockType> {
    fun buildTree(source: SeekableByteChannel): ABTNode<B>
}

interface Reader<T, B : BlockType> {
    fun read(source: SeekableByteChannel, node: ABTNode<B>): T
}
