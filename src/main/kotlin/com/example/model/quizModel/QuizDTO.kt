package com.example.model.quizModel

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class QuizDTO(
    @Contextual val quizId: UUID,
    val title: String,
    val shortDescription: String? = null,
    val preview: String? = null,
)

@Serializable
data class QuestionDTO(
    @Contextual val questionId: UUID,
    @Contextual val quizId: UUID,
    val question: String,
    val explanation: String? = null,
)

@Serializable
data class AnswerVariantDTO(
    @Contextual val answerId: UUID,
    @Contextual val questionId: UUID,
    val text: String,
    val isCorrect: Boolean = false,
)