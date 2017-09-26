package noahandrews.me.saveorganizer.util

import android.util.TimingLogger

class AndroidPerfTimer (private val tag: String, private val label: String) : PerfTimer {
    var timer = TimingLogger(tag, label)

    override fun addSplit(splitLabel: String) {
       timer.addSplit(splitLabel)
    }

    override fun dumpToLog() {
        timer.dumpToLog()
    }

}

class AndroidPerfTimerFactory : PerfTimerFactory {
    override fun newPerfTimer(tag: String, label: String): PerfTimer {
       return AndroidPerfTimer(tag, label)
    }

}