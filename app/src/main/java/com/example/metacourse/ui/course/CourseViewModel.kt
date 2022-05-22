package com.example.metacourse.ui.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.metacourse.ResourceHelper
import com.example.metacourse.network.CourseModel
import com.example.metacourse.network.LessonModel
import com.example.metacourse.network.ModuleModel
import com.example.metacourse.network.NetworkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber

class CourseViewModel(
    private val resourceHelper: ResourceHelper
) : ViewModel() {

    private val courseEventsChannel = Channel<CourseEvents>()
    val courseEvents = courseEventsChannel.receiveAsFlow()

    private val logTag = javaClass.simpleName

    fun getCourseById(id: Long) = viewModelScope.launch {
        try {
            var courseResponse: Response<CourseModel>? = null

            courseResponse = withContext(Dispatchers.IO) {
                NetworkApi.retrofitService.getCourseById(id)
            }

            if (courseResponse.isSuccessful) {
                val result = courseResponse.body()
                Timber.d("success response: ${result.toString()}")

                result?.let {
                    courseEventsChannel.send(CourseEvents.ShowResult(it))
                }

            } else {
                var errorMessage = courseResponse.errorBody()?.string()
                Timber.d("unsuccess response: $errorMessage")
                if (errorMessage.isNullOrBlank()) {
                    errorMessage = "Произошла ошибка!"
                    courseEventsChannel.send(CourseEvents.ShowNotFound("Не найдено"))
                }
                courseEventsChannel.send(CourseEvents.ShowErrorMessage(errorMessage))
                courseEventsChannel.send(CourseEvents.ShowNotFound("Не найдено"))
            }
        } catch (e: Exception) {
            Timber.d(e)
            courseEventsChannel.send(CourseEvents.ShowErrorMessage(e.message.toString()))
        }
    }

    fun getModuleById(id: Long) = viewModelScope.launch {
        try {
            var moduleResponse: Response<ModuleModel>? = null

            moduleResponse = withContext(Dispatchers.IO) {
                NetworkApi.retrofitService.getModuleById(id)
            }

            if (moduleResponse.isSuccessful) {
                val result = moduleResponse.body()
                Timber.d("success response: ${result.toString()}")

                result?.let {
                    courseEventsChannel.send(CourseEvents.ShowModuleResult(it))
                }

            } else {
                var errorMessage = moduleResponse.errorBody()?.string()
                Timber.d("unsuccess response: $errorMessage")
                if (errorMessage.isNullOrBlank()) {
                    errorMessage = "Произошла ошибка!"
                    courseEventsChannel.send(CourseEvents.ShowNotFound("Не найдено"))
                }
                courseEventsChannel.send(CourseEvents.ShowErrorMessage(errorMessage))
                courseEventsChannel.send(CourseEvents.ShowNotFound("Не найдено"))
            }
        } catch (e: Exception) {
            Timber.d(e)
            courseEventsChannel.send(CourseEvents.ShowErrorMessage(e.message.toString()))
        }
    }

    fun getLessonById(id: Long) = viewModelScope.launch {
        try {
            var lessonResponse: Response<LessonModel>? = null

            lessonResponse = withContext(Dispatchers.IO) {
                NetworkApi.retrofitService.getLessonById(id)
            }

            if (lessonResponse.isSuccessful) {
                val result = lessonResponse.body()
                Timber.d("success response: ${result.toString()}")

                result?.let {
                    courseEventsChannel.send(CourseEvents.ShowLessonResult(it))
                }

            } else {
                var errorMessage = lessonResponse.errorBody()?.string()
                Timber.d("unsuccess response: $errorMessage")
                if (errorMessage.isNullOrBlank()) {
                    errorMessage = "Произошла ошибка!"
                    courseEventsChannel.send(CourseEvents.ShowNotFound("Не найдено"))
                }
                courseEventsChannel.send(CourseEvents.ShowErrorMessage(errorMessage))
                courseEventsChannel.send(CourseEvents.ShowNotFound("Не найдено"))
            }
        } catch (e: Exception) {
            Timber.d(e)
            courseEventsChannel.send(CourseEvents.ShowErrorMessage(e.message.toString()))
        }
    }

    sealed class CourseEvents {
        data class ShowResult(val result: CourseModel) : CourseEvents()
        data class ShowModuleResult(val result: ModuleModel) : CourseEvents()
        data class ShowLessonResult(val result: LessonModel) : CourseEvents()
        data class ShowErrorMessage(val msg: String) : CourseEvents()
        data class ShowNotFound(val msg: String) : CourseEvents()
    }
}

class CourseViewModelFactory(private val resourceHelper: ResourceHelper) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        CourseViewModel(resourceHelper) as T
}