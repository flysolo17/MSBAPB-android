package com.danica.msbapb.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class IncidentReport(
    val id: Int,
    val photo : String ? = "",

    val status: String,
    val location: String,
    val type: String,
    val description: String,
    val severity: String,
    val respondents : String  ? = "",
    val incident_date: String,
) :  Parcelable

