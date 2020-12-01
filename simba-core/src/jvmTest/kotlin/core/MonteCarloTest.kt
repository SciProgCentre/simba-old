package scientifik.simulation.simba.core

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import org.apache.commons.math3.stat.descriptive.SummaryStatistics
import java.util.*
import java.util.stream.Stream
import kotlin.test.Test

class MonteCarloTest {

    @Test
    fun test(){
        runBlocking {
            val f = flow{
                (1..3).forEach{emit(it)}
            }
            f.onEach{println("A: $it")}
            f.onEach{println("B: $it")}
            f.collect()
        }
    }
}