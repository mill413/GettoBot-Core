package top.harumill.getto.tools

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

val JSONParser = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
    coerceInputValues = true
}

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified E> decodeFromJSONStr(json: String): E = JSONParser.decodeFromString(json)

inline fun <reified E> decodeFromJSONFile(filePath: String): E {
    val file = File(filePath)
    var str = ""
    file.readLines().forEach {
        str = str.plus(it)
    }
    return decodeFromJSONStr(str)
}