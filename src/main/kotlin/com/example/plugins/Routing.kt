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
            try {
                call.respondText("Hello World!")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

        get("/quizzes") {
            try {
                val listOfQuizzes = dao.getAllQuizzes()
                if (listOfQuizzes.isEmpty()) {
                    call.respond("There are no quizzes!")
                } else {
                    call.respond(listOfQuizzes)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

        get("/quizzes/{quizId}") {
            try {
                val quizIdParam = call.parameters["quizId"]
                if (quizIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, "Missing or invalid quizId")
                    return@get
                }
                val quiz = dao.getQuiz(UUID.fromString(quizIdParam))
                if (quiz == null) {
                    call.respond("Quiz has not been found :(")
                } else {
                    call.respond(quiz)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

        post("/quizzes") {
            try {
                val quizRequest = call.receive<QuizRequest>()
                val quiz = dao.createQuiz(quizRequest)
                if (quiz == null) {
                    call.respond("Quiz hasn't been added :(")
                } else {
                    call.respond(quiz)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

        put("/quizzes") {
            try {
                val quizDTO = call.receive<QuizDTO>()
                val isQuizUpdated = dao.updateQuiz(quizDTO)
                if (!isQuizUpdated) {
                    call.respond("Quiz hasn't been updated :(")
                } else {
                    call.respond("Quiz has been updated successfully :)")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

        delete("/quizzes/{quizId}") {
            try {
                val quizIdParam = call.parameters["quizId"]
                if (quizIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, "Missing or invalid quizId")
                    return@delete
                }
                val isQuizDeleted = dao.deleteQuiz(UUID.fromString(quizIdParam))
                if (!isQuizDeleted) {
                    call.respond("Quiz hasn't been deleted :(")
                } else {
                    call.respond("Quiz has been deleted successfully :)")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

        // get all quiz questions
        get("/quizzes/{quizId}/questions") {
            try {
                val quizIdParam = call.parameters["quizId"]
                if (quizIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, "Missing or invalid quizId")
                    return@get
                }
                val quizQuestions = dao.getAllQuizQuestions(UUID.fromString(quizIdParam))
                if (quizQuestions.isEmpty()){
                    call.respond("There are no questions in this quiz!")
                } else {
                    call.respond(quizQuestions)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }

        }

        // get question with specific questionId
        get("/questions/{questionId}") {
            try {
                val questionIdParam = call.parameters["questionId"]
                if (questionIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, "Missing or invalid questionId")
                    return@get
                }
                val question = dao.getQuestion(UUID.fromString(questionIdParam))
                if (question == null)
                    call.respond("Question has not been found :(")
                else
                    call.respond(question)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }

        }

        // create question
        post("/questions") {

            try {
                val questionRequest = call.receive<QuestionRequest>()
                val question = dao.createQuestion(questionRequest)
                if (question == null)
                    call.respond("Question hasn't been added :(")
                else
                    call.respond(question)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

        // update question
        put("/questions") {
            try {
                val questionDTO = call.receive<QuestionDTO>()
                val isQuestionUpdated = dao.updateQuestion(questionDTO)
                if (isQuestionUpdated)
                    call.respond("Question hasn't been updated :(")
                else
                    call.respond("Question has been updated successfully :)")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }

        }

        // delete question
        delete("/questions/{questionId}") {
            try {
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
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }

        }

        // get question answer variants
        get("/questions/{questionId}/answerVariants") {
            try {
                val questionIdParam = call.parameters["questionId"]
                if (questionIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, "Missing or invalid questionId")
                    return@get
                }
                val answerVariants = dao.getAllQuestionAnswerVariants(UUID.fromString(questionIdParam))
                if (answerVariants.isEmpty())
                    call.respond("There are no answer variants for this question :(")
                else
                    call.respond(answerVariants)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }

        }

        // get answer variant
        get("/answerVariants/{answerId}") {
            try {
                val answerIdParam = call.parameters["answerId"]
                if (answerIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, "Missing or invalid answerId")
                }
                val answerVariant = dao.getAnswerVariant(UUID.fromString(answerIdParam))
                if (answerVariant == null)
                    call.respond("Answer variant has not been found :(")
                else
                    call.respond(answerVariant)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }

        // create answerVariant
        post("/answerVariant") {
            try {
                val answerVariantRequest = call.receive<AnswerVariantRequest>()
                val answerVariant = dao.createAnswerVariant(answerVariantRequest)
                if (answerVariant == null)
                    call.respond("Answer variant hasn't been added :(")
                else
                    call.respond(answerVariant)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }

        }

        // update question
        put("/answerVariant") {
            try {
                val answerVariantDTO = call.receive<AnswerVariantDTO>()
                val isAnswerVariantUpdated = dao.updateAnswerVariant(answerVariantDTO)
                if (isAnswerVariantUpdated)
                    call.respond("Answer variant hasn't been updated :(")
                else
                    call.respond("Answer variant has been updated successfully :)")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }

        }

        // delete question
        delete("/answerVariant/{answerId}") {
            try {
                val answerIdParam = call.parameters["answerId"]
                if (answerIdParam == null) {
                    call.respond(HttpStatusCode.BadRequest, "Missing or invalid answerId")
                }
                val isAnswerVariantDeleted = dao.deleteQuestion(UUID.fromString(answerIdParam))
                if (isAnswerVariantDeleted)
                    call.respond("Answer variant hasn't been deleted :(")
                else
                    call.respond("Answer variant has been deleted successfully :)")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }

        }


    }
}
