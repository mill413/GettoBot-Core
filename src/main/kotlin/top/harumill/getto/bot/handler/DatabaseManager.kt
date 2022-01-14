package top.harumill.getto.bot.handler

import org.ktorm.database.Database
import org.ktorm.dsl.insert
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

// TODO-需要了再写
object DatabaseManager {
    private val database = Database.connect(
        url = "jdbc:sqlite:data/GettoTest.sqlite",
        driver = "org.sqlite.JDBC"
    )

    object Test : Table<Nothing>("test") {
        val id = int("id").primaryKey()
        val name = varchar("name")
    }

    fun doSomething() {
        database.insert(Test) {
            set(Test.id, 12)
            set(Test.name, "李田所")
        }
    }
}