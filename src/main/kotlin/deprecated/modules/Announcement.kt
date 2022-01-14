package deprecated.modules

import deprecated.bot.Getto
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.subscribeMessages

class Announcement(
    override val name: String = "公告",
    override val description: String = ""
) : Mod() {

    override suspend fun onEnable() {
        gettoEventChannel.filter { it is MessageEvent && it.sender.id == Getto.info.administrator }
            .subscribeMessages {
                startsWith("公告") { arg ->
                    when {
                        arg.startsWith("修改") -> {
                            val ann = arg.removePrefix("修改")
                            Getto.announcement = ann
                            subject.sendMessage("已修改公告为 ${Getto.announcement}")
                        }
                        arg.startsWith("查看") -> {
                            subject.sendMessage(Getto.announcement)
                        }
                        arg.startsWith("追加") -> {
                            val exann = arg.removePrefix("追加")
                            Getto.announcement = Getto.announcement + exann
                            subject.sendMessage("已修改公告为 ${Getto.announcement}")
                        }
                        arg.startsWith("help") -> {
                            subject.sendMessage(
                                """
                                1.修改公告：公告 修改 [内容]
                                2.查看公告：公告 查看
                                3.追加公告：公告 追加 [内容]
                            """.trimIndent()
                            )
                        }
                    }
                }
            }
    }
}