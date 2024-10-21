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
        val database =  Database.connect(
            url = "jdbc:postgresql://localhost:5432/QuizzesApp",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "3312"
        )
        transaction (database) {
            SchemaUtils.create(Quizzes)
        }


    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }


}



fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val dao: QuizDao = QuizDaoImpl()
    DatabaseFactory.init()

    configureSerialization()
    configureRouting(dao)


}
