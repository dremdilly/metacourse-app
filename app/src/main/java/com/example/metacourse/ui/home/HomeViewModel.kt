package com.example.metacourse.ui.home

import androidx.lifecycle.*
import com.example.metacourse.ResourceHelper
import com.example.metacourse.network.CourseModel
import com.example.metacourse.network.NetworkApi
import com.example.metacourse.ui.auth.SignupViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber

class HomeViewModel(
    private val resourceHelper: ResourceHelper
) : ViewModel() {

    private val homeEventsChannel = Channel<HomeEvents>()
    val homeEvents = homeEventsChannel.receiveAsFlow()

    private val logTag = javaClass.simpleName

    fun getAllCourses() = viewModelScope.launch {
        try {
            var allCoursesResponse: Response<List<CourseModel>>? = null

            allCoursesResponse = withContext(Dispatchers.IO) {
                NetworkApi.retrofitService.getAllCourses()
            }

            if (allCoursesResponse.isSuccessful) {
                val result = allCoursesResponse.body()
                Timber.d("success response: ${result.toString()}")

                result?.let {
                    homeEventsChannel.send(HomeEvents.ShowResult(it))
                }

            } else {
                var errorMessage = allCoursesResponse.errorBody()?.string()
                Timber.d("unsuccess response: $errorMessage")
                if (errorMessage.isNullOrBlank()) {
                    errorMessage = "Произошла ошибка!"
                    homeEventsChannel.send(HomeEvents.ShowNotFound("Не найдено"))
                }
                homeEventsChannel.send(HomeEvents.ShowErrorMessage(errorMessage))
                homeEventsChannel.send(HomeEvents.ShowNotFound("Не найдено"))
            }
        } catch (e: Exception) {
            Timber.d(e)
            homeEventsChannel.send(HomeEvents.ShowErrorMessage(e.message.toString()))
        }
    }

    sealed class HomeEvents {
        data class ShowResult(val result: List<CourseModel>) : HomeEvents()
        data class ShowErrorMessage(val msg: String) : HomeEvents()
        data class ShowNotFound(val msg: String) : HomeEvents()
    }
}

class HomeViewModelFactory(private val resourceHelper: ResourceHelper) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        HomeViewModel(resourceHelper) as T
}