package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

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
        suspend fun <T> dbQuery(block: suspend () -> T): T =
            newSuspendedTransaction(Dispatchers.IO) { block() }
    }


}

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
