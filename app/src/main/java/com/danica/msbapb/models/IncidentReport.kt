package com.danica.msbapb.models

import java.time.Instant

data class IncidentReport(
    val id: Int,
    val status: String,
    val location: String,
    val type: String,
    val description: String,
    val severity: String,
    val incident_date: String, // Using Instant for date/time
    val reporter: Reporter
) {
    data class Reporter(
        val fullname: String,
        val contact: String,
        val email: String
    )
}

