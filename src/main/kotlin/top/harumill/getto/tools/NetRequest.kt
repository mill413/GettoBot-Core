package top.harumill.getto.tools

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.utils.io.charsets.*
import java.net.URL
import java.nio.charset.Charset
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

fun buildURL(url: String) = URL(url)
fun buildURL(
    protocol: String = "https",
    hostname: String,
    port: Int = 80,
    path: String = "/",
    parameters: List<Pair<String, String>> = listOf()
) = buildURL("${protocol}://${hostname}${if (port != 80) ":${port}" else ""}${path}?${
    with(parameters) {
        var params = ""
        for (pair in parameters) {
            params += "${pair.first}=${pair.second}&"
        }
        params
    }
}")

fun trustAllHosts() {
    val trustAllCerts = arrayOf<TrustManager>(
        object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {

            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {

            }
        }
    )
    try {
        val sc = SSLContext.getInstance("TLS")
        sc.init(null, trustAllCerts, java.security.SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun getRespByURL(url: URL, charset: Charset = Charsets.UTF_8): String {
    trustAllHosts()
    val conn =
        if (url.protocol.lowercase() == "https") {
            val https = (url.openConnection() as HttpsURLConnection)
            https.hostnameVerifier = HostnameVerifier { _, _ -> true }
            https
        } else url.openConnection()

    return conn.getInputStream().readBytes().toString(charset)
}

/**
 * 向api发送get请求，获取返回的JSON数据并解析
 * 使用Ktor提供的[HttpClient]，解析后会立即关闭
 *
 * @param url api的url
 * @param block
 *
 * @return 返回解析的数据
 */
suspend inline fun <reified T> getJsonRespFromApi(url: String, block: HttpRequestBuilder.() -> Unit = {}) =
    HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(JSONParser)
        }
    }.use {
        it.get<T>(url, block)
    }

suspend inline fun <reified T> getJsonRespFromApi(
    protocol: String = "https",
    hostname: String,
    port: Int = 80,
    path: String = "/",
    parameters: List<Pair<String, String>> = listOf(),
    block: HttpRequestBuilder.() -> Unit = {}
) = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = KotlinxSerializer(JSONParser)
    }
}.use {
    it.get<T>(buildURL(protocol, hostname, port, path, parameters), block)
}