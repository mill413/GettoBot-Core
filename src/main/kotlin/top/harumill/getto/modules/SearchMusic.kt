package top.harumill.getto.modules

import kotlinx.coroutines.delay
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.data.MusicKind
import net.mamoe.mirai.message.data.MusicShare
import top.harumill.getto.data.musicRes.GettoMusic
import top.harumill.getto.data.musicRes.MusicInfo
import java.net.URL
import java.net.URLEncoder

class SearchMusic(
    override val name: String = "搜歌",
    override val description: String = ""
):Mod() {
    private fun searchMusic(name: String): MusicInfo? {
        val url = "http://8.131.255.9:4000/cloudsearch?keywords=${URLEncoder.encode(name,"UTF-8")}&type=1"

        val resp = URL(url).readText()

//        println(resp)

        val res = Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
        }.decodeFromString<GettoMusic>(resp)

//            println("${res.result.songs[0].id} ${res.result.songs[0].name} ${res.result.songs[0].ar[0].name}")

        if (res.result.songs.isNullOrEmpty()) {
            return null
        }

        return MusicInfo(
            res.result.songs[0].id,
            res.result.songs[0].name,
            res.result.songs[0].ar[0].name,
            res.result.songs[0].al.picUrl)
    }

    override suspend fun onEnable() {
        gettoEventChannel.subscribeGroupMessages {
            startsWith("搜歌") { song ->
                delay(2000)
                try {
                    val music = searchMusic(song)
                    if (music == null) {
                        group.sendMessage("暂时找不到 $song 这首歌哦，请更换关键词重新搜索试试")
                    } else {
                        group.sendMessage(
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
                    logOut(e.message!!)
                    e.printStackTrace()
                    group.sendMessage("啊哦，搜歌接口好像坏掉了，请联系作者查找bug")
                }
            }
        }
    }
}