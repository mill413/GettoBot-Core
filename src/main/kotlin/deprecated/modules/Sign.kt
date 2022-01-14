package deprecated.modules

import deprecated.bot.Getto
import deprecated.tools.GettoDB
import kotlinx.coroutines.delay
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.data.At
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Sign(
    override val name: String = "签到",
    override val description: String = ""
) : Mod() {
    override suspend fun onEnable() {
        gettoEventChannel.subscribeGroupMessages {

            "签到" reply {
                delay(Getto.delayTime)
                At(sender) + sign(sender.id)
            }
        }
    }

    private fun sign(id: Long): String {
        val localDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now())
        val query = "select * from sign where id=${id}"

        val res = GettoDB.query(query)

        val credit: Long

        val timeStamp = (System.currentTimeMillis() / 1000)
        val randomNo = ((timeStamp.toString().random()).code - '0'.code)
        val getCredit = (10..randomNo + 10).random()
        while (res.next()) {
            credit = res.getLong("credit")

            return if (res.getString("lastdate").toString() == localDate) {
                "你今天已经签过到了，你目前的积分为${credit}"
            } else {
                val update = "update sign set credit=${credit + getCredit},lastdate='${localDate}' where id=${id}"
                GettoDB.update(update)
                "签到成功，你获得了${getCredit}点积分，你现在的积分为${credit + getCredit}"
            }
        }
        val insert = "insert into sign (id,credit,lastdate) values(${id},${getCredit},'${localDate}')"
        GettoDB.update(insert)

        GettoDB.stat.close()

        return "这是你第一次签到，你获得了${getCredit}点积分"
    }
}