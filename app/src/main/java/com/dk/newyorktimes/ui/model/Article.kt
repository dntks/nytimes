package com.dk.newyorktimes.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val headline: String,
    val webUrl: String,
    val thumbnailUrl: String?,
) : Parcelable