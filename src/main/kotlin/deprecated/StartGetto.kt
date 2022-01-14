package top.harumill.getto

import deprecated.bot.Getto
import kotlinx.coroutines.runBlocking
import java.io.FileNotFoundException
import java.time.LocalDateTime

fun main() {
    try {
        Getto.statusLogFile.appendText("${LocalDateTime.now()} program start.\n")
    } catch (err: FileNotFoundException) {
        Getto.statusLogFile.createNewFile()
    }
    while (true) {
        runBlocking {
            Getto.start()
        }
        Getto.bot.close()
        val text = "${LocalDateTime.now()} runBlocking break up.\n"
        println(text)
        Getto.statusLogFile.appendText(text)

    }
}