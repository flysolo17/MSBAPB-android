package com.danica.msbapb.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date
@Parcelize
data class News(
    val photo: String,
    val title: String,
    val description: String,
    val link: String,
    val createdAt: String,
    val type: String
) : Parcelable