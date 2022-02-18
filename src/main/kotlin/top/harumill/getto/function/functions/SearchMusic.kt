package top.harumill.getto.function.functions

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.message.data.MusicKind
import net.mamoe.mirai.message.data.MusicShare
import top.harumill.getto.bot.logger.GettoLogger
import top.harumill.getto.bot.logger.Output
import top.harumill.getto.bot.logger.Priority
import top.harumill.getto.data.musicRes.GettoMusic
import top.harumill.getto.data.musicRes.MusicInfo
import top.harumill.getto.function.Command
import java.net.URL
import java.net.URLEncoder

object SearchMusic : Command() {
    override suspend fun onAllEvent(event: Event) {
        //
    }

    private val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun searchMusic(name: String): MusicInfo? {
        val url = "http://8.131.255.9:4000/cloudsearch?keywords=${URLEncoder.encode(name, "UTF-8")}&type=1"

        val resp = URL(url).readText()

        val res = json.decodeFromString<GettoMusic>(resp)

        if (res.result.songs.isEmpty()) {
            return null
        }

        val music = res.result.songs.random()
        return MusicInfo(
            music.id,
            music.name,
            music.artists[0].name,
            music.album.picUrl
        )
    }

    override suspend fun onEnable(contact: Contact, sender: User) {
        val song = parameterList["song"]
        try {
            val music = song?.let { searchMusic(it) }
            if (music == null) {
                contact.sendMessage("暂时找不到 $song 这首歌哦，请更换关键词重新搜索试试")
            } else {
                contact.sendMessage(
                    MusicShare(
                        MusicKind.NeteaseCloudMusic,
                        music.title,
                        music.artist,
                        "https://y.music.163.com/m/song/${music.id}/?app_version=8.1.60",
                        music.picurl,
                        "https://music.163.com/song/media/outer/url?id=${music.id}"
                    )
                )
            }
        } catch (e: Exception) {
            GettoLogger.log(Priority.ERROR, "Exception:${e.message},Cause:${e.cause}", "Function $name")
            GettoLogger.log(Priority.ERROR, "Exception:${e.message},Cause:${e.cause}", "Function $name", Output.FILE)
            contact.sendMessage("搜歌功能出错了，请联系管理员")
        }

    }
}