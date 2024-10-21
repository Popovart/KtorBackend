package com.example.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

// I need cascade deleting when quiz deletes
@Serializable
data class Quiz(
    @Contextual val quizId: UUID,
    val title: String,
    val shortDescription: String? = null,
    val preview: String? = null,
)

@Serializable
data class Question(
    @Contextual val questionId: UUID,
    @Contextual val quizId: UUID,
    val question: String,
    val explanation: String? = null,
)

@Serializable
data class AnswerVariant(
    @Contextual val answerId: UUID,
    @Contextual val questionId: UUID,
    val text: String,
    val isCorrect: Boolean = false,
)