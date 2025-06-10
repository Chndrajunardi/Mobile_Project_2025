package com.example.todolistapp.data

import com.google.gson.annotations.SerializedName

data class ResponseQuote(

	@field:SerializedName("ResponseQuote")
	val responseQuote: List<ResponseQuoteItem?>? = null
)

data class ResponseQuoteItem(

	@field:SerializedName("q")
	val q: String? = null,

	@field:SerializedName("a")
	val a: String? = null,

	@field:SerializedName("c")
	val c: String? = null,

	@field:SerializedName("h")
	val h: String? = null
)
