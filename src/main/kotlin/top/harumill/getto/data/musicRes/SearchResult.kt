package top.harumill.getto.data.musicRes

import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    val songCount: Int,
    val songs: List<Song>
)