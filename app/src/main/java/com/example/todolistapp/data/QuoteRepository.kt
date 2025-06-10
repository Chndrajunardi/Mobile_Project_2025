package com.example.todolistapp.data

import androidx.lifecycle.LiveData
import com.example.todolistapp.network.QuoteApiService
import com.example.todolistapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.net.UnknownHostException

class QuoteRepository(private val quoteDao: QuoteDao) {
    val allQuotes: LiveData<List<Quote>> = quoteDao.getAllQuotes()
    private val apiService = RetrofitClient.quoteApiService

    suspend fun refreshQuotes(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                withTimeout(5000) { // 5 seconds timeout
                    val response = apiService.getQuotes()
                    if (response.isSuccessful) {
                        response.body()?.let { quotes ->
                            val quoteEntities = quotes.map { quote ->
                                Quote(
                                    id = quote.c ?: System.currentTimeMillis().toString(),
                                    quote = quote.q ?: "",
                                    author = quote.a ?: "",
                                    html = quote.h ?: ""
                                )
                            }
                            quoteDao.deleteAll()
                            quoteDao.insertAll(quoteEntities)
                            Result.success(Unit)
                        } ?: Result.failure(Exception("Empty response body"))
                    } else {
                        Result.failure(Exception("Server error: ${response.code()}"))
                    }
                }
            } catch (e: TimeoutCancellationException) {
                Result.failure(Exception("Request timed out. Please check your internet connection."))
            } catch (e: UnknownHostException) {
                Result.failure(Exception("No internet connection. Please check your network settings."))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
} 