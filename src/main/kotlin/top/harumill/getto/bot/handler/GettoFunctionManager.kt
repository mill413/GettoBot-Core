package top.harumill.getto.bot.handler

import top.harumill.getto.bot.logger.GettoLogger
import top.harumill.getto.bot.logger.Priority
import top.harumill.getto.function.Function
import top.harumill.getto.tools.initObject

object GettoFunctionManager {

    private val functionList = mutableSetOf<Function>()

    fun onEnable() {
        invokeFunctions()

        GettoLogger.log(message = "FunctionManager start.", from = "FunctionManager")
        functionList.forEach {
            it.load()
        }

        GettoCommandManager.onEnable()
    }

    fun register(function: Function) {
        GettoLogger.log(message = "${function.name} register to FunctionManager", from = "FunctionManager")
        functionList.add(function)
    }

    private fun invokeFunctions() {
        val functions = GettoConfigManager.getFunctionsList()

        functions.forEach { className ->
            try {
                initObject("top.harumill.getto.function.functions.${className}")
            } catch (e: ClassNotFoundException) {
                GettoLogger.log(
                    Priority.ERROR,
                    "Function $className Not Found!Please check Function-$className exists or not!",
                    "FunctionManager"
                )
            }
        }
    }
}