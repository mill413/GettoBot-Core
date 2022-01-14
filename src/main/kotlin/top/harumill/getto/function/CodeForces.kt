package top.harumill.getto.function

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import top.harumill.getto.data.cf.CFContestList
import top.harumill.getto.data.cf.Contest
import top.harumill.getto.data.cf.ContestPhase
import top.harumill.getto.tools.buildURL
import top.harumill.getto.tools.decodeFromJSONStr
import top.harumill.getto.tools.getResp
import top.harumill.getto.tools.timeStampToDateTime
import kotlin.math.min

object CodeForces : Command() {

    private fun getContestList(): MutableList<Contest> {
        val resp = getResp(
            buildURL(
                hostname = "codeforces.com",
                path = "/api/contest.list",
                parameters = listOf(Pair("gym", "false"))
            )
        )

        val list = decodeFromJSONStr<CFContestList>(resp)
        val result = mutableListOf<Contest>()
        list.result.forEach { contest -> if (contest.phase != ContestPhase.FINISHED) result.add(contest) }

        return result
    }


    override suspend fun onEnable(contact: Contact, sender: User) {
        val arg = parameterList["num"]
        try {
            val contests = getContestList()
            var contestStr = ""
            arg?.let { min(it.toInt(), contests.size) }?.let {
                repeat(it) { index ->
                    val contest = contests[index]
                    contestStr += "${index + 1}\n${contest.name}\n\t开始时间:${
                        timeStampToDateTime(
                            format = "MM-dd HH:mm:ss",
                            timestamp = contest.startTimeSeconds.toLong()
                        )
                    }\n"
                }
            }
            contact.sendMessage(contestStr)
        } catch (e: NumberFormatException) {
            contact.sendMessage("请输入一个整数")
        }
    }
}