package com.example.udacity_project_two.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.udacity_project_two.Asteroid
import com.example.udacity_project_two.Constants.DATABASE_NAME

//define a @Dao interface called AsteroidDao
@Dao
interface AsteroidDao {
    //insertAll() is an upsert, so don't forget to pass it onConflict=OnConflictStrategy.REPLACE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroidList: List<Asteroid>)

    //Get all Asteroids in the database
    @Query("Select * from Asteroid")
    suspend fun getAllAsteroids(): List<Asteroid>

    //Get Asteroids saved in the database and sort by closeApproachDate
    @Query("Select * from Asteroid ORDER by closeApproachDate")
    fun getSavedAsteroids(): LiveData<List<Asteroid>>

    //Get Asteroids with today's date
    @Query("Select * from Asteroid where closeApproachDate = :date")
    fun getTodaysAsteroids(date: String): LiveData<List<Asteroid>>

    //Get Asteroids for the past 7 days
    @Query("Select * from Asteroid where closeApproachDate >= :fromDate ORDER by closeApproachDate")
    fun getWeeksAsteroids(fromDate: String): LiveData<List<Asteroid>>
}

@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {

    //add an abstract asteroidDao getter
    abstract fun asteroidDao(): AsteroidDao

    companion object {

        @Volatile
        private var INSTANCE: AsteroidDatabase? = null

        fun getDatabase(context: Context): AsteroidDatabase {
            // use a temporary instance and return it if exist
            val temp = INSTANCE
            if (temp != null) {
                return temp
            }
            //otherwise create a new instance and return it
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

//Create an abstract VideosDatabase class that extends RoomDatabase, and
//annotate it with @Database, including entities and version
//@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
//abstract class AsteroidDatabase : RoomDatabase() {
//
//    //add an abstract videoDao variable
//    abstract val asteroidDao: AsteroidDao
//}
//
////define an INSTANCE variable to store the singleton
//private lateinit var INSTANCE: AsteroidDatabase
//
////Define a getDatabase() function to return the VideosDatabase
//fun getDatabase(context: Context): AsteroidDatabase {
//
//    //Make sure your code is synchronized so itâ€™s thread safe
//    //Wrap the if-statement
//    synchronized(AsteroidDatabase::class.java) {
//
//        //use ::INSTANCE.isInitialized to check if the variable has been initialized.
//        //If it hasn't, then initialize it
//        if (!::INSTANCE.isInitialized) {
//
//            INSTANCE = Room.databaseBuilder(context.applicationContext,
//                AsteroidDatabase::class.java,
//                DATABASE_NAME).build()
//        }
//    }
//    return INSTANCE
//}
