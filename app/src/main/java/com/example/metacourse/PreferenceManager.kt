package com.example.metacourse

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow

object PreferenceManager {
    private const val TAG = "PreferenceManager"

    lateinit var dataStore: DataStore<Preferences>
    lateinit var preferencesFlow: Flow<Preferences>

    fun initPreferences(context: Context) {
        dataStore = context.createDataStore("user_search_preferences")
        preferencesFlow = dataStore.data
    }

    suspend fun updateBaseUrl(baseUrl: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.BASE_URL] = baseUrl
        }
    }

    suspend fun getBaseUrl(): String {
        lateinit var baseUrl: String
        dataStore.edit { preferences ->
            baseUrl = preferences[PreferencesKeys.BASE_URL]!!
        }
        return baseUrl
    }

    object PreferencesKeys {
        val BASE_URL = preferencesKey<String>("base_url")
    }
}