package com.dk.newyorktimes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dk.newyorktimes.network.NetworkResult
import com.dk.newyorktimes.network.Repository
import com.dk.newyorktimes.network.model.ArticleSearchResponse
import com.dk.newyorktimes.ui.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        searchedArticles.value = NetworkResult.Error("Articles Not Found")
    }

    val searchedArticles = MutableLiveData<NetworkResult<List<Article>>>()

    fun searchArticles(name: String = "") {
        searchedArticles.value = NetworkResult.Loading()
        viewModelScope.launch(exceptionHandler) {
            val articles: NetworkResult<List<Article>> = getArticles(name)
            searchedArticles.value = articles
        }
    }

    private suspend fun getArticles(name: String): NetworkResult<List<Article>> {
        val response = repository.remote.searchArticles(name)
        val articles = mapResponseToArticles(response)
        return NetworkResult.Success(articles)
    }

    private fun mapResponseToArticles(searchResponse: ArticleSearchResponse) =
        searchResponse.response?.docs?.map { it.toArticle() } ?: emptyList()
}
