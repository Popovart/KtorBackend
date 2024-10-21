package com.example

import com.example.DatabaseFactory.dbQuery
import com.example.model.AnswerVariant
import com.example.model.Question
import com.example.model.Quiz
import kotlinx.coroutines.flow.Flow
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*
import kotlin.math.exp


interface QuizDao {
    suspend fun getAllQuizzes(): List<Quiz>
    suspend fun getQuiz(quizId: UUID): Quiz?

    suspend fun addQuiz(
        title: String,
        shortDescription: String?,
        preview: String?
    ): Quiz?
    suspend fun editQuiz(
        quizId: UUID,
        title: String,
        shortDescription: String?,
        preview: String?
    ): Boolean

    suspend fun deleteQuiz(quizId: UUID): Boolean
}

interface QuestionDao {
    suspend fun getAllQuizQuestions(quizId: UUID): List<Question>
    suspend fun getQuestion(questionId: UUID): Question?

    suspend fun addQuestion(
        quizId: UUID,
        question: String,
        explanation: String?,
    ): Question?
    suspend fun editQuestion(
        questionId: UUID,
        quizId: UUID,
        question: String,
        explanation: String?,
    ): Boolean

    suspend fun deleteQuestion(questionId: UUID): Boolean
}

interface AnswerVariantDao {
    suspend fun getAllQuestionAnswerVariants(): List<AnswerVariant>
    suspend fun getAnswerVariant(answerId: UUID): AnswerVariant?

    suspend fun addAnswerVariant(
        questionId: UUID,
        text: String,
        isCorrect: Boolean = false
    ): AnswerVariant?
    suspend fun editAnswerVariant(
        answerId: UUID,
        questionId: UUID,
        text: String,
        isCorrect: Boolean,
    ): Boolean

    suspend fun deleteAnswerVariant(answerId: UUID): Boolean
}

class QuizDaoImpl : QuizDao, QuestionDao, AnswerVariantDao {
    override suspend fun getAllQuizzes(): List<Quiz> = dbQuery {
        Quizzes.selectAll().map(::resultRowToQuiz)
    }
    override suspend fun getQuiz(quizId: UUID): Quiz? = dbQuery {
        Quizzes.select {
            Quizzes.quizId eq quizId
        }
            .map(::resultRowToQuiz)
            .singleOrNull()
    }

    override suspend fun addQuiz(title: String, shortDescription: String?, preview: String?): Quiz? = dbQuery {
        val insertStatement = Quizzes.insert {
            it[Quizzes.title] = title
            it[Quizzes.shortDescription] = shortDescription
            it[Quizzes.preview] = preview
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToQuiz)
    }

    override suspend fun editQuiz(
        quizId: UUID,
        title: String,
        shortDescription: String?,
        preview: String?
    ): Boolean = dbQuery {
        Quizzes.update({Quizzes.quizId eq quizId}) {
            it[Quizzes.title] = title
            it[Quizzes.shortDescription] = shortDescription
            it[Quizzes.preview] = preview
        } > 0
    }

    override suspend fun deleteQuiz(quizId: UUID): Boolean = dbQuery {
        Quizzes.deleteWhere { Quizzes.quizId eq quizId } > 0
    }

    override suspend fun getAllQuizQuestions(quizId: UUID): List<Question> = dbQuery {
        Questions.selectAll().map(::resultRowToQuestion)
    }
    override suspend fun getQuestion(questionId: UUID): Question? = dbQuery {
        Questions.select {
            Questions.questionId eq questionId
        }
            .map(::resultRowToQuestion)
            .singleOrNull()
    }

    override suspend fun addQuestion(quizId: UUID, question: String, explanation: String?): Question? = dbQuery {
        val insertStatement = Questions.insert {
            it[Questions.quizId] = quizId
            it[Questions.question] = question
            it[Questions.explanation] = explanation
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToQuestion)
    }

    override suspend fun editQuestion(
        questionId: UUID,
        quizId: UUID,
        question: String,
        explanation: String?
    ): Boolean = dbQuery {
        Questions.update ({Questions.questionId eq questionId}) {
            it[Questions.quizId] = quizId
            it[Questions.question] = question
            it[Questions.explanation] = explanation
        } > 0
    }

    override suspend fun deleteQuestion(questionId: UUID): Boolean = dbQuery {
        Questions.deleteWhere { Questions.questionId eq questionId } > 0
    }

    override suspend fun getAllQuestionAnswerVariants(): List<AnswerVariant> = dbQuery {
        AnswerVariants.selectAll().map(::resultRowToAnswerVariant)
    }

    override suspend fun getAnswerVariant(answerId: UUID): AnswerVariant? = dbQuery {
        AnswerVariants.select {
            AnswerVariants.answerId eq answerId
        }
            .map(::resultRowToAnswerVariant)
            .singleOrNull()
    }

    override suspend fun addAnswerVariant(
        questionId: UUID,
        text: String,
        isCorrect: Boolean
    ): AnswerVariant? = dbQuery {
        val insertStatement = AnswerVariants.insert {
            it[AnswerVariants.questionId] = questionId
            it[AnswerVariants.text] = text
            it[AnswerVariants.isCorrect] = isCorrect
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToAnswerVariant)
    }

    override suspend fun editAnswerVariant(
        answerId: UUID,
        questionId: UUID,
        text: String,
        isCorrect: Boolean
    ): Boolean = dbQuery {
        AnswerVariants.update ({AnswerVariants.answerId eq answerId}) {
            it[AnswerVariants.answerId] = answerId
            it[AnswerVariants.questionId] = questionId
            it[AnswerVariants.text] = text
            it[AnswerVariants.isCorrect] = isCorrect
        } > 0
    }

    override suspend fun deleteAnswerVariant(answerId: UUID): Boolean = dbQuery {
        AnswerVariants.deleteWhere { AnswerVariants.answerId eq answerId } > 0
    }

    private fun resultRowToQuiz(row : ResultRow) = Quiz(
        quizId = row[Quizzes.quizId],
        title = row[Quizzes.title],
        shortDescription = row[Quizzes.shortDescription],
        preview = row[Quizzes.preview]
    )

    private fun resultRowToQuestion(row : ResultRow) = Question(
        questionId = row[Questions.questionId],
        quizId = row[Questions.quizId],
        question = row[Questions.question],
        explanation = row[Questions.explanation]
    )

    private fun resultRowToAnswerVariant(row: ResultRow) = AnswerVariant(
        answerId = row[AnswerVariants.answerId],
        questionId = row[AnswerVariants.questionId],
        text = row[AnswerVariants.text],
        isCorrect = row[AnswerVariants.isCorrect]
    )
}
