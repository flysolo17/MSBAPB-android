package com.danica.msbapb.models

import kotlinx.serialization.json.Json

data class Personels(
    val id: Int,
    val name: String,
    val photo: String,
    val position: String,
    val type: String,
    val contact: String,
    val active: Int
)


