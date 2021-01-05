package com.example.udacity_project_two.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.udacity_project_two.api.RetrofitBuilder.getRetrofitBuilder
import com.example.udacity_project_two.api.getTodaysDateString
import com.example.udacity_project_two.api.parseAsteroidsJsonResult
import com.example.udacity_project_two.database.AsteroidDatabase
import org.json.JSONObject

//Create a class called RefreshDataWorker that is a CoroutineWorker
class RefreshDataWorker(private val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            val asteroidsResponse = getRetrofitBuilder().getAsteroids(getTodaysDateString())
            if (asteroidsResponse.isSuccessful) {
                val asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidsResponse.body()!!)).toList()
                AsteroidDatabase.getDatabase(context.applicationContext).asteroidDao().insertAll(asteroidList)
                return Result.success()
            } else {
                Result.failure()
            }
        } catch (exception: Exception) {
            Result.failure()
        }
    }

   //create a companion object and define a work name that can be used to uniquely identify this worker
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
}