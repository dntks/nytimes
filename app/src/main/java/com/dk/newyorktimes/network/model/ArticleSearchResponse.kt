package com.dk.newyorktimes.network.model

data class ArticleSearchResponse(
    val status: String?,
    val response: Response?
)

data class Response(val docs: List<Doc>)