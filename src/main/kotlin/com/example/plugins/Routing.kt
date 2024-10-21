package com.example.plugins

import com.example.AnswerVariantDao
import com.example.QuestionDao
import com.example.QuizDao
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Application.configureRouting(
    quizDao : QuizDao,
) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("/addquiz") {
            val formParameters = call.receiveParameters()
            val title = formParameters.getOrFail("title")
            val preview = formParameters.getOrFail("preview")
            val shortDescription = formParameters.getOrFail("short_description")
            val article = quizDao.addQuiz(title, shortDescription, preview)
            call.respond("Quiz added successfully")
        }

        get("/quizzes") {
            val listOfQuizzes = quizDao.getAllQuizzes()
            if (listOfQuizzes.isEmpty()){
                call.respond("list of quizzes are empty!")
            } else {
                call.respond(listOfQuizzes.toString())
            }
        }
    }
}
