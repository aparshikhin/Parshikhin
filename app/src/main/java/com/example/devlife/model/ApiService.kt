package com.example.devlife.model

import com.example.devlife.model.models.PostDTO
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("random?json=true")
    fun getRandomPost(@Query("json") json: Boolean): Observable<PostDTO>

    companion object Factory {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://developerslife.ru/")
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}