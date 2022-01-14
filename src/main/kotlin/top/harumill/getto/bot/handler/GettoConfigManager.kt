package top.harumill.getto.bot.handler

import top.harumill.getto.data.config.FunctionConfig
import top.harumill.getto.data.config.GettoConfig
import top.harumill.getto.tools.decodeFromJSONFile

// TODO-写的太烂了
object GettoConfigManager {
    private const val configPath = "./data/GettoConfig.json"

    private val functionConfigPool = mutableMapOf<String, FunctionConfig>()

    private fun getGettoConfig(): GettoConfig {
        return decodeFromJSONFile(configPath)
    }

    fun getFunctionConfig(name: String): FunctionConfig? = functionConfigPool[name]

    fun getFunctionsList(): List<String> {
        val functionList = mutableListOf<String>()
        functionConfigPool.forEach { (t, _) ->
            functionList.add(t)
        }
        return functionList.toList()
    }

    init {
        val functionList = getGettoConfig().functionList
        functionList.forEach { entity ->
            functionConfigPool[entity.name] = entity.config
        }
    }
}