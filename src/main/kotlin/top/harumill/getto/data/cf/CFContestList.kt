package top.harumill.getto.data.cf

import kotlinx.serialization.Serializable

@Serializable
data class CFContestList(
    val status: String,
    val result: List<Contest>
)
