package top.harumill.getto.function.functions

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import top.harumill.getto.function.Command

object Weather : Command() {
    override suspend fun onEnable(contact: Contact, sender: User) {
        contact.sendMessage("不知道捏")
    }


}