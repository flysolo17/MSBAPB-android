package com.danica.msbapb.models

import java.util.Date

data class News(
    val photo: String,
    val title: String,
    val description: String,
    val link: String,
    val createdAt: String,
    val type: String
)