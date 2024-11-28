package com.example

import com.example.Quizzes.autoGenerate
import com.example.Quizzes.quizId
import com.example.plugins.*
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*




object DatabaseFactory {
    fun init() {
        val databaseUrl = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/QuizzesApp"
        val user = System.getenv("DATABASE_USER") ?: "postgres"
        val password = System.getenv("DATABASE_PASSWORD") ?: "3312"

        var connected = false
        while (!connected) {
            try {
                val database = Database.connect(
                    url = databaseUrl,
                    driver = "org.postgresql.Driver",
                    user = user,
                    password = password
                )
                println("Successfully connected to the database!")
                connected = true
                transaction(database) {
                    SchemaUtils.create(Quizzes, Questions, AnswerVariants)
                }
            } catch (e: Exception) {
                println("Database connection failed: ${e.message}")
                println("Retrying in 2 seconds...")
                Thread.sleep(2000)
            }
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}




fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val dao = QuizDaoImpl()
    DatabaseFactory.init()

    configureSerialization()
    configureRouting(dao)


}
