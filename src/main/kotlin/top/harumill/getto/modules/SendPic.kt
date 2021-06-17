package top.harumill.getto.modules

import kotlinx.coroutines.delay
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsVoice
import top.harumill.getto.bot.Getto
 
import top.harumill.getto.data.Dir
import java.io.File

class SendPic(
    override val name: String = "RandomPic",
    override val description: String = "随机发图"
) : Mod() {
    var files = mutableListOf<String>()

    override suspend fun onEnable() {
        gettoEventChannel.subscribeGroupMessages {
            startsWith("随机") { target ->
                delay(2000)
                when (target) {
                    "猫猫" -> {
                        files = Getto.Utils.getImgList(
                            Dir.cats
                        )
                        val cat = files.random()
                        if (cat.startsWith("dog")) {
                            group.sendMessage(
                                PlainText("恭喜${sender.nameCard}随机出一只狗狗") + group.uploadImage(
                                    File(

                                        Dir.cats + cat
                                    ).toExternalResource()
                                )
                            )
                        } else {
                            group.sendImage(
                                File(
                                    Dir.cats + cat
                                )
                            )
                        }
                    }
                    "傻狗" -> {
                        files = Getto.Utils.getImgList(
                            Dir.sbDog
                        )
                        val dog = files.random()
                        group.sendImage(
                            File(
                                Dir.sbDog + dog
                            )
                        )
                    }
//                    "头像" -> {
//                        group.sendMessage("该功能暂时下线")
////                        val ava = Getto.Utils.downloadFile(
////                            url = bot.groups.random().members.random().avatarUrl,
////                            fileName = "tmp",
////                            path = Dir.img
////                        )
////                        group.sendImage(ava)
////                        ava.deleteRecursively()
//                    }
                }
            }
            startsWith("#") {
                val args = it.split(" ")
                delay(5000)
                when (args[0]) {
                    "help" -> {
                        group.sendImage(
                            File(
                                "${
                                    Dir.img
                                }help.png"
                            )
                        )
                    }
                    "pcr" -> {
                        when (args[1]) {
                            "comic" -> {
                                files = Getto.Utils.getImgList(
                                    Dir.pcrComic
                                )
                                if (args.size < 3) {
                                    group.sendMessage("已更新到${files.size - 1}话,请输入相应话数，如\"#pcr comic 2\"")
                                } else {
                                    val fileName = "episode_${args[2]}.png"
                                    if (fileName !in files) {
                                        group.sendMessage("没有你要找的")
                                    } else {
                                        group.sendImage(
                                            File(
                                                Dir.pcrComic + fileName
                                            )
                                        )
                                    }
                                }
                            }
                            "stamp" -> {
                                files = Getto.Utils.getImgList(
                                    Dir.pcrStamp
                                )
                                val fileName =
                                    if (args.size < 3)
                                        files.random()
                                    else
                                        args[2] + ".png"

                                if (fileName !in files) {
                                    group.sendMessage("没有你要找的")
                                } else {
                                    group.sendImage(
                                        File(
                                            Dir.pcrStamp + fileName
                                        )
                                    )
                                }
                            }
                            "card" -> {
                                group.sendMessage(At(sender) + PlainText("图片较大，请稍等"))
                                files = Getto.Utils.getImgList(
                                    Dir.pcrCard
                                )
                                group.sendMessage(
                                    At(sender) + group.uploadImage(
                                        File(
                                            Dir.pcrCard + files.random()
                                        ).toExternalResource()
                                    )
                                )
                            }
                            "gacha" -> {
                                when (args[2]) {
                                    "1" -> {
                                        group.sendMessage(
                                            File(
                                                "${
                                                    Dir.voice
                                                }gacha_1.mp3.silk"
                                            ).toExternalResource()
                                                .uploadAsVoice(group)
                                        )
                                    }
                                    "10" -> {
                                        group.sendMessage(
                                            File(
                                                "${
                                                    Dir.voice
                                                }gacha_10.mp3.silk"
                                            ).toExternalResource()
                                                .uploadAsVoice(group)
                                        )
                                    }
                                    else -> {
                                        group.sendMessage("要么单抽要么十连")
                                    }
                                }
                            }
                        }
                    }
                    "bang" -> {
                        when (args[1]) {
                            "bg" -> {
                                group.sendMessage(At(sender) + PlainText("图片较大，请稍等"))
                                files = Getto.Utils.getImgList(
                                    Dir.bang
                                )
                                group.sendMessage(
                                    At(sender) + group.uploadImage(
                                        File(
                                            Dir.bang + files.random()
                                        ).toExternalResource()
                                    )
                                )
                            }
                        }
                    }
                }
            }
            "悄悄看" {
                files = Getto.Utils.getImgList(Dir.look)
                group.sendImage(File(Dir.look + files.random()))
            }
        }
    }

}