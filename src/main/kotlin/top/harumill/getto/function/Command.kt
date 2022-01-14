package top.harumill.getto.function

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.MessageEvent
import top.harumill.getto.bot.logger.GettoLogger
import top.harumill.getto.bot.logger.Priority

// !{name} para1 para2 ...
abstract class Command : Function() {

    protected val parameterList = mutableMapOf<String, String?>()

    init {

        config.params.forEach { params ->
            parameterList[params] = null
        }
    }

    abstract suspend fun onEnable(contact: Contact, sender: User)

    fun invoke(params: List<String>) {
        require(parameterList.size == params.size) {
            GettoLogger.log(
                Priority.ERROR,
                "Parameter's count is not same with Arguments' count!",
                "Command $name"
            )
        }

        var cnt = 0
        config.params.forEach { parameter ->
            parameterList[parameter] = params[cnt++]
        }
    }

    override suspend fun onMessageEvent(messageEvent: MessageEvent) {
//        onEnable(messageEvent)
    }

    override suspend fun onAllEvent(event: Event) {
        //
    }
}