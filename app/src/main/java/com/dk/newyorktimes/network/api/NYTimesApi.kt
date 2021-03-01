package com.dk.newyorktimes.network.api

import com.dk.newyorktimes.util.Constants.Companion.NY_TIMES_API_KEY
import com.dk.newyorktimes.network.model.ArticleSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NYTimesApi {
    @GET("articlesearch.json")
    suspend fun searchArticles(
        @Query("q")
        searchPhrase: String,
        @Query("api-key")
        apiKey: String = NY_TIMES_API_KEY
    ): ArticleSearchResponse
}