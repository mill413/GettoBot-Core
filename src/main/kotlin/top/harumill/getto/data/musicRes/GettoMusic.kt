package top.harumill.getto.data.musicRes

import kotlinx.serialization.Serializable

@Serializable
data class GettoMusic(
    val code: Int,
    val result: SearchResult
)