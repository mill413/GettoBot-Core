package top.harumill.getto.modules

import kotlinx.coroutines.delay
import net.mamoe.mirai.event.events.BotLeaveEvent
import net.mamoe.mirai.event.events.MemberJoinEvent
import net.mamoe.mirai.event.events.MemberLeaveEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.MiraiExperimentalApi
import top.harumill.getto.bot.Getto


class GroupManage(
    override val name: String = "GroupManage",
    override val description: String = ""
) : Mod() {
    @OptIn(MiraiExperimentalApi::class)
    override suspend fun onEnable() {

        /**
         * 成员退群
         */
        gettoEventChannel.subscribeAlways<MemberLeaveEvent> {
            group.sendMessage("${member.nick}离开了我们。。。 ")
        }

        /**
         * 新人入群
         */
        gettoEventChannel.subscribeAlways<MemberJoinEvent> {
            delay(5000)
            group.sendMessage(
                PlainText("欢迎新人") +
                        At(member) +
                        PlainText("加群,要和大家愉快玩耍哦，发送#help可获取bot可用指令")
            )
        }

        /**
         * bot被踢出群事件
         */
        gettoEventChannel.subscribeAlways<BotLeaveEvent.Kick> {
            bot.getFriendOrFail(Getto.info.administrator)
                .sendMessage("被${operator.nick}(${operator.id})踢出群${group.name}(${group.id})")
        }

        /**
         * bot主动退群事件
         */
        gettoEventChannel.subscribeAlways<BotLeaveEvent.Active> {
            Getto.sendToAdministrator("主动退出群${group.name}(${group.id})")
        }
    }
}