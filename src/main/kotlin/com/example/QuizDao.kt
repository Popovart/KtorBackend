package com.example

import kotlinx.coroutines.flow.Flow


@Dao
interface QuizDao {

    @Query("SELECT * FROM quiz")
    fun observeQuizzesInfo(): Flow<List<LocalQuiz>>

    @Query("SELECT * FROM quiz WHERE quizId = :quizId")
    fun getQuizById(quizId: QuizId): LocalQuiz

    @Transaction
    @Query("SELECT * FROM quiz WHERE quizId = :quizId")
    fun observeQuizWithQuestions(quizId: QuizId): Flow<LocalQuizWithQuestions>

    @Query("SELECT * FROM quiz_question WHERE parentQuizId = :quizId")
    fun observeQuizQuestions(quizId: QuizId): Flow<List<LocalQuizQuestion>>

    @Query("SELECT * FROM quiz_answer_variants WHERE parentQuestionId = :questionId")
    fun observeQuestionAnswerVariants(questionId: QuestionId): Flow<List<LocalQuizAnswerVariant>>

    @Upsert
    suspend fun upsertQuiz(quiz: LocalQuiz)

//    @Upsert
//    suspend fun upsertAllQuizzes(quizzes: List<LocalQuiz>)

    @Upsert
    suspend fun upsertQuizQuestion(quizQuestion: LocalQuizQuestion)

    @Upsert
    suspend fun upsertAllQuizQuestions(quizQuestions: List<LocalQuizQuestion>)

    @Upsert
    suspend fun upsertQuizAnswerVariant(quizAnswerVariant: LocalQuizAnswerVariant)

    @Upsert
    suspend fun upsertAllQuizAnswerVariants(quizAnswerVariants: List<LocalQuizAnswerVariant>)



    @Query("DELETE FROM quiz")
    suspend fun deleteAllQuizzes()

    @Query("DELETE FROM quiz WHERE quizId = :quizId")
    suspend fun deleteQuizById(quizId: QuizId)

    @Query("DELETE FROM quiz_question WHERE questionId = :questionId")
    suspend fun deleteQuizQuestion(questionId: QuestionId)

    @Query("DELETE FROM quiz_answer_variants WHERE answerId = :answerVariantId")
    suspend fun deleteQuizQuestionAnswerVariant(answerVariantId: AnswerId)

    @Query("DELETE FROM quiz_answer_variants WHERE parentQuestionId = :questionId")
    suspend fun deleteAllQuizQuestionAnswerVariants(questionId: QuestionId)
}

class QuizDaoImpl : QuizDao {

}