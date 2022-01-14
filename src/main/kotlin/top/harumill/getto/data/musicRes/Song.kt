package top.harumill.getto.data.musicRes

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Song(
    @SerialName("al")
    @Required
    val album: Album,
    @SerialName("ar")
    @Required
    val artists: List<Artist>,
    @Required
    val id: Long,
    @Required
    val name: String,
    val publishTime: Long

)