package com.example.plugins

import com.example.QuizDaoImpl
import com.example.model.quizModel.*
import io.ktor.http.*
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
            val quizIdParam = call.parameters["quizId"]
            if (quizIdParam == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid quizId")
                return@get
            }
            val quiz = dao.getQuiz(UUID.fromString(quizIdParam))
            if (quiz == null)
                call.respond("Quiz has not been found :(")
            else
                call.respond(quiz.toString())
        }

        // create quiz
        post("/quizzes") {
            val quizRequest  = call.receive<QuizRequest>()
            val quiz = dao.createQuiz(quizRequest)
            if (quiz == null)
                call.respond("Quiz hasn't been added :(")
            else
                call.respond(quiz.toString())
        }

        /* update quiz
        * (What if quiz to update has not been found?
        * Do I need to make POST request or this must be handled on a client side?)
        */
        put("/quizzes") {
            val quizDTO = call.receive<QuizDTO>()
            val isQuizUpdated = dao.updateQuiz(quizDTO)
            if (isQuizUpdated)
                call.respond("Quiz hasn't been updated :(")
            else
                call.respond("Quiz has been updated successfully :)")
        }

        // delete quiz
        delete("/quizzes/{quizId}") {
            val quizIdParam = call.parameters["quizId"]
            if (quizIdParam == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid quizId")
                return@delete
            }
            val isQuizDeleted = dao.deleteQuiz(UUID.fromString(quizIdParam))
            if (isQuizDeleted)
                call.respond("Quiz hasn't been deleted :(")
            else
                call.respond("Quiz has been deleted successfully :)")
        }

        // get all quiz questions
        get("/quizzes/{quizId}/questions") {
            val quizIdParam = call.parameters["quizId"]
            if (quizIdParam == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid quizId")
                return@get
            }
            val quizQuestions = dao.getAllQuizQuestions(UUID.fromString(quizIdParam))
            if (quizQuestions.isEmpty()){
                call.respond("There are no questions in this quiz!")
            } else {
                call.respond(quizQuestions.toString())
            }
        }

        // get question with specific questionId
        get("/questions/{questionId}") {
            val questionIdParam = call.parameters["questionId"]
            if (questionIdParam == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid questionId")
                return@get
            }
            val question = dao.getQuestion(UUID.fromString(questionIdParam))
            if (question == null)
                call.respond("Question has not been found :(")
            else
                call.respond(question.toString())
        }

        // create question
        post("/questions") {
            val questionRequest = call.receive<QuestionRequest>()
            val question = dao.createQuestion(questionRequest)
            if (question == null)
                call.respond("Question hasn't been added :(")
            else
                call.respond(question.toString())
        }

        // update question
        put("/questions") {
            val questionDTO = call.receive<QuestionDTO>()
            val isQuestionUpdated = dao.updateQuestion(questionDTO)
            if (isQuestionUpdated)
                call.respond("Question hasn't been updated :(")
            else
                call.respond("Question has been updated successfully :)")
        }

        // delete question
        delete("/questions/{questionId}") {
            val questionIdParam = call.parameters["questionId"]
            if (questionIdParam == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid questionId")
                return@delete
            }
            val isQuestionDeleted = dao.deleteQuestion(UUID.fromString(questionIdParam))
            if (isQuestionDeleted)
                call.respond("Question hasn't been deleted :(")
            else
                call.respond("Question has been deleted successfully :)")
        }

        // get question answer variants
        get("/questions/{questionId}/answerVariants") {
            val questionIdParam = call.parameters["questionId"]
            if (questionIdParam == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid questionId")
                return@get
            }
            val answerVariants = dao.getAllQuestionAnswerVariants(UUID.fromString(questionIdParam))
            if (answerVariants.isEmpty())
                call.respond("There are no answer variants for this question :(")
            else
                call.respond(answerVariants.toString())
        }

        // get answer variant
        get("/answerVariants/{answerId}") {
            val answerIdParam = call.parameters["answerId"]
            if (answerIdParam == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid answerId")
            }
            val answerVariant = dao.getAnswerVariant(UUID.fromString(answerIdParam))
            if (answerVariant == null)
                call.respond("Answer variant has not been found :(")
            else
                call.respond(answerVariant.toString())
        }

        // create answerVariant
        post("/answerVariant") {
            val answerVariantRequest = call.receive<AnswerVariantRequest>()
            val answerVariant = dao.createAnswerVariant(answerVariantRequest)
            if (answerVariant == null)
                call.respond("Answer variant hasn't been added :(")
            else
                call.respond(answerVariant.toString())
        }

        // update question
        put("/answerVariant") {
            val answerVariantDTO = call.receive<AnswerVariantDTO>()
            val isAnswerVariantUpdated = dao.updateAnswerVariant(answerVariantDTO)
            if (isAnswerVariantUpdated)
                call.respond("Answer variant hasn't been updated :(")
            else
                call.respond("Answer variant has been updated successfully :)")
        }

        // delete question
        delete("/answerVariant/{answerId}") {
            val answerIdParam = call.parameters["answerId"]
            if (answerIdParam == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid answerId")
            }
            val isAnswerVariantDeleted = dao.deleteQuestion(UUID.fromString(answerIdParam))
            if (isAnswerVariantDeleted)
                call.respond("Answer variant hasn't been deleted :(")
            else
                call.respond("Answer variant has been deleted successfully :)")
        }


    }
}
