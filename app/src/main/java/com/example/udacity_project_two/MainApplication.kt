package com.example.udacity_project_two

import android.app.Application
import android.os.Build
import androidx.work.*
import com.example.udacity_project_two.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

//Override application to setup background work via WorkManager
class MainApplication : Application() {

    /**
     * onCreate is called before the first screen is shown to the user
     */
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        //Add a call to delayedInit() in onCreate()
        delayedInit()
    }

    //Add a coroutine scope variable, applicationScope, which uses Dispatchers.Default
    val applicationScope = CoroutineScope(Dispatchers.Default)

    //Create an initialization function that does not block the main thread
    private fun delayedInit() = applicationScope.launch {
        setupRecurringWork()
    }

    private fun setupRecurringWork() {

        //use a Builder to define constraints In setupRecurringWork()
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        //add the constraint to the repeatingRequest definition SEE ABOVE!!!
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        //Schedule the work as unique
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)
    }
}