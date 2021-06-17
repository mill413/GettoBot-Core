package top.harumill.getto.data.musicRes

import kotlinx.serialization.Serializable

@Serializable
data class L(
    val br: Int,
    val fid: Int,
    val size: Int,
    val vd: Double
)