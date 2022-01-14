package top.harumill.getto.function

import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.MessageEvent
import top.harumill.getto.bot.handler.GettoConfigManager
import top.harumill.getto.bot.handler.GettoEventHandler
import top.harumill.getto.bot.handler.GettoFunctionManager
import top.harumill.getto.bot.logger.GettoLogger
import top.harumill.getto.data.config.FunctionConfig

abstract class Function {

    val name: String
        get() = this.javaClass.simpleName

    protected val config: FunctionConfig
        get() {
            requireNotNull(GettoConfigManager.getFunctionConfig(name))
            return GettoConfigManager.getFunctionConfig(name)!!
        }

    fun load() {
        GettoLogger.log(message = "Function $name load. GlobalEnabled:${config.globalEnable}", from = "Func $name")
        subscribe()
    }

    protected fun checkUserDismissed(id: Long): Boolean {
        return (!config.globalEnable) || (id in config.dismissUserList)
    }

    protected fun checkGroupDismissed(id: Long): Boolean {
        return (!config.globalEnable) || (id in config.dismissGroupList)
    }

    private fun subscribe() {
        GettoEventHandler.listenEvent<Event> { event ->
            onAllEvent(event)
        }
        GettoEventHandler.listenMessageEvent<MessageEvent> { messageEvent ->
            onMessageEvent(messageEvent)
        }
    }

    abstract suspend fun onAllEvent(event: Event)
    abstract suspend fun onMessageEvent(messageEvent: MessageEvent)

    init {
        registerToManager()
    }

    private fun registerToManager() {
        GettoFunctionManager.register(this)
    }
}
