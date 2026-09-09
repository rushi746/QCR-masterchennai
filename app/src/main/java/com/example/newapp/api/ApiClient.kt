package com.example.newapp.api

import com.example.newapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        private val retrofit by lazy {

            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api by lazy {
            if(BuildConfig.FLAVOR.equals("dict", true)) {
                retrofit.create(DICTApiInterface::class.java)
            } else {
                retrofit.create(ApiInterface::class.java)
            }
        }

    }
}