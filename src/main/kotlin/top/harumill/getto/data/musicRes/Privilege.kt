package top.harumill.getto.data.musicRes

import kotlinx.serialization.Serializable

@Serializable
data class Privilege(
    val chargeInfoList: List<ChargeInfo>?,
    val cp: Int?,
    val cs: Boolean?,
    val dl: Int?,
    val downloadMaxbr: Int?,
    val fee: Int?,
    val fl: Int?,
    val flag: Int?,
    val freeTrialPrivilege: FreeTrialPrivilege?,
    val id: Int?,
    val maxbr: Int?,
    val payed: Int?,
    val pl: Int?,
    val playMaxbr: Int?,
    val preSell: Boolean?,
    val rscl: String?,
    val sp: Int?,
    val st: Int?,
    val subp: Int?,
    val toast: Boolean
)