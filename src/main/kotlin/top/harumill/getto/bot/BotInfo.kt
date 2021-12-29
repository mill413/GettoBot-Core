package top.harumill.getto.bot

import kotlinx.serialization.Serializable

@Serializable
data class BotInfo(
    val id: Long,
    val passwd: String,
    val protocol: String,
    val administrator: Long,
    val wife: Long,
    val networkLog:String,
    val botLog:String,
    var lastlogin:String,
    var autoAddFriend:Boolean,
    var autoAddGroup:Boolean,
    var loginTip:Boolean
)
