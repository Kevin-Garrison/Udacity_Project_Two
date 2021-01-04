package com.example.udacity_project_two.reposistory

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.udacity_project_two.Asteroid
import com.example.udacity_project_two.api.RetrofitBuilder.getRetrofitBuilder
import com.example.udacity_project_two.api.*
import com.example.udacity_project_two.database.*
import org.json.JSONObject

//create the AsteroidRepository class, passing the Dao
class AsteroidRepository(private val asteroidsDao: AsteroidDao) {

    private suspend fun insertAsteroids(asteroidList: List<Asteroid>) {
        asteroidsDao.insertAll(asteroidList)
    }

    fun getTodaysAsteroids(date: String): LiveData<List<Asteroid>> {
        return asteroidsDao.getTodaysAsteroids(date)
    }

    fun getSavedAsteroids(): LiveData<List<Asteroid>> {
        return asteroidsDao.getSavedAsteroids()
    }

    fun getWeeksAsteroids(fromDate: String): LiveData<List<Asteroid>> {
        return asteroidsDao.getWeeksAsteroids(fromDate)
    }

    suspend fun getAllAsteroids(): List<Asteroid> {
        return asteroidsDao.getAllAsteroids()
    }

    @SuppressLint("LogNotTimber")
    suspend fun downloadFromInternet(fromDate: String) {
        Log.i("AsteroidRepository:", "downloadFromInternet() called")
        if (asteroidsDao.getAllAsteroids().isNullOrEmpty()) {
            val asteroidsResponse = getRetrofitBuilder().getAsteroids(fromDate)
            if (asteroidsResponse.isSuccessful) {
                val parseAsteroidsJsonResult = parseAsteroidsJsonResult(JSONObject(asteroidsResponse.body()!!)).toList()
                insertAsteroids(parseAsteroidsJsonResult)
            }
        }
    }




}