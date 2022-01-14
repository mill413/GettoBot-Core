package deprecated.modules

import deprecated.bot.Getto
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.subscribeMessages
import java.time.LocalDateTime

/**
 * 模块抽象类，所有模块必须继承此类,并实现[onEnable]方法
 *
 * 若以代码形式扩展模块，需手动在 [ModuleManager][top.harumill.getto.tools.ModuleManager] 中注册，详细方法参看代码
 *
 * （否则将继承了Mod类的模块类打包为jar）
 *
 * @property switch 提供给开发者的模块开关，开发者可直接利用该属性对整个模块进行控制
 * @property name 模块的名称
 * @property description 模块的描述
 *
 */
abstract class Mod {
    var switch = true
    abstract val name: String
    abstract val description: String

    val gettoEventChannel: EventChannel<BotEvent>
        get() = Getto.bot.eventChannel

    abstract suspend fun onEnable()

    fun logOut(info: String) {
        println(
            "${
                Getto.showDateTime(LocalDateTime.now())
            } Getto/Mod-$name: $info"
        )
    }

    suspend fun load() {
        onEnable()
        control()
        logOut("Load Successfully.")
    }

    private suspend fun control() {
        gettoEventChannel.filter {
            it is MessageEvent && it.sender.id == Getto.info.administrator
        }
            .subscribeMessages {
                startsWith(name) { arg ->
                    when (arg) {
                        "on" -> {
                            switch = true
                            subject.sendMessage("${name}模块开启")
                        }
                        "off" -> {
                            switch = false
                            subject.sendMessage("${name}模块关闭")
                        }
                        "check" -> {
                            subject.sendMessage("${name}模块已${if (switch) "开启" else "关闭"}")
                        }
                        "des" -> {
                            subject.sendMessage("${name}模块描述:\n${description}")
                        }
                        else -> {
//                            bot.getFriendOrFail(Getto.info.administrator).sendMessage("参数错误")
                        }
                    }
                }
            }
    }

}

