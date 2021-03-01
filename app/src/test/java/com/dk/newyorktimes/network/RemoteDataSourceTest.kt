package com.dk.newyorktimes.network

import com.dk.newyorktimes.network.api.NYTimesApi
import com.dk.newyorktimes.network.model.ArticleSearchResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class RemoteDataSourceTest : TestCase() {
    private val nyTimesApi = mockk<NYTimesApi>()
    lateinit var remoteDataSource: RemoteDataSource

    @Before
    override fun setUp() {
        remoteDataSource = RemoteDataSource(nyTimesApi)
    }

    @Test
    fun testGivenNYTimesApiReturnsResponseWhenSearchArticlesThenCorrectResponseIsReturned() {
        val expectedResponse = ArticleSearchResponse("a", mockk())
        coEvery { nyTimesApi.searchArticles(any()) } returns expectedResponse

        runBlockingTest {
            val searchArticles = remoteDataSource.searchArticles("TripActions")
            assertEquals(expectedResponse, searchArticles)
        }
    }

    @Test
    fun testWhenSearchArticlesCalledWithParamThenNYTimesApiCalledWithSameParam() {
        val expectedResponse = ArticleSearchResponse("ok", mockk())
        coEvery { nyTimesApi.searchArticles(any()) } returns expectedResponse

        runBlockingTest {
            remoteDataSource.searchArticles("TripActions")
            coVerify { nyTimesApi.searchArticles("TripActions") }
        }
    }
}