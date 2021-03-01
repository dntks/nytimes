package com.dk.newyorktimes.network.model

import android.annotation.SuppressLint
import com.dk.newyorktimes.ui.model.Article
import com.dk.newyorktimes.util.Constants.Companion.IMAGE_URL_PREFIX
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat

data class Doc(
    val headline: HeadLine?,
    val abstract: String?,
    @SerializedName("web_url")
    val webUrl: String?,
    val byline: ByLine?,
    @SerializedName("pub_date")
    val publishDate: String?,
    val multimedia: List<Multimedia>
) {
    fun toArticle() = Article(
        headline = headline?.main ?: DEFAULT_HEADLINE,
        abstract = abstract,
        webUrl = webUrl ?: DEFAULT_WEB_URL,
        byLine = byline?.original,
        thumbnailUrl = extractImageUrl(SubType.THUMBNAIL),
        imageUrl = extractImageUrl(SubType.BLOG_480),
        publishDate = publishDate?.let { parseDate(it) }
    )

    private fun extractImageUrl(subType: SubType): String {
        val url = multimedia.firstOrNull() { it.subtype == subType.value }?.url
        return "${IMAGE_URL_PREFIX}${url}"
    }

    @SuppressLint("SimpleDateFormat")
    private fun parseDate(dateToParse: String): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd")
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            val format = formatter.format(parser.parse(dateToParse) ?: "")
            format
        } catch (ex: Exception) {
            dateToParse
        }
    }

    companion object {
        private const val DEFAULT_WEB_URL = "http://nytimes.com"
        private const val DEFAULT_HEADLINE = "No headline available"
    }
}

data class ByLine(val original: String?)

data class HeadLine(val main: String?)
