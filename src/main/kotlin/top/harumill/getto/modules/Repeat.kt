package top.harumill.getto.modules

import kotlinx.coroutines.delay
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.Face
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.PlainText
import top.harumill.getto.bot.Getto

class Repeat(
    override val name: String = "Repeat",
    override val description: String = ""
) : Mod() {

    var repeatRatio = 0.03

    override suspend fun onEnable() {
        gettoEventChannel.subscribeAlways<GroupMessageEvent> {
            if (switch) {
                var repeatOrNot = true
                //判断是否只含有文字，图片及表情
                message.forEach {
                    if (it !is PlainText && it !is Image && it !is Face) {
                        repeatOrNot = false
                        return@forEach
                    }
                }
                if (repeatOrNot  && switch) {
                    if ((0..1000).random() < repeatRatio * 1000) {
                        delay(1000)
                        group.sendMessage(message)
                    }
                }
            }
        }

        gettoEventChannel.filter {
            it is FriendMessageEvent && it.friend.id == Getto.info.administrator
        }
            .subscribeMessages {
                startsWith("复读概率") { arg ->
                    try {
                        repeatRatio = arg.toDouble()
                        bot.getFriendOrFail(Getto.info.administrator).sendMessage("复读概率已修改为$repeatRatio")
                    } catch (e: Exception) {
                        bot.getFriendOrFail(Getto.info.administrator).sendMessage("参数错误: ${e.message}")
                    }
                }
            }
    }
}