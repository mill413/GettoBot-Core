package deprecated.modules

import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.FriendMessagePostSendEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.GroupMessagePostSendEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.utils.MiraiInternalApi
import net.mamoe.mirai.utils.PlatformLogger

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GettoLog(
    override val name: String = "Log",
    override val description: String = ""
) : Mod() {
    var logger = GettoLogger(name)

    override suspend fun onEnable() {
        gettoEventChannel.subscribeAlways<Event> {
            logger.logEvent(it)
        }

    }
}


@OptIn(MiraiInternalApi::class)
class GettoLogger(
    val id: String?
) : PlatformLogger() {
    override val identity: String
        get() = id ?: "null"

    private fun ouputLog(log: String) {
        println("${formatDatetime(LocalDateTime.now())} Getto/Log: $log")

    }

    private fun formatDatetime(datetime: LocalDateTime): String {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS").format(datetime)
    }

    private fun formatMessage(msg: MessageChain): String {
        return msg.toString().replace("\n", "\\n")
    }

    fun logEvent(event: Event) {
        val eventName = event.javaClass.simpleName
        val noLogEvent = arrayListOf(
            "ConfigPush",
            "FriendMessagePreSendEvent",
            "FriendInputStatusChangedEvent",
            "GetMsgSuccess",
            "GroupMessagePreSendEvent",
            "MemberCardChangeEvent",
            "SendGroupMessageReceipt",
            "BeforeImageUploadEvent",
            "Succeed",
            "ServerListPush",
            "EmptyResponse"
        )
        when (eventName) {
            in noLogEvent -> {
//                ouputLog(eventName)
            }
            "FriendMessagePostSendEvent" -> {
                val fmpse = (event as FriendMessagePostSendEvent)
                val target = fmpse.target
                val msg = formatMessage(fmpse.message)
                ouputLog("MessageTofriend ${target.nick}(${target.id}): $msg")
            }
            "FriendMessageEvent" -> {
                val fme = (event as FriendMessageEvent)
                val msg = formatMessage(fme.message)
                ouputLog("MessageFromFriend ${fme.senderName}(${fme.sender.id}): ${msg}")
            }
            "GroupMessageEvent" -> {
                val gme = (event as GroupMessageEvent)
                val group = gme.group
                val sender = gme.sender
                val permission = gme.permission
                val msg = formatMessage(gme.message)
                ouputLog("MessageFromGroup ${group.name}(${group.id})'s $permission ${sender.nick}(${sender.id}): $msg")
            }
            "GroupMessagePostSendEvent" -> {
                val gmpse = (event as GroupMessagePostSendEvent)
                val group = gmpse.target
                val msg = formatMessage(gmpse.message)
                ouputLog("MessageToGroup ${group.name}(${group.id}): $msg")
            }
            "NudgeEvent" -> {
                ouputLog(event.toString())
            }
            else -> {
                println(event)
                println(eventName)
            }
        }
    }
}