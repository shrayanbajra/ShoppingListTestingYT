package com.androiddevs.shoppinglisttestingyt.data.remote.response

data class ImageResponse(
    val hits: List<ImageResult>,
    val total: Int,
    val totalHits: Int
)