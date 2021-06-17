package top.harumill.getto.data

import net.mamoe.mirai.message.data.MessageChain
import java.time.LocalDateTime

data class MyMessage(
    val msg: MessageChain,
    val time: LocalDateTime
)
