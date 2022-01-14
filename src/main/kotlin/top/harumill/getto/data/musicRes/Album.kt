package top.harumill.getto.data.musicRes

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val id: Int,
    val name: String,
    val pic: Long,
    @Required
    val picUrl: String,
    val pic_str: String? = null
)