package deprecated.modules

import deprecated.bot.Getto
import deprecated.tools.MessagesPool
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageRecallEvent
import net.mamoe.mirai.event.subscribeFriendMessages
import net.mamoe.mirai.message.data.PlainText
import java.time.LocalDateTime

class Recall(
    override val name: String = "Recall",
    override val description: String = ""
) : Mod() {
    override suspend fun onEnable() {
        switch = false
        gettoEventChannel.subscribeAlways<GroupMessageEvent> {
            MessagesPool.insert(message, LocalDateTime.now())
        }
        gettoEventChannel.subscribeAlways<MessageRecallEvent.GroupRecall> {
            if (switch) {
                val dtm = LocalDateTime.now()
                Getto.sendToAdministrator(PlainText("${dtm.toLocalDate()} ${dtm.hour}:${dtm.minute}:${dtm.second}\n${operator?.nick}(${operator?.id})\n在群${group.name}(${group.id})撤回了一条\n${author.nick}(${authorId})发送的消息"))
                val msg = MessagesPool.getById(messageIds[0])
                if (msg != null)
                    Getto.sendToAdministrator(msg)
                else
                    Getto.sendToAdministrator("该撤回消息已过期")
            }
        }
        gettoEventChannel.filter { it is FriendMessageEvent && it.friend.id == Getto.info.administrator }
            .subscribeFriendMessages {
                "Recall" { target ->
                    when (target) {
                        "on" -> {
                            switch = true
                        }

                    }
                }
            }
    }
}