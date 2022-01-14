package top.harumill.getto.data.config

import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class FunctionConfig(
    @Required
    val globalEnable: Boolean = true,
    @Required
    val dismissUserList: MutableList<Long> = mutableListOf(),
    @Required
    val dismissGroupList: MutableList<Long> = mutableListOf(),

    // 指令型功能配置项
    val prefixEnable: Boolean = true,
    val commandName: String = "",
    val params: MutableList<String> = mutableListOf()
)
