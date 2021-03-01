package com.dk.newyorktimes.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dk.newyorktimes.TestCoroutineRule
import com.dk.newyorktimes.network.NetworkConnectionChecker
import com.dk.newyorktimes.network.NetworkResult
import com.dk.newyorktimes.network.RemoteDataSource
import com.dk.newyorktimes.network.Repository
import com.dk.newyorktimes.network.model.ArticleSearchResponse
import com.dk.newyorktimes.network.model.Response
import com.dk.newyorktimes.network.model.createDefaultArticle
import com.dk.newyorktimes.network.model.createDefaultDoc
import com.dk.newyorktimes.ui.model.Article
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArticleViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val repository = mockk<Repository>()
    private val remoteDataSource = mockk<RemoteDataSource>()
    private val networkConnectionChecker = mockk<NetworkConnectionChecker>()
    private val application = mockk<Application>()
    private val observer = mockk<Observer<NetworkResult<List<Article>>>>(relaxed = true)
    lateinit var articleViewModel: ArticleViewModel

    @Before
    fun setUp() {
        coEvery { repository.remote } returns remoteDataSource
        articleViewModel = ArticleViewModel(
            repository,
            networkConnectionChecker,
            application
        ).apply {
            searchedArticles.observeForever(observer)
        }
    }

    @Test
    fun testGivenNetworkConnectionCheckerReturnsFalseWhenSearchArticlesThenLoadingAndErrorResultIsObserved() {
        testCoroutineRule.runBlockingTest {
            every { networkConnectionChecker.hasInternetConnection(any()) } returns false

            articleViewModel.searchArticles()

            val slotIds = mutableListOf<NetworkResult<List<Article>>>()
            verifyAll {
                observer.onChanged(capture(slotIds))
                networkConnectionChecker.hasInternetConnection(application)
            }
            assertTrue(slotIds[0] is NetworkResult.Loading)
            assertTrue(slotIds[1] is NetworkResult.Error)
        }
    }

    @Test
    fun testGivenNetworkConnectionCheckerReturnsTrueWhenSearchArticlesWithNoParameterThenRemoteDataSourceSearchArticlesIsCalledWithEmptyString() =
        testCoroutineRule.runBlockingTest {
            every { networkConnectionChecker.hasInternetConnection(any()) } returns true

            articleViewModel.searchArticles()

            coVerifyAll {
                remoteDataSource.searchArticles("")
            }
        }

    @Test
    fun testGivenNetworkConnectionCheckerReturnsTrueWhenSearchArticlesWithStringThenRemoteDataSourceSearchArticlesIsCalledSameString() =
        testCoroutineRule.runBlockingTest {
            every { networkConnectionChecker.hasInternetConnection(any()) } returns true

            articleViewModel.searchArticles("TripActions")

            coVerifyAll {
                remoteDataSource.searchArticles("TripActions")
            }
        }

    @Test
    fun testGivenNetworkConnectionCheckerReturnsTrueWhenSearchArticlesThenSuccessStateIsObservered() =
        testCoroutineRule.runBlockingTest {
            every { networkConnectionChecker.hasInternetConnection(any()) } returns true
            coEvery { remoteDataSource.searchArticles(any()) } returns createArticleSearchResponse()

            articleViewModel.searchArticles("TripActions")

            val slotIds = mutableListOf<NetworkResult<List<Article>>>()
            coVerifyAll {
                observer.onChanged(capture(slotIds))
            }
            assertTrue(slotIds[0] is NetworkResult.Loading)
            assertTrue(slotIds[1] is NetworkResult.Success)
        }

    @Test
    fun testGivenNetworkConnectionCheckerReturnsTrueWhenSearchArticlesThenCorrectDataIsObservered() =
        testCoroutineRule.runBlockingTest {
            every { networkConnectionChecker.hasInternetConnection(any()) } returns true
            coEvery { remoteDataSource.searchArticles(any()) } returns createArticleSearchResponse()

            val expectedArticle = createDefaultArticle()
            articleViewModel.searchArticles("TripActions")

            val slotIds = mutableListOf<NetworkResult<List<Article>>>()
            coVerifyAll {
                observer.onChanged(capture(slotIds))
            }
            assertTrue(slotIds[1].data == listOf(expectedArticle))
        }

    private fun createArticleSearchResponse() = ArticleSearchResponse(
        status = "ok",
        Response(docs = listOf(createDefaultDoc()))
    )
}