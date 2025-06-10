package com.example.todolistapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey
    val id: String,
    val quote: String,
    val author: String,
    val html: String
) 