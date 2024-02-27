package com.danica.msbapb

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp
import java.util.prefs.Preferences





@HiltAndroidApp
class Danica : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}