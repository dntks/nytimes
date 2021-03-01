package com.dk.newyorktimes.network.model

import com.google.gson.annotations.SerializedName

data class Doc(
    val headline: HeadLine?,
    val abstract: String?,
    @SerializedName("web_url")
    val webUrl: String?,
    val byline: ByLine?,
    @SerializedName("pub_date")
    val publishDate: String?,
    val multimedia: List<Multimedia>
)

data class ByLine(val original: String?)

data class HeadLine(val main: String?)
