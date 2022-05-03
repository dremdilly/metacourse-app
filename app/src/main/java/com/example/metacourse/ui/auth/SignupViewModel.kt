package com.example.metacourse.ui.auth

import androidx.lifecycle.ViewModel
import com.example.metacourse.PreferenceManager
import com.example.metacourse.network.NetworkApi
import com.example.metacourse.network.RegisterData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SignupViewModel : ViewModel() {
    private val signupEventsChannel = Channel<SignupEvents>()
    val signupEvents = signupEventsChannel.receiveAsFlow()

    private val logTag = javaClass.simpleName

    fun sendRegisterData(email: String, password: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            NetworkApi.authRetrofit("http://localhost:8181")
            PreferenceManager.updateBaseUrl("http://localhost:8181")

            val response = NetworkApi.retrofitService.sendRegisterRequest(
                "application/json",
                "application/json",
                RegisterData(email, password)
            )

            if (response.isSuccessful) {
                signupEventsChannel.send(SignupEvents.NavigateToSignin)
            } else {
                signupEventsChannel.send(
                    SignupEvents.ShowErrorMessage(
                        response.errorBody()?.string() ?: "Registration Error"
                    )
                )
                Timber.tag(logTag).d(response.errorBody()?.string())
            }

        } catch (t: Throwable) {
            Timber.tag(logTag).d(t)
            signupEventsChannel.send(SignupEvents.ShowErrorMessage("Connection Error"))
        }
    }

    sealed class SignupEvents {
        object NavigateToSignin : SignupEvents()
        data class ShowErrorMessage(val msg: String) : SignupEvents()
    }
}