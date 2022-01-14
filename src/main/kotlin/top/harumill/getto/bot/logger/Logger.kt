package top.harumill.getto.bot.logger

abstract class Logger {

    abstract fun normal(e: String)

    abstract fun warn(e: String)

    abstract fun error(e: String)
}