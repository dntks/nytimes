package com.dk.newyorktimes.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val headline: String,
    val webUrl: String,
    val abstract: String?,
    val byLine: String?,
    val thumbnailUrl: String?,
    val imageUrl: String?,
    val publishDate: String?
) : Parcelable