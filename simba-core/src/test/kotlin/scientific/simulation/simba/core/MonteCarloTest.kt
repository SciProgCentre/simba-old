package scientifik.simulation.simba.core

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import org.apache.commons.math3.stat.descriptive.SummaryStatistics
import java.util.*
import java.util.stream.Stream
import kotlin.test.Test

class MonteCarloTest {

//    val generator = object : Generator<Double> {
//        val rnd = Random(0)
//        override val bucket: ReceiveChannel<Double> = GlobalScope.produce<Double>(capacity = Channel.UNLIMITED) {
//            while (true) {
//                val number = rnd.nextInt(10)
//                println("Generate: $number")
//                delay(number * 100L)
//                send(number.toDouble())
//            }
//
//        }
//    }

    val accumulator = SpaceAccumulator<Double>(DoubleSpace)
    val channelAccumulator = ChannelSpaceAccumulator<Double>(DoubleSpace)




    @Test
    fun testSequnceChain() {

        val mc = object : MonteCarlo<Double> {

            val accumulator = SpaceAccumulator(DoubleSpace)

            override fun generateChain(): Chain<Double> {
                return SequenceChain(
                    sequence {
                        val rnd = Random(0)
                        while (true) {
                            yield(rnd.nextGaussian())
                        }
                    },
                    accumulator
                )
            }

        }

        val chain = mc.generateChain()

        val stat = SummaryStatistics()

        chain.asSequence().take(500).forEach{
            stat.addValue(it)
        }

        println("${mc.accumulator.get().value} ${stat.mean}")

        chain.asSequence().take(5000).forEach{
            stat.addValue(it)
        }
        println("${mc.accumulator.get().value} ${stat.mean}")

        chain.asSequence().take(5000).forEach{
            stat.addValue(it)
        }
        println("${mc.accumulator.get().value} ${stat.mean}")
        println("${stat.standardDeviation}")

    }

    @Test
    fun testParallelStream(){

        val mc = object : MonteCarlo<Double> {

        private val _accumulators = mutableListOf<Accumulator<Double>>()

            override fun generateChain(): Chain<Double> {
                val accumulator = SpaceAccumulator(DoubleSpace)
                _accumulators.add(accumulator)
                println("Generate chain")
                return SequenceChain(
                    sequence {
                        val rnd = Random(0)
                        while (true) {
                            yield(rnd.nextGaussian())
                        }
                    },
                    accumulator
                )
            }
            val result: MCResult<Double>
            get() = _accumulators.map { it.get() }.reduce { acc, mcResult ->  with(DoubleSpace){
                    acc.join(mcResult)
                } }

        }


        Stream.generate{mc.generateChain()}.limit(6).parallel().forEach {
            it.jumpOn(500)
        }

        println("${mc.result.value}")
    }
}