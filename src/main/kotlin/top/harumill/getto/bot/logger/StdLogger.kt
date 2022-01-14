package top.harumill.getto.bot.logger

object StdLogger : Logger() {
    private val logger = GettoLog()

    override fun normal(e: String) {
        println(logger.normal(e))
    }

    override fun warn(e: String) {
        println(logger.warn(e))
    }

    override fun error(e: String) {
        println(logger.error(e))
    }
}