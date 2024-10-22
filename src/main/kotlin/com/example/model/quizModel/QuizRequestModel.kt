package com.example.model.quizModel

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

/*

Quiz Request Model.

This model introduces data witch can be retrieved from the client side

*/
@Serializable
data class QuizRequest(
    val title: String,
    val shortDescription: String? = null,
    val preview: String? = null,
)

@Serializable
data class QuestionRequest(
    @Contextual val quizId: UUID,
    val question: String,
    val explanation: String? = null,
)

@Serializable
data class AnswerVariantRequest(
    @Contextual val questionId: UUID,
    val text: String,
    val isCorrect: Boolean = false,
)