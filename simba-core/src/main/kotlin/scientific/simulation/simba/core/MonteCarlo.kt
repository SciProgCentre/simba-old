package scientifik.simulation.simba.core

//import kotlinx.coroutines.*
//import kotlinx.coroutines.channels.Channel
//import kotlinx.coroutines.channels.consumeEach

//interface Algebra
//
//inline operator fun <T : Algebra, R> T.invoke(block: T.() -> R): R = run(block)
//
//
//interface Space<T> : Algebra {
//    val zero: T
//
//    fun add(a: T, b: T): T
//
//    fun multiply(a: T, k: Number): T
//
//    operator fun T.times(k: Number) = multiply(this, k)
//    operator fun Number.times(a: T) = multiply(a, this)
//    operator fun T.plus(b: T): T = add(this, b)
//    operator fun T.div(k: Number) = multiply(this, 1/k.toDouble())
//
//    fun MCResult<T>.join(other: MCResult<T>) : MCResult<T>{
//        if (this.number == 0L){
//            return other
//        }
//        if (other.number == 0L){
//            return this
//        }
//        val sumNumber =  this.number + other.number
//        val k1 = this.number.toDouble()/(sumNumber)
//        val k2 = other.number.toDouble()/(sumNumber)
//        val item1 = this.value
//        val item2 = other.value
//        val result = k1*item1 + k2*item2
//        return MCResult(sumNumber,result)
//
//    }
//}
//
//object DoubleSpace : Space<Double> {
//    override val zero: Double = 0.0
//    override fun add(a: Double, b: Double): Double {
//        return a + b
//    }
//
//    override fun multiply(a: Double, k: Number): Double {
//        return a * k.toDouble()
//    }
//}
//
//interface Accumulator<T> {
//
//    fun accumulate(item: T)
//
//    fun get(): MCResult<T>
//
//    val counter: Long
//
//}
//
//interface ForkAccumulator<T> : Accumulator<T>{
//    fun fork() : ForkAccumulator<T>
//}
/**
 * Non thread-safe space accumulator
 * Using external parth for thread-safe accumulation
 */
//class SpaceAccumulator<T>(val space: Space<T>) : Accumulator<T> {
//
//    private var _counter = 0L
//    override val counter: Long
//        get() = _counter
//
//    private var current: T = space.zero
//
//    override fun get(): MCResult<T> {
//        return with(space) {
//            MCResult(counter, current / counter)
//        }
//    }
//
//    override fun accumulate(item: T) {
//        with(space) {
//            current = current + item
//            _counter++
//        }
//    }
//}

///**
// * Classic thread-safe space accumulator
// */
//class MutexSpaceAccumulator<T>(val space: Space<T>) : Accumulator<T> {
//
//    private val mutex = Mutex()
//    private var _counter = AtomicLong(0)
//    override val counter: Long
//        get() = _counter.get()
//
//    private var current: T = space.zero
//
//    override fun get(): MCResult<T> {
//        return runBlocking {
//            with(space) {
//                MCResult(counter, current / counter)
//            }
//        }
//    }
//
//    override fun accumulate(item: T) {
//        with(space) {
//                current = current + item
//                _counter.incrementAndGet()
//        }
//    }
//}
//
//@ExperimentalCoroutinesApi
//class ChannelSpaceAccumulator<T>(val space: Space<T>, val coroutineScope: CoroutineScope = GlobalScope) : Accumulator<T> {
//    override fun accumulate(item: T) {
//        coroutineScope.launch {
//            channel.send(item)
//        }
//    }
//
//
//    override fun get(): MCResult<T> {
//        return with(space) {
//            runBlocking {
//                MCResult(counter, current / counter)
//            }
//        }
//    }
//
//    private val channel = Channel<T>()
//    private val job = coroutineScope.launch {
//        channel.consumeEach {
//            _counter++
//            with(space) {
//                current += it
//            }
//        }
//    }
//    private var current: T = space.zero
//    private var _counter = 0L
//
//    override val counter: Long
//        get() = _counter
//}
//
//
//data class MCResult<T>(
//    val number: Long,
//    val value: T
//)
//
//
//
//interface Chain<T> {
//    fun jump(): T
//
//    fun jumpOn(n : Int){
//        for (i in 0 until n){
//            jump()
//        }
//    }
//
//    fun asSequence() = sequence {
//        while (true) {
//            yield(jump())
//        }
//    }
//}
//
//class SequenceChain<T>(
//    private val sequence: Sequence<T>,
//    private val accumulator: Accumulator<T>,
//    private val coroutineScope: CoroutineScope = GlobalScope
//) : Chain<T> {
//
//    override fun jump(): T {
//        val item = sequence.first()
//        accumulator.accumulate(item)
//        return item
//    }
//
//    override fun jumpOn(n: Int) {
//        sequence.take(n).forEach {
//            accumulator.accumulate(it)
//        }
//    }
//
//    override fun asSequence(): Sequence<T> {
//        return sequence.map {
//            accumulator.accumulate(it)
//        it}
//    }
//}


//class StreamChain<T>(
//    private val streamProducer:(Int) -> Stream<T>,
//    private val accumulator: Accumulator<T>,
//    private val coroutineScope: CoroutineScope = GlobalScope,
//    private val parallel : Boolean = true
//) : Chain<T>{
//    override fun calculate(n: Int, action: (T) -> Unit): MCResult<T> {
//        var stream = streamProducer(n)
//        stream = if (parallel) stream.parallel() else stream.sequential()
//        stream = if(n== ALL) stream else stream.limit(n.toLong())
//        stream.forEach {
//            coroutineScope.launch {
//                action(it)
//            }
//            accumulator.accumulate(it)
//        }
//        return accumulator.get()
//    }
//}

//class FlowChain<T>(
//    private val bucket: Flow<T>,
//    private val accumulator: Accumulator<T>,
//    private val coroutineScope: CoroutineScope
//) : Chain<T>{
//
//
//    override fun calculate(n: Int, action: (T) -> Unit): MCResult<T> {
//
//            if (n == ALL) {
//                bucket.collect {
//                    accumulator.accumulate(it)
//                    action.invoke(it)
//                }
//            } else {
//                repeat(n) {
//                    val item = bucket.
//                    accumulator.accumulate(item)
//                }
//            }
//
//        }
//        return MCResult(accumulator.counter, accumulator.get())
//    }
//
//}

//interface MonteCarlo<T> {
//    fun generateChain(): Chain<T>
//}
//
//public const val ALL = -1

//class MonteCarlo<T>(
//    private val generator: Generator<T>,
//    private val accumulator: Accumulator<T>
//) : IMonteCarlo<T> {
//
//    suspend fun accumulateSamples(n: Int = ALL, action: (T) -> Unit): MCResult<T> {
//        GlobalScope.launch {
//            if (n == ALL) {
//                generator.bucket.consumeEach {
//                    accumulator.accumulate(it)
//                    action.invoke(it)
//                }
//            } else {
//                repeat(n) {
//                    val item = generator.bucket.receive()
//                    accumulator.accumulate(item)
//                }
//            }
//
//        }
//        return result()
//    }
//
//    override fun result(): MCResult<T> = MCResult(accumulator.counter, accumulator.get())
//
//
//
//}

//interface MonteCarloFactory<T, R : Space<T>> {
//    fun build(): MonteCarlo<T, R>
//
//}