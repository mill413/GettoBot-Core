package top.harumill.getto.data.musicRes

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    val id: Int,
    @Required
    val name: String
)