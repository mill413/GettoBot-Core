package top.harumill.getto

import kotlinx.coroutines.runBlocking
import top.harumill.getto.bot.Getto
import java.time.LocalDateTime

fun main(){
    Getto.statusLogFile.appendText("${LocalDateTime.now()} program start.\n")
    while (true){
        runBlocking {
            Getto.start()
        }
        Getto.bot.close()
        val text = "${LocalDateTime.now()} runBlocking break up.\n"
        println(text)
        Getto.statusLogFile.appendText(text)
    }
}