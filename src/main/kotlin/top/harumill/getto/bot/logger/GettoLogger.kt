package top.harumill.getto.bot.logger

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

enum class Output {
    STD, FILE
}

enum class Priority {
    NORMAL, WARN, ERROR
}

object GettoLogger {


    data class Entry(
        val message: String,
        val output: Output,
        val priority: Priority
    )

    private val logQueue = mutableListOf<Entry>()

    fun log(priority: Priority = Priority.NORMAL, message: String, from: String, to: Output = Output.STD) {
        logQueue.add(Entry("${from}: $message", to, priority))
    }

    suspend fun onEnable() = coroutineScope {
        log(message = "GettoLogger start.", from = "GettoLogger")
        while (true) {
            if (logQueue.size != 0) {
                val entry = logQueue.first()
                logQueue.removeAt(0)
                val logger = when (entry.output) {
                    Output.STD -> StdLogger
                    Output.FILE -> FileLogger
                }

                when (entry.priority) {
                    Priority.NORMAL -> logger.normal(entry.message)
                    Priority.WARN -> logger.warn(entry.message)
                    Priority.ERROR -> logger.error(entry.message)
                }
            } else {
                delay(1)
            }
        }
    }


}