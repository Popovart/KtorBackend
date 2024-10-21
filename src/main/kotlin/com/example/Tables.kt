package com.example

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Quizzes : Table() {
    val quizId = uuid("quiz_id").autoGenerate()
    val title = varchar("title", 128)
    val shortDescription = varchar("short_description", 1024).nullable()
    val preview = varchar("preview", 1024).nullable()

    override val primaryKey: PrimaryKey = PrimaryKey(quizId)
}

object Questions : Table() {
    val questionId = uuid("question_id").autoGenerate()
    val quizId = reference(
        name = "quiz_id",
        refColumn = Quizzes.quizId,
        onDelete = ReferenceOption.CASCADE
    )
    val question = varchar("question", 1024)
    val explanation = varchar("explanation", 1024).nullable()

    override val primaryKey: PrimaryKey = PrimaryKey(questionId)
}

object AnswerVariants : Table() {
    val answerId = uuid("answer_id").autoGenerate()
    val questionId = reference(
        name = "question_id",
        refColumn = Questions.questionId,
        onDelete = ReferenceOption.CASCADE
    )
    val text = varchar("text", 1024)
    val isCorrect = bool("is_correct").default(false)

    override val primaryKey: PrimaryKey = PrimaryKey(answerId)
}