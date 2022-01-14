package top.harumill.getto.data.cf

import kotlinx.serialization.Serializable

@Serializable
data class Contest(
    val id: Int,
    val name: String,
    val type: ContestType,
    val phase: ContestPhase,
    val frozen: Boolean,
    val durationSeconds: Int,

    val startTimeSeconds: Int = 0,
    val relativeTimeSeconds: Int = 0,
    val preparedBy: String = "",
    val websiteUrl: String = "",
    val description: String = "",
    val difficulty: String = "",
    val kind: String = "",
    val icpcRegion: String = "",
    val country: String = "",
    val city: String = "",
    val season: String = ""
)

@Serializable
enum class ContestType(value: String) {
    CF("CF"), IOI("IOI"), ICPC("ICPC")
}

enum class ContestPhase(value: String) {
    BEFORE("BEFORE"), CODING("CODING"), PENDING_SYSTEM_TEST("PENDING_SYSTEM_TEST"), SYSTEM_TEST("SYSTEM_TEST"), FINISHED(
        "FINISHED"
    )
}