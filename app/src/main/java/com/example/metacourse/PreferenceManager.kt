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

    suspend fun updateName(name: String, surname: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NAME] = name
            preferences[PreferencesKeys.SURNAME] = surname
        }
    }

    suspend fun getName(): String {
        lateinit var name: String
        dataStore.edit { preferences ->
            name = preferences[PreferencesKeys.NAME]!!
            name += " " + preferences[PreferencesKeys.SURNAME]!!
        }
        return name
    }

    suspend fun updateEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.EMAIL] = email
        }
    }

    suspend fun getEmail(): String {
        lateinit var email: String
        dataStore.edit { preferences ->
            email = preferences[PreferencesKeys.EMAIL]!!
        }
        return email
    }

    suspend fun updateBirthdate(birthdate: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.BIRTHDATE] = birthdate
        }
    }

    suspend fun updatePhoto(photo: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.PHOTO] = photo
        }
    }

    suspend fun getBirthdate(): String {
        lateinit var birthdate: String
        dataStore.edit { preferences ->
            birthdate = preferences[PreferencesKeys.BIRTHDATE]!!
        }
        return birthdate
    }

    object PreferencesKeys {
        val BASE_URL = preferencesKey<String>("base_url")
        val NAME = preferencesKey<String>("name")
        val SURNAME = preferencesKey<String>("surname")
        val BIRTHDATE = preferencesKey<String>("birthdate")
        val EMAIL = preferencesKey<String>("email")
        val PHOTO = preferencesKey<String>("photo")
    }
}