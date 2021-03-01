package com.dk.newyorktimes.network

import com.dk.newyorktimes.network.api.NYTimesApi
import com.dk.newyorktimes.network.model.ArticleSearchResponse
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val nyTimesApi: NYTimesApi
) {
    suspend fun searchArticles(searchPhrase: String): ArticleSearchResponse {
        return nyTimesApi.searchArticles(searchPhrase)
    }
}