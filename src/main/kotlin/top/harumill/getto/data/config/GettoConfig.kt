package top.harumill.getto.data.config

import kotlinx.serialization.Serializable

@Serializable
data class GettoConfig(
    val id: Long,
    val passwd: String,

    val protocol: String,

    val master: Long,

    val functionList: MutableList<FunctionEntity> = mutableListOf()


)
