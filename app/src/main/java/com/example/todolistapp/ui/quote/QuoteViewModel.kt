package com.example.todolistapp.ui.quote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.data.Quote
import com.example.todolistapp.data.QuoteDatabase
import com.example.todolistapp.data.QuoteRepository
import kotlinx.coroutines.launch

class QuoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: QuoteRepository
    private val _quotes = MutableLiveData<List<Quote>>()
    val quotes: LiveData<List<Quote>> = _quotes

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        val quoteDao = QuoteDatabase.getDatabase(application).quoteDao()
        repository = QuoteRepository(quoteDao)
        
        // Observe database changes
        repository.allQuotes.observeForever { quotes ->
            _quotes.value = quotes
        }
    }

    fun refreshQuotes() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            repository.refreshQuotes()
                .onSuccess {
                    _error.value = null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            
            _isLoading.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.allQuotes.removeObserver { quotes ->
            _quotes.value = quotes
        }
    }
}