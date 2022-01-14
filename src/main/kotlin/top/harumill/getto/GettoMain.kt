package top.harumill.getto

import kotlinx.coroutines.runBlocking
import top.harumill.getto.bot.GettoBot
import top.harumill.getto.bot.logger.GettoLogger
import top.harumill.getto.bot.logger.Output

fun main() = runBlocking {
    GettoBot.start()
    GettoLogger.log(message = "Progress exit.", from = "GettoMain", to = Output.FILE)
    println("Progress exit.")
}