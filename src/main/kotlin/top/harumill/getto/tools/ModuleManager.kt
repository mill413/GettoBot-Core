package top.harumill.getto.tools

import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.subscribeFriendMessages
import top.harumill.getto.bot.Getto
import top.harumill.getto.modules.*

object ModuleManager : Mod() {
    var modList = mutableListOf(
        SendPic(),
        Chat(),
        GroupManage(),
//        Repeat(),
        BotManage(),
        Sign(),
//        Recall(),
        DragonBot(),
        SearchMusic(),
//        FlashCollect()
//        GettoLog()
    )

    override val name: String = "ModuleManager"
    override val description: String = ""
    override suspend fun onEnable() {
        modList.forEach {
            it.load()
        }

        gettoEventChannel.filter { it is FriendMessageEvent && it.sender.id == Getto.info.administrator }
            .subscribeFriendMessages {
                startsWith("mod") { arg ->
                    when (arg) {
                        "list" -> {
                            var mods = ""
                            modList.forEach {
                                mods += "${it.name}模块: ${if (it.switch) "on" else "off"}\n"
                            }
                            Getto.sendToAdministrator(mods)
                        }
                    }
                }
            }

    }
}