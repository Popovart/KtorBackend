package com.example.model.quizModel

import com.example.UUIDSerializer
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
    @Serializable(with = UUIDSerializer::class) val quizId: UUID,
    val question: String,
    val explanation: String? = null,
)

@Serializable
data class AnswerVariantRequest(
    @Serializable(with = UUIDSerializer::class) val questionId: UUID,
    val text: String,
    val isCorrect: Boolean = false,
)

// FULL quiz request model

@Serializable
data class FullQuizRequest(
    val title: String,
    val shortDescription: String? = null,
    val preview: String? = null,
    val questions: List<QuestionWithAnswersRequest>
)

@Serializable
data class QuestionWithAnswersRequest(
    @Serializable(with = UUIDSerializer::class) val quizId: UUID,
    val question: String,
    val explanation: String? = null,
    val answerVariants: List<AnswerVariantRequest>
)