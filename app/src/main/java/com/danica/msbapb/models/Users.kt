package com.danica.msbapb.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val profile : String ? ,
    val fullname: String,
    val address: String,
    val phone: String,
    val email: String
) : Parcelable
