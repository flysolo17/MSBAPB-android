package com.danica.msbapb.utils

import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val STORAGE_LINK ="https://danica.msbapb.com/api/uploads"

fun String.formatDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = dateFormat.parse(this)
    val formattedDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formattedDateFormat.format(date)
}




fun TextInputLayout.isEmail(): Boolean {
    val email = this.editText?.text?.toString() ?: run {
        this.error = "Please enter an email address"
        return false
    }
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        this.error = "Invalid email format"
        return false
    }
    return true
}

fun TextInputLayout.isString(): Boolean {
    val s = this.editText?.text?.toString() ?: run {
        this.error = "Please enter name"
        return false
    }
    if (!s.matches(Regex("^[A-Za-z0-9 ]*$"))) {
        this.error = "Special characters not allowed"
        return false
    }
    return true
}

fun TextInputLayout.isPhone(): Boolean {
    val p = this.editText?.text?.toString() ?: run {
        this.error = "Please enter phone number"
        return false
    }
    if (!p.matches(Regex("^09\\d{9}$"))) {
        this.error = "Invalid phone number format"
        return false
    }
    return true
}

fun TextInputLayout.isPassword(): Boolean {
    val p = this.editText?.text?.toString() ?: run {
        this.error = "Please enter password"
        return false
    }
    if (p.length < 7) {
        this.error = "Password must be at least 7 characters"
        return false
    }
    return true
}

fun TextInputLayout.confirmPassword(password: String): Boolean {
    val p = this.editText?.text?.toString() ?: run {
        this.error = "Please enter password"
        return false
    }
    if (password != p) {
        this.error = "Password does not match"
        return false
    }
    return true
}
