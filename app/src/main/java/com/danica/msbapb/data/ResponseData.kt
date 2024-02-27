package com.danica.msbapb.data

data class ResponseData<out T>(
    val status : Boolean,
    val message : String,
    val data  : T
)
