package top.harumill.getto.tools

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
        sc.init(null, trustAllCerts, java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory);
    } catch (e: Exception) {
        e.printStackTrace();
    }
}

fun getResp(url: URL, charset: Charset = Charsets.UTF_8): String {
    trustAllHosts()
    val conn =
        if (url.protocol.lowercase() == "https") {
            val https = (url.openConnection() as HttpsURLConnection)
            https.hostnameVerifier = HostnameVerifier { _, _ -> true }
            https
        } else url.openConnection()

    return conn.getInputStream().readBytes().toString(charset)
}
