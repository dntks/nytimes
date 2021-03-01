package com.dk.newyorktimes.network.model

data class Multimedia(val url: String, val subtype: String)

enum class SubType(val value: String) {
    BLOG_480("blog480"),
    THUMBNAIL("thumbLarge")
}
