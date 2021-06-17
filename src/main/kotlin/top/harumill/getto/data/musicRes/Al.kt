package top.harumill.getto.data.musicRes

import kotlinx.serialization.Serializable

@Serializable
data class Al(
    val id: Int,
    val name: String,
    val pic: Long,
    val picUrl: String,
    val pic_str: String? = null
)