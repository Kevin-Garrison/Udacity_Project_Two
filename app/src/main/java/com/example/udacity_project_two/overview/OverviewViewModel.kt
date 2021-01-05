package com.example.udacity_project_two.overview

import android.app.Application
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
import timber.log.Timber

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
    //Create a Boolean to see if there is a internet connection
    private val internetConnected = isNetworkAvailable(application)

    //Check to see if there is internet. Set _pictureOfTheDay and get data if there is
    init {
        viewModelScope.launch {

            val asteroids = repository.getAllAsteroids()

            if (asteroids.isEmpty()) {
                Timber.i("asteroids is empty")

                if (internetConnected) {
                    Timber.i("isNetworkAvailable true")
                    updateData()
                } else {
                    Timber.i("isNetworkAvailable false")
                    loadAsteroidList(asteroids)
                }
            } else {
                Timber.i("asteroids is not empty")
                loadAsteroidList(asteroids)

                Toast.makeText(
                    application, application.getString(
                        R.string.no_connection
                    ), Toast.LENGTH_SHORT
                ).show()
            }
            setPictureOfTheDay()
        }
    }

    private suspend fun updateData() {
        Timber.i("updateData() called")
        try {
            repository.downloadFromInternet(getTodaysDateString())
        } catch (e: Exception) {
            Timber.e("Unexpected exception: ${e.message}")
        }
        loadAsteroidList(repository.getAllAsteroids())
    }

    private suspend fun setPictureOfTheDay() {
        Timber.i("setPictureOfTheDay() called")

        if (internetConnected) {
            val response = getRetrofitBuilder().getPictureOfTheDay()
            if (response.isSuccessful) {
                response.body()?.let {
                    _pictureOfTheDay.value = it
                    Timber.i("Mediatype =  ${it.mediaType}")
                }
            }
        }
    }

    fun loadWeeksAsteroids(): LiveData<List<Asteroid>> {
        Timber.i("loadWeeksAsteroids() called")
        return repository.getWeeksAsteroids(getTodaysDateString())
    }

    fun loadTodaysAsteroids(): LiveData<List<Asteroid>> {
        Timber.i("loadTodaysAsteroids() called")
        return repository.getTodaysAsteroids(getTodaysDateString())
    }

    fun loadSavedAsteroids(): LiveData<List<Asteroid>> {
        Timber.i("loadSavedAsteroids() called")
        return repository.getSavedAsteroids()
    }

    fun loadAsteroidList(asteroidList: List<Asteroid>) {
        _displayList.value = asteroidList
        Timber.i("loadAsteroidList() called")
    }
}