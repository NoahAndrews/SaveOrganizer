package noahandrews.me.saveorganizer.util

interface PerfTimerFactory {
    fun newPerfTimer(tag: String, label: String): PerfTimer

}

interface PerfTimer {
    fun addSplit(splitLabel: String)
    fun dumpToLog()
}
