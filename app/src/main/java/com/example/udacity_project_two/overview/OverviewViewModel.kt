package com.example.udacity_project_two.overview

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.udacity_project_two.Asteroid
import com.example.udacity_project_two.R
import com.example.udacity_project_two.api.RetrofitBuilder.getRetrofitBuilder
import com.example.udacity_project_two.api.getTodaysDateString
import com.example.udacity_project_two.api.isNetworkAvailable
import com.example.udacity_project_two.database.AsteroidDatabase
import com.example.udacity_project_two.reposistory.AsteroidRepository
import kotlinx.coroutines.launch
import org.json.JSONException
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketTimeoutException

@SuppressLint("LogNotTimber")
class OverviewViewModel(application: Application) : AndroidViewModel(application)  {

    //Create a database object
    private val database: AsteroidDatabase = AsteroidDatabase.getDatabase(application)
    //Create a repository object
    private val repository = AsteroidRepository(database.asteroidDao())
    //Create a LiveData PictureOfTheDay object
    private val _pictureOfTheDay = MutableLiveData<PictureOfTheDay>()
    val pictureOfTheDay: LiveData<PictureOfTheDay> = _pictureOfTheDay
    //Create a LiveData List of Asteroids
    private val _displayList: MutableLiveData<List<Asteroid>> = MutableLiveData()
    val displayList: LiveData<List<Asteroid>> = _displayList

    //Check to see if there is internet. Set _pictureOfTheDay and get data if there is
    init {
        if (isNetworkAvailable(application)) {
            viewModelScope.launch {
                val asteroids = repository.getAllAsteroids()
                if (asteroids.isEmpty()) {
                    Log.i("OverviewViewModel:", "asteroids.isEmpty()")
                    updateData()
                } else {
                    loadAsteroidList(asteroids)
                }
                setPictureOfTheDay()
            }
        } else {
            Toast.makeText(
                application, application.getString(
                    R.string.no_connection
                ), Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun refreshDisplay() {
        Log.i("OverviewViewModel:", "refreshDisplay() called")
        viewModelScope.launch {
            loadAsteroidList(repository.getAllAsteroids())
        }
    }

    suspend fun updateData() {
        Log.i("OverviewViewModel:", "updateData() called")
        try {
            repository.downloadFromInternet(getTodaysDateString())
        } catch (e: Exception) {
            Timber.e("Unexpected exception: ${e.message}")
        }
        loadAsteroidList(repository.getAllAsteroids())
    }

    suspend fun setPictureOfTheDay() {
        Log.i("OverviewViewModel:", "setPictureOfTheDay() called")

        val response = getRetrofitBuilder().getPictureOfTheDay()
        if (response.isSuccessful) {
            response.body()?.let {
                _pictureOfTheDay.value = it
                Log.i("OverviewViewModel:", "Mediatype =  ${it.mediaType}")
            }
        }
    }

    fun loadWeeksAsteroids(): LiveData<List<Asteroid>> {
        Log.i("OverviewViewModel:", "loadWeeksAsteroids() called")
        return repository.getWeeksAsteroids(getTodaysDateString())
    }

    fun loadTodaysAsteroids(): LiveData<List<Asteroid>> {
        Log.i("OverviewViewModel:", "loadTodaysAsteroids() called")
        return repository.getTodaysAsteroids(getTodaysDateString())
    }

    fun loadSavedAsteroids(): LiveData<List<Asteroid>> {
        Log.i("OverviewViewModel:", "loadSavedAsteroids() called")
        return repository.getSavedAsteroids()
    }

    fun loadAsteroidList(asteroidList: List<Asteroid>) {
        _displayList.value = asteroidList
        Log.i("OverviewViewModel:", "loadAsteroidList() called")
    }
}