package com.example.udacity_project_two.api

import com.example.udacity_project_two.Constants.API_KEY
import com.example.udacity_project_two.Constants.BASE_URL
import com.example.udacity_project_two.Constants.POD_SUB_URL
import com.example.udacity_project_two.Constants.SUB_URL
import com.example.udacity_project_two.overview.PictureOfTheDay
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

//Create a ApiService interface,
interface ApiService {
    //Annotate the method with @GET, specifying the endpoint for the JSON response PictureOfTheDay
    @GET(POD_SUB_URL)
    suspend fun getPictureOfTheDay(
        @Query("api_key") apiKey: String = API_KEY
    ): Response<PictureOfTheDay>

    //Annotate the method with @GET, specifying the endpoint for the JSON Asteroid response string
    @GET(SUB_URL)
    //Define a getAsteroids() method to request the JSON response string
    suspend fun getAsteroids(
        @Query("start_date") startDate: String,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<String>
}

object RetrofitBuilder {
    // Build retrofit to parse JSON from ApiService
    fun getRetrofitBuilder(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(ApiService::class.java)
    }
}