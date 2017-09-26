package noahandrews.me.saveorganizer

import com.google.common.base.Stopwatch
import noahandrews.me.saveorganizer.util.PerfTimer
import noahandrews.me.saveorganizer.util.PerfTimerFactory
import java.util.concurrent.TimeUnit

class TestPerfTimer (private val tag: String, private val label: String) : PerfTimer {
    private val splits : ArrayList<Split> = ArrayList()

    private val stopwatch : Stopwatch = Stopwatch.createStarted()

    private val header = "$tag: $label:\t\t"

    override fun addSplit(splitLabel: String) {
        splits.add(Split(stopwatch.elapsed(TimeUnit.MILLISECONDS), splitLabel))
    }

    override fun dumpToLog() {
        stopwatch.stop()
        if(splits.isEmpty()) {
            println("$header ${stopwatch.elapsed(TimeUnit.MILLISECONDS)} ms")
        } else {
            println("$header begin")
            for ((i, split) in splits.withIndex()) {
                val prev : Long = if(i>=1) {
                    splits[i-1].elapsedTimeMs
                } else {
                    0
                }
                println("$header ${split.elapsedTimeMs - prev} ms, ${split.label}")
            }
            println("$header end, ${stopwatch.elapsed(TimeUnit.MILLISECONDS)} ms")
        }
    }

    private data class Split(val elapsedTimeMs: Long, val label: String)
}

class TestPerfTimerFactory : PerfTimerFactory {
    override fun newPerfTimer(tag: String, label: String): PerfTimer {
       return TestPerfTimer(tag, label)
    }
}