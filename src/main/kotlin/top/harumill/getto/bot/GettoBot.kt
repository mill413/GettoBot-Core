package top.harumill.getto.bot

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.BotOfflineEvent
import net.mamoe.mirai.event.events.BotOnlineEvent
import net.mamoe.mirai.event.events.BotReloginEvent
import net.mamoe.mirai.utils.BotConfiguration.MiraiProtocol
import net.mamoe.mirai.utils.DirectoryLogger
import top.harumill.getto.bot.handler.GettoFunctionManager
import top.harumill.getto.bot.logger.GettoLogger
import top.harumill.getto.bot.logger.Output
import top.harumill.getto.data.config.GettoConfig
import top.harumill.getto.tools.decodeFromJSONFile

object GettoBot {

    private val config: GettoConfig
        get() {
            return decodeFromJSONFile("./data/GettoConfig.json")
        }
    val bot: Bot
        get() {
            return BotFactory.newBot(
                config.id,
                config.passwd
            ) {
                protocol = when (config.protocol) {
                    "PHONE" -> MiraiProtocol.ANDROID_PHONE
                    "PAD" -> MiraiProtocol.ANDROID_PAD
                    "WATCH" -> MiraiProtocol.ANDROID_WATCH
                    else -> MiraiProtocol.ANDROID_PHONE
                }
                networkLoggerSupplier = { DirectoryLogger("./logs/mirai") }
//                randomDeviceInfo()
                fileBasedDeviceInfo("./data/device.json")
            }
        }

    suspend fun start() = coroutineScope {
//        bot.otherClients.forEach { println(it.deviceName) }
        launch {
            GettoLogger.onEnable()
        }

        launch {
            GettoFunctionManager.onEnable()
        }

        bot.login()
        bot.eventChannel.subscribeAlways<BotEvent> { botEvent ->
            when (botEvent) {
                is BotOnlineEvent -> {
                    GettoLogger.log(message = "Bot login.", from = "Bot(${bot.id})", to = Output.FILE)
                }
                is BotReloginEvent -> {
                    GettoLogger.log(message = "Bot relogin.", from = "Bot(${bot.id})", to = Output.FILE)
                    // 重连之后有概率无法监听消息，重新登录
                    bot.login()
                }
                is BotOfflineEvent -> {
                    GettoLogger.log(message = "Bot offline.", from = "Bot(${bot.id})", to = Output.FILE)
                }
            }
        }
    }
}