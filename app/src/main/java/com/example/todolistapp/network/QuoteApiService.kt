package com.example.todolistapp.network

import com.example.todolistapp.data.ResponseQuoteItem
import retrofit2.Response
import retrofit2.http.GET
 
interface QuoteApiService {
    @GET("quotes")
    suspend fun getQuotes(): Response<List<ResponseQuoteItem>>
} 