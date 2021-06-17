package top.harumill.getto.data.musicRes

import kotlinx.serialization.Serializable

@Serializable
data class FreeTrialPrivilege(
    val resConsumable: Boolean,
    val userConsumable: Boolean
)