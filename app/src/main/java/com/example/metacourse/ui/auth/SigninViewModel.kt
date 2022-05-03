package com.example.metacourse.ui.auth

import androidx.lifecycle.ViewModel
import com.example.metacourse.PreferenceManager
import com.example.metacourse.network.AuthorizeData
import com.example.metacourse.network.NetworkApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SigninViewModel : ViewModel() {
    private val signinEventsChannel = Channel<SigninEvents>()
    val signinEvents = signinEventsChannel.receiveAsFlow()

    private val logTag = javaClass.simpleName

    fun sendAuthData(email: String, password: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            NetworkApi.authRetrofit("http://localhost:95.141.136.227:8181/")
            PreferenceManager.updateBaseUrl("http://localhost:95.141.136.227:8181/")

            val response = NetworkApi.authService.sendAuthorizeRequest(
                "application/json",
                "application/json",
                AuthorizeData(email, password)
            )

            if (response.isSuccessful) {
                signinEventsChannel.send(SigninEvents.NavigateToMain)
                NetworkApi.createRetrofit("https://cabinet.spdev.kz", response.body()!!.jwtAuthResponse.accessToken)

            } else {
                signinEventsChannel.send(
                    SigninEvents.ShowErrorMessage(
                        response.errorBody()?.string() ?: "Login Error"
                    )
                )
                Timber.tag(logTag).d(response.errorBody()?.string())
            }

        } catch (t: Throwable) {
            Timber.tag(logTag).d(t)
            signinEventsChannel.send(SigninEvents.ShowErrorMessage("Connection Error"))
        }
    }

    sealed class SigninEvents {
        object NavigateToMain : SigninEvents()
        data class ShowErrorMessage(val msg: String) : SigninEvents()
    }
}