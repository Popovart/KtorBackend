package com.example

import com.example.DatabaseFactory.dbQuery
import com.example.model.quizModel.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.util.*


interface QuizDao {
    suspend fun getAllQuizzes(): List<QuizDTO>
    suspend fun getQuiz(quizId: UUID): QuizDTO?

    suspend fun createQuiz(quizRequest: QuizRequest): QuizDTO?

    suspend fun updateQuiz(quizDTO: QuizDTO) : Boolean

    suspend fun deleteQuiz(quizId: UUID): Boolean
}

interface QuestionDao {
    suspend fun getAllQuizQuestions(quizId: UUID): List<QuestionDTO>
    suspend fun getQuestion(questionId: UUID): QuestionDTO?

    suspend fun createQuestion(questionRequest: QuestionRequest): QuestionDTO?
    suspend fun updateQuestion(questionDTO: QuestionDTO): Boolean
    suspend fun deleteQuestion(questionId: UUID): Boolean
}

interface AnswerVariantDao {
    suspend fun getAllQuestionAnswerVariants(questionId: UUID): List<AnswerVariantDTO>
    suspend fun getAnswerVariant(answerId: UUID): AnswerVariantDTO?

    suspend fun createAnswerVariant(answerVariantRequest: AnswerVariantRequest): AnswerVariantDTO?
    suspend fun updateAnswerVariant(answerVariantDTO: AnswerVariantDTO): Boolean

    suspend fun deleteAnswerVariant(answerId: UUID): Boolean
}

class QuizDaoImpl : QuizDao, QuestionDao, AnswerVariantDao {
    override suspend fun getAllQuizzes(): List<QuizDTO> = dbQuery {
        Quizzes.selectAll().map(::resultRowToQuiz)
    }
    override suspend fun getQuiz(quizId: UUID): QuizDTO? = dbQuery {
        Quizzes.select {
            Quizzes.quizId eq quizId
        }
            .map(::resultRowToQuiz)
            .singleOrNull()
    }

    override suspend fun createQuiz(quizRequest: QuizRequest): QuizDTO? = dbQuery {
        val insertStatement = Quizzes.insert {
            it[title] = quizRequest.title
            it[shortDescription] = quizRequest.shortDescription
            it[preview] = quizRequest.preview
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToQuiz)
    }

    override suspend fun updateQuiz(quizDTO: QuizDTO): Boolean = dbQuery {
        Quizzes.update({ Quizzes.quizId eq quizDTO.quizId }) {
            it[title] = quizDTO.title
            it[shortDescription] = quizDTO.shortDescription
            it[preview] = quizDTO.preview
        } > 0
    }

    override suspend fun deleteQuiz(quizId: UUID): Boolean = dbQuery {
        Quizzes.deleteWhere { Quizzes.quizId eq quizId } > 0
    }

    override suspend fun getAllQuizQuestions(quizId: UUID): List<QuestionDTO> = dbQuery {
        Questions.select { Questions.quizId eq quizId }.map(::resultRowToQuestion)
    }
    override suspend fun getQuestion(questionId: UUID): QuestionDTO? = dbQuery {
        Questions.select {
            Questions.questionId eq questionId
        }
            .map(::resultRowToQuestion)
            .singleOrNull()
    }

    override suspend fun createQuestion(questionRequest: QuestionRequest): QuestionDTO? = dbQuery {
        val insertStatement = Questions.insert {
            it[quizId] = questionRequest.quizId
            it[question] = questionRequest.question
            it[explanation] = questionRequest.explanation
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToQuestion)
    }

    override suspend fun updateQuestion(questionDTO: QuestionDTO): Boolean = dbQuery {
        Questions.update({ Questions.questionId eq questionDTO.questionId }) {
            it[quizId] = questionDTO.quizId
            it[question] = questionDTO.question
            it[explanation] = questionDTO.explanation
        } > 0
    }

    override suspend fun deleteQuestion(questionId: UUID): Boolean = dbQuery {
        Questions.deleteWhere { Questions.questionId eq questionId } > 0
    }

    override suspend fun getAllQuestionAnswerVariants(questionId: UUID): List<AnswerVariantDTO> = dbQuery {
        Questions.select { Questions.questionId eq questionId }.map(::resultRowToAnswerVariant)
    }

    override suspend fun getAnswerVariant(answerId: UUID): AnswerVariantDTO? = dbQuery {
        AnswerVariants.select {
            AnswerVariants.answerId eq answerId
        }
            .map(::resultRowToAnswerVariant)
            .singleOrNull()
    }

    override suspend fun createAnswerVariant(
        answerVariantRequest: AnswerVariantRequest
    ): AnswerVariantDTO? = dbQuery {
        val insertStatement = AnswerVariants.insert {
            it[questionId] = answerVariantRequest.questionId
            it[text] = answerVariantRequest.text
            it[isCorrect] = answerVariantRequest.isCorrect
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToAnswerVariant)
    }

    override suspend fun updateAnswerVariant(answerVariantDTO: AnswerVariantDTO): Boolean = dbQuery {
        AnswerVariants.update({ AnswerVariants.answerId eq answerVariantDTO.answerId }) {
            it[questionId] = answerVariantDTO.questionId
            it[text] = answerVariantDTO.text
            it[isCorrect] = answerVariantDTO.isCorrect
        } > 0
    }

    override suspend fun deleteAnswerVariant(answerId: UUID): Boolean = dbQuery {
        AnswerVariants.deleteWhere { AnswerVariants.answerId eq answerId } > 0
    }

    private fun resultRowToQuiz(row : ResultRow) = QuizDTO(
        quizId = row[Quizzes.quizId],
        title = row[Quizzes.title],
        shortDescription = row[Quizzes.shortDescription],
        preview = row[Quizzes.preview]
    )

    private fun resultRowToQuestion(row : ResultRow) = QuestionDTO(
        questionId = row[Questions.questionId],
        quizId = row[Questions.quizId],
        question = row[Questions.question],
        explanation = row[Questions.explanation]
    )

    private fun resultRowToAnswerVariant(row: ResultRow) = AnswerVariantDTO(
        answerId = row[AnswerVariants.answerId],
        questionId = row[AnswerVariants.questionId],
        text = row[AnswerVariants.text],
        isCorrect = row[AnswerVariants.isCorrect]
    )
}

