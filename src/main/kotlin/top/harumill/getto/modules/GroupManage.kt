package top.harumill.getto.modules

import kotlinx.coroutines.delay
import net.mamoe.mirai.event.events.MemberJoinEvent
import net.mamoe.mirai.event.events.MemberLeaveEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
 

class GroupManage(
    override val name: String = "GroupManage",
    override val description: String = ""
) : Mod() {
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

    }
}