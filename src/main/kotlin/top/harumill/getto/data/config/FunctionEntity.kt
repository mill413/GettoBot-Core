package top.harumill.getto.data.config

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class FunctionEntity(
    @Required
    val name: String,
    @Required
    val config: FunctionConfig
)
