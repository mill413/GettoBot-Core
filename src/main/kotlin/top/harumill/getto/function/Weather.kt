package top.harumill.getto.function

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User

object Weather : Command() {
    override suspend fun onEnable(contact: Contact, sender: User) {
        contact.sendMessage("不知道捏")
    }


}