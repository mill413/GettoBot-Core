package top.harumill.getto.data.musicRes

import kotlinx.serialization.Serializable

@Serializable
data class ChargeInfo(
//    val chargeMessage: Any,
    val chargeType: Int,
//    val chargeUrl: Any,
    val rate: Int
)