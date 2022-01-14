package top.harumill.getto.bot.logger

import top.harumill.getto.tools.getCurrentDate
import java.io.File

object FileLogger : Logger() {
    private val logger = GettoLog()

    private const val logDirectoryPath = "./logs"

    private val logDirectory = checkFileExist(logDirectoryPath, true)

    private val todayLogFile = checkFileExist("${logDirectoryPath}/${getCurrentDate()}.log", false)

    private fun checkFileExist(path: String, isDir: Boolean): File {
        val file = File(path)
        if (!file.exists()) {
            if (isDir) file.mkdirs() else file.createNewFile()
        }
        return file
    }

    override fun normal(e: String) {
        todayLogFile.appendText(logger.normal(e + "\n"))
    }

    override fun warn(e: String) {
        todayLogFile.appendText(logger.warn(e + "\n"))
    }

    override fun error(e: String) {
        todayLogFile.appendText(logger.error(e + "\n"))
    }
}