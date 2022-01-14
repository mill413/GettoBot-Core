package deprecated.bot

import kotlinx.serialization.Serializable

@Serializable
data class BotInfo(
    val id: Long = 3417987894,
    val passwd: String = "Millharuto0628",
    val protocol: String,
    val administrator: Long,
    val wife: Long,
    val networkLog: String,
    val botLog: String,
    var lastlogin: String,
    var autoAddFriend: Boolean,
    var autoAddGroup: Boolean,
    var loginTip: Boolean
)
