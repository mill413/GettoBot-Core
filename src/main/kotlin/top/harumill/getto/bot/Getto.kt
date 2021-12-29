package top.harumill.getto.bot

import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.MiraiLogger
import top.harumill.getto.tools.ModuleManager
import java.io.File
import java.net.URL
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.zip.GZIPInputStream

object Getto {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSS")
    var info: BotInfo
    lateinit var bot: Bot
    private lateinit var start: LocalDateTime
    var reloginTime = 5*60L

    /**
     * 公告
     * get：获取data/announcement.txt里内容
     * set：覆写data/announcement.txt里内容
     */
    var announcement: String
        get() {
            val announcementFile = File("data/announcement.txt")
            if (!announcementFile.exists()) announcementFile.createNewFile()
            var announcement = ""
            announcementFile.readLines().forEach {
                announcement += it
            }
            return announcement
        }
        set(ann) {
            val announcementFile = File("data/announcement.txt")
            if (!announcementFile.exists()) announcementFile.createNewFile()
            var announcement = ""
            announcementFile.readLines().forEach {
                announcement += it
            }
            announcementFile.writeText(ann)
        }
    private const val updateStatusTime = 30L
    val statusLogFile = File("logs/status.log")

    var delayTime:Long = 2000

    init {
        var infoString = ""
        File("data/jsons/botInfo.json").readLines().forEach {
            infoString = infoString.plus(it)
        }
        info = Json.decodeFromString(infoString)


    }

    suspend fun start() {
        MiraiLogger
        start = LocalDateTime.now()
        bot = BotFactory.newBot(
            info.id,
            info.passwd,
        ) {
            fileBasedDeviceInfo()
            protocol = when (info.protocol) {
                "ANDROID_PAD" -> BotConfiguration.MiraiProtocol.ANDROID_PAD
                "ANDROID_WATCH" -> BotConfiguration.MiraiProtocol.ANDROID_WATCH
                else -> BotConfiguration.MiraiProtocol.ANDROID_PHONE
            }
            if (info.networkLog.isNotEmpty()) {
                redirectNetworkLogToDirectory(dir = File(info.networkLog))
            }
            if (info.botLog.isNotEmpty()) {
                redirectBotLogToDirectory(dir = File(info.botLog))
            }

        }
        bot.login()

        ModuleManager.load()

        val login = LocalDateTime.now()
        if (info.loginTip){
            sendToAdministrator(
                "${
                    formatter.format(login)
                } ${bot.nick}(${bot.id})" +
                        " 登录成功" + " 开机用时${Duration.between(start, login).toMillis().toDouble() / 1000}s"
            )
        }


        info.lastlogin = formatter.format(login)

        overwriteInfo(info)

        while (true) {
            val text = """
                ${formatter.format(start)} To ${formatter.format(LocalDateTime.now())}:
                ${if (bot.isOnline) "online" else "offline"} and ${if (bot.isActive) "active" else "inactive"}
                
            """.trimIndent()
            statusLogFile.appendText(text)

            if (!(bot.isOnline && bot.isActive)) {
                break
            }

            delay(updateStatusTime * 1000)
        }
    }

    suspend fun sendToAdministrator(msg: Message) {
        try {
            bot.getFriendOrFail(info.administrator).sendMessage(msg)
        }catch (e:Exception){
            bot.getFriendOrFail(info.administrator).sendMessage("消息发送失败，请前往日志查看")
            val file = File("logs/exceptionLog.log")
            file.appendText("""
                ${formatter.format(LocalDateTime.now())} 发送一条消息时报错
                消息内容:
                $msg
                异常信息:
                ${e.message}
                ${e.stackTrace}
                
            """.trimIndent())
        }
    }

    suspend fun sendToAdministrator(msg: String) {
       sendToAdministrator(PlainText(msg))
    }

    fun showDateTime(datetime: java.time.temporal.TemporalAccessor): String {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(datetime)
    }

    fun overwriteInfo(info:BotInfo){
        val str = Json { prettyPrint = true }.encodeToString(info)
        File("data/jsons/botInfo.json").writeText(str)
    }
    object Utils {
        fun getImgList(path: String): MutableList<String> {
            val files: MutableList<String> = mutableListOf()
            val fileTree: FileTreeWalk = File(path).walk()
            fileTree.maxDepth(1)
                .filter { it.isFile }
                .forEach { files.add(it.name) }
            return files
        }

        fun downloadFile(url: String, fileName: String, fileType: String? = null, path: String = "data/"): File {
            val openConnection = URL(url).openConnection()
            var type = "."
            if (fileType == null) {
                val contentType = openConnection.contentType
                var copyOrNot = false
                contentType.forEach {
                    if (it == '/' && !copyOrNot)
                        copyOrNot = true
                    else if (copyOrNot) {
                        type = type.plus(it)
                    }
                }
            } else {
                type += fileType
            }

            val file = File("$path$fileName$type")
            //防止某些网站跳转到验证界面
//        openConnection.addRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
            //如果图片是采用gzip压缩
            val bytes = if (openConnection.contentEncoding == "gzip") {
                GZIPInputStream(openConnection.getInputStream()).readBytes()
            } else {
                openConnection.getInputStream().readBytes()
            }
            file.writeBytes(bytes)
            return file
        }
    }


}