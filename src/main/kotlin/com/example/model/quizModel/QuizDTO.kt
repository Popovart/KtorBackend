package com.example.model.quizModel

import com.example.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class QuizDTO(
    @Serializable(with = UUIDSerializer::class) val quizId: UUID,
    val title: String,
    val shortDescription: String? = null,
    val preview: String? = null,
)

@Serializable
data class QuestionDTO(
    @Serializable(with = UUIDSerializer::class) val questionId: UUID,
    @Serializable(with = UUIDSerializer::class) val quizId: UUID,
    val question: String,
    val explanation: String? = null,
)

@Serializable
data class AnswerVariantDTO(
    @Serializable(with = UUIDSerializer::class) val answerId: UUID,
    @Serializable(with = UUIDSerializer::class) val questionId: UUID,
    val text: String,
    val isCorrect: Boolean = false,
)