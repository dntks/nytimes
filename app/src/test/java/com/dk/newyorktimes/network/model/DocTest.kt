package com.dk.newyorktimes.network.model

import com.dk.newyorktimes.ui.model.Article
import junit.framework.TestCase
import org.junit.Test

class DocTest : TestCase() {

    @Test
    fun testGivenAllDataIsGivenWhenToArticleCalledOnDocThenCreatedArticleIsAsExpected() {
        val expectedArticle = createDefaultArticle()
        val doc = createDefaultDoc()
        assertEquals(doc.toArticle(), expectedArticle)
    }

    @Test
    fun testGivenOptionalValuesNotInDocWhenToArticleThenTheyAreSetAsExpected() {
        val expectedArticle = Article(
            headline = "No headline available",
            webUrl = "http://nytimes.com",
            abstract = null,
            byLine = null,
            thumbnailUrl = "https://www.nytimes.com/null",
            imageUrl = "https://www.nytimes.com/null",
            publishDate = "wrongDate",
        )
        val doc = Doc(
            headline = null,
            abstract = null,
            webUrl = null,
            byline = null,
            publishDate = "wrongDate",
            multimedia = listOf(
                Multimedia(url = "abcd", subtype = "weirdvalue"),
            )
        )
        assertEquals(doc.toArticle(), expectedArticle)
    }

}

fun createDefaultDoc() = Doc(
    headline = HeadLine("Corona disappeared from the world thanks to an amazing app"),
    abstract = "abstract",
    webUrl = "http://",
    byline = ByLine("By Donát Kiss"),
    publishDate = "2021-03-01",
    multimedia = listOf(
        Multimedia(url = "abcd", subtype = SubType.THUMBNAIL.value),
        Multimedia(url = "aret", subtype = SubType.BLOG_480.value)
    )
)

fun createDefaultArticle() = Article(
    headline = "Corona disappeared from the world thanks to an amazing app",
    webUrl = "http://",
    abstract = "abstract",
    byLine = "By Donát Kiss",
    thumbnailUrl = "https://www.nytimes.com/abcd",
    imageUrl = "https://www.nytimes.com/aret",
    publishDate = "01.03.2021",
)