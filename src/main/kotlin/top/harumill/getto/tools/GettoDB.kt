package top.harumill.getto.tools

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

object GettoDB {
    private const val driver = "org.sqlite.JDBC"
    private const val db = "data/databases/bot.db"
    private const val url = "jdbc:sqlite:$db"

    private var conn: Connection
    var stat: Statement

    init {
        Class.forName(driver)
        conn = DriverManager.getConnection(url)
        stat = conn.createStatement()
    }

    fun query(op: String): ResultSet {
        stat = conn.createStatement()
        return stat.executeQuery(op)
    }

    fun update(op: String) {
        stat = conn.createStatement()
        stat.executeUpdate(op)
    }
}