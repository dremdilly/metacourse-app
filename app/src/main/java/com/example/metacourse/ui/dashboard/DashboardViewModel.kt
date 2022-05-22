package com.example.metacourse.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metacourse.PreferenceManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val _name = MutableLiveData<String>().apply {
        viewModelScope.launch {
//            PreferenceManager.preferencesFlow.collect {
//                value = it[PreferenceManager.PreferencesKeys.NAME] + " " + it[PreferenceManager.PreferencesKeys.SURNAME]
//            }
            value = PreferenceManager.getName()
        }

    }
    private val _email = MutableLiveData<String>().apply {
        viewModelScope.launch {
            value = PreferenceManager.getEmail()
        }
    }

    val name: LiveData<String> = _name
    val email: LiveData<String> = _email
}