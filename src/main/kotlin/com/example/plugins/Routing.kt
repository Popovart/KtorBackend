package com.example.plugins

import com.example.QuizDaoImpl
import com.example.model.quizModel.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import java.util.*

fun Application.configureRouting(
    dao : QuizDaoImpl,
) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        // get all quizzes
        get("/quizzes") {
            val listOfQuizzes = dao.getAllQuizzes()
            if (listOfQuizzes.isEmpty()){
                call.respond("There are no quizzes!")
            } else {
                call.respond(listOfQuizzes.toString())
            }
        }

        // get a specific quiz by ID
        get("/quizzes/{quizId}") {
            val quizId = UUID.fromString(call.parameters["quizId"])
            val quiz = dao.getQuiz(quizId)
            if (quiz == null) {
                call.respond("Quiz has not been found :(")
            } else
            {
                call.respond("Quiz has been retrieved :)")
            }
        }

        // create quiz
        post("/quizzes") {
            val quizRequest  = call.receive<QuizRequest>()
            dao.createQuiz(quizRequest)
            call.respond("Quiz has been added successfully")
        }

        /* update quiz
        * (What if quiz to update has not been found?
        * Do I need to make POST request or this must be handled on a client side?)
        */
        put("/quizzes") {
            val quizDTO = call.receive<QuizDTO>()
            dao.updateQuiz(quizDTO)
            call.respond("Quiz has been updated successfully")
        }

        // delete quiz
        delete("/quizzes/{quizId}") {
            val quizId = UUID.fromString(call.parameters["quizId"])
            dao.deleteQuiz(quizId)
            call.respond("Quiz has been deleted successfully")
        }

        // get all quiz questions
        get("/quizzes/{quizId}/questions") {
            val quizId = UUID.fromString(call.parameters["quizId"])
            dao.getAllQuizQuestions(quizId)
            call.respond("Quiz questions has been retrieved successfully :)")
        }

        // get question with specific questionId
        get("/questions/{questionId}") {
            val questionId = UUID.fromString(call.parameters["questionId"])
            val question = dao.getQuestion(questionId)
            if (question == null) {
                call.respond("Question has not been found :(")
            } else
            {
                call.respond("Question has been retrieved :)")
            }
        }

        // create question
        post("/questions") {
            val questionRequest = call.receive<QuestionRequest>()
            dao.createQuestion(questionRequest)
            call.respond("Question has been added successfully :)")
        }

        // update question
        put("/questions") {
            val questionDTO = call.receive<QuestionDTO>()
            dao.updateQuestion(questionDTO)
            call.respond("Question has been updated successfully :)")
        }

        // delete question
        delete("/questions/{questionId}") {
            val questionId = UUID.fromString(call.parameters["questionId"])
            dao.deleteQuestion(questionId)
            call.respond("Question has been deleted successfully :)")
        }

        // get question answer variants
        get("/questions/{questionId}/answerVariants") {
            val questionId = UUID.fromString(call.parameters["questionId"])
            dao.getAllQuestionAnswerVariants(questionId)
            call.respond("Answer variants has been retrieved successfully :)")
        }

        // get answer variant
        get("/answerVariants/{answerId}") {
            val answerId = UUID.fromString(call.parameters["answerId"])
            val answerVariant = dao.getAnswerVariant(answerId)
            if (answerVariant == null) {
                call.respond("Answer Variant has not been found :(")
            } else
            {
                call.respond("Question has been retrieved :)")
            }
        }

        // create answerVariant
        post("/answerVariant") {
            val answerVariantRequest = call.receive<AnswerVariantRequest>()
            dao.createAnswerVariant(answerVariantRequest)
            call.respond("Answer Variant has been added successfully :)")
        }

        // update question
        put("/answerVariant") {
            val answerVariantDTO = call.receive<AnswerVariantDTO>()
            dao.updateAnswerVariant(answerVariantDTO)
            call.respond("Answer Variant has been updated successfully :)")
        }

        // delete question
        delete("/answerVariant/{answerId}") {
            val answerId = UUID.fromString(call.parameters["answerId"])
            dao.deleteQuestion(answerId)
            call.respond("Answer Variant has been deleted successfully :)")
        }


    }
}
