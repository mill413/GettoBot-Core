package top.harumill.getto.modules

import kotlinx.coroutines.delay
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.getMember
import net.mamoe.mirai.contact.getMemberOrFail
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.SingleMessage
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.toPlainText
import net.mamoe.mirai.utils.MiraiExperimentalApi
import top.harumill.getto.bot.Getto
import top.harumill.getto.tools.ModuleManager
import java.awt.GraphicsEnvironment
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BotManage(
    override val name: String = "BotManage",
    override val description: String = ""
) : Mod() {

    private var autoAddFriend:Boolean
        get() {
            return Getto.info.autoAddFriend
        }
        set(value) {
            Getto.info.autoAddFriend = value
            Getto.overwriteInfo(Getto.info)
        }
    private var autoAddGroup:Boolean
        get() {
            return Getto.info.autoAddGroup
        }
        set(value) {
            Getto.info.autoAddGroup = value
            Getto.overwriteInfo(Getto.info)
        }

    @MiraiExperimentalApi
    override suspend fun onEnable() {
        val startTime: LocalDateTime = LocalDateTime.now()

        /**
         * 管理员控制指令
         */
        gettoEventChannel.filter { it is MessageEvent && it.sender.id == Getto.info.administrator }
            .subscribeMessages {
                startsWith("自动加好友") { arg ->
                    when (arg) {
                        "on" -> {
                            autoAddFriend = true
                            subject.sendMessage(
                                PlainText("自动加好友功能${if (autoAddFriend) "开启" else "关闭"}")
                            )
                        }
                        "off" -> {
                            autoAddFriend = false
                            subject.sendMessage(
                                PlainText("自动加好友功能${if (autoAddFriend) "开启" else "关闭"}")
                            )
                        }
                        "check" -> {
                            subject.sendMessage(
                                PlainText("自动加好友功能${if (autoAddFriend) "开启" else "关闭"}")
                            )
                        }
                        else -> {
                            subject.sendMessage(
                                PlainText("参数错误")
                            )
                        }
                    }
                }
                startsWith("自动加群") { arg ->
                    when (arg) {
                        "on" -> {
                            autoAddGroup = true
                            subject.sendMessage(
                                PlainText("自动加群功能${if (autoAddGroup) "开启" else "关闭"}")
                            )
                        }
                        "off" -> {
                            autoAddGroup = false
                            subject.sendMessage(
                                PlainText("自动加群功能${if (autoAddGroup) "开启" else "关闭"}")
                            )
                        }
                        "check" -> {
                            subject.sendMessage(
                                PlainText("自动加群功能${if (autoAddGroup) "开启" else "关闭"}")
                            )
                        }
                        else -> {
                            subject.sendMessage(
                                PlainText("参数错误")
                            )
                        }
                    }
                }
                startsWith("广播") {
                    val content: MutableList<SingleMessage> = mutableListOf()
                    message.forEach {
                        if (it.contentToString().startsWith("广播")) {
                            content.add(it.content.removePrefix("广播").trim().toPlainText())
                        } else content.add(it)
                    }
                    var cnt = 0
                    var failed = 0
                    bot.groups.forEach {
                        try {
                            it.sendMessage(PlainText("[Getto 广播消息]\n") + content)
                        } catch (e: Exception) {
                            failed++
                        } finally {
                            cnt++
                        }
                    }
                    subject.sendMessage("共${cnt}个群,发送失败${failed}个群")
                }
                startsWith("回复") { target ->
                    val id = target.split(" ")[0].toLong()
                    val info = target.split(" ")[1]
                    bot.groups.forEach {
                        if (it.getMember(id) != null) {
                            val p = it.getMemberOrFail(id)
                            p.sendMessage("来自作者的消息\n${info}")
                            Getto.sendToAdministrator("已发送给${p.nick}(${p.id})")
                            return@startsWith
                        }
                    }
                }
                startsWith("重启间隔") { time ->
                    Getto.reloginTime = time.toLong()
                    subject.sendMessage("现在间隔${Getto.reloginTime}s重启")
                }
                startsWith("清退") { target ->
                    if (subject is Group){
                        (subject as Group).getMemberOrFail(target.removePrefix("@").toLong()).kick("gui")
                    }
                }
                startsWith("清理好友") { num ->
                    var cnt = 0
                    if (num.toLong() > bot.friends.size){
                        subject.sendMessage("只有${bot.friends.size}位好友了")
                    }
                    else{
                        bot.friends.forEach {
                            if (cnt < num.toLong() && it.id!=Getto.info.administrator){
                                it.delete()
                                cnt++
                            }
                        }
                        subject.sendMessage("已删除$cnt 位好友")
                    }
                }
                "status" {
                    val duration = Duration.between(startTime, LocalDateTime.now())
                    subject.sendMessage(
                        "好友数:${bot.friends.size}\n" +
                                "已加入群:${bot.groups.size}\n" +
                                "Java版本:${System.getProperties().getProperty("java.version")}\n" +
                                "操作系统名称:${System.getProperties().getProperty("os.name")}\n" +
                                "操作系统版本:${System.getProperties().getProperty("os.version")}\n" +
                                "上次登陆时间:${startTime.year}年${startTime.monthValue}月${startTime.dayOfMonth}日${startTime.hour}时${startTime.minute}分${startTime.second}秒\n" +
                                "已运行时间:${duration.toDaysPart()}天${duration.toHoursPart()}小时${duration.toMinutesPart()}分钟${duration.toSecondsPart()}秒\n" +
                                "掉线重启间隔:${Getto.reloginTime}s\n" +
                                "自动加好友:${if (autoAddFriend) "开启" else "关闭"}\n" +
                                "自动加群:${if (autoAddGroup) "开启" else "关闭"}"
                    )
                }
                "群列表" {
                    var groupListInfo = "${
                        DateTimeFormatter.ofPattern("截止yyyy/MM/dd hh:mm:ss").format(LocalDateTime.now())
                    }  已加入${bot.groups.size}群\n"
                    bot.groups.forEach {
                        groupListInfo += "${it.name}(${it.id})\n"
                    }
                    Getto.sendToAdministrator(groupListInfo)
                }
                "好友列表" {
                    var friendListInfo = "${
                        DateTimeFormatter.ofPattern("截止yyyy-MM-dd hh-mm-ss").format(LocalDateTime.now())
                    }  已有${bot.friends.size}好友\n"
                    bot.friends.forEach {
                        friendListInfo += "${it.nick}(${it.id})\n"
                    }
                    Getto.sendToAdministrator(friendListInfo)
                }
                "模块列表" {
                    var modListInfo = "目前有${ModuleManager.modList.size}个模块\n"
                    ModuleManager.modList.forEach {
                        modListInfo += "模块${it.name}:${it.description} ${if (it.switch) "on" else "off"}\n"
                    }
                    subject.sendMessage(modListInfo)
//                    Getto.sendToAdministrator(modListInfo)
                }
                "字体列表" {
                    val fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().availableFontFamilyNames
                    subject.sendMessage(fonts.toString())
                }
                "重启" {
                    Getto.sendToAdministrator("${Getto.reloginTime}s后重启")
                    Getto.bot.close()
                }
            }

        /**
         * 请求添加好友事件
         * 若 (自动加好友开关)[autoAddFriend] 开启，则自动接受请求
         * 否则需管理员私聊发送“ok”接受请求
         */
        gettoEventChannel.subscribeAlways<NewFriendRequestEvent> {
            if (autoAddFriend) accept()
            else {
                Getto.sendToAdministrator("${fromNick}(${fromId})请求添加好友")
                bot.eventChannel.subscribeAlways<FriendMessageEvent> {
                    if (sender.id == Getto.info.administrator) {
                        if (message.contentToString() == "ok") {
                            accept()
                        }
                    }
                }
            }
        }

        /**
         * 添加好友事件
         */
        gettoEventChannel.subscribeAlways<FriendAddEvent> {
            friend.sendMessage(
                "这里是月斗bot，暂时没有私聊的功能哦，如果需要查看最新功能指令表请输入help，如果有赞助作者的想法请输入\$，以下是来自作者的公告：\n"+
                    Getto.announcement)
            Getto.sendToAdministrator(PlainText("已添加${friend.nick}(${friend.id})为好友"))
        }

        /**
         * 邀请入群事件
         * 若 (自动加群开关)[autoAddGroup] 开启，则自动接受请求
         * 否则需管理员私聊发送“ok”接受请求
         */
        gettoEventChannel.subscribeAlways<BotInvitedJoinGroupRequestEvent> {
            if (autoAddGroup) {
                accept()
            }
            else {
                invitor?.sendMessage("暂时不接受邀请入群哦，请等待作者手动同意")
            }
        }

        /**
         * bot受邀入群事件
         */
        gettoEventChannel.subscribeAlways<BotJoinGroupEvent.Invite> {
            bot.getFriendOrFail(Getto.info.administrator)
                .sendMessage("接受${invitor.nick}(${invitor.id})邀请已加入群${group.name}(${group.id})")
            if (autoAddGroup){
                group.sendMessage("月斗已加入群聊，请输入#help获取最新指令功能表")
            }
            else{
                group.sendMessage("暂时不接受邀请入群，由于腾迅机制自动入群，即将退群")
                delay(1000)
                group.quit()
            }
        }

    }
}
