package com.example.devlife.model

import com.example.devlife.model.models.LoadData
import com.example.devlife.model.models.PostDTO
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class RemoteRepository(private val apiService: ApiService) {
    fun getRandomPost(): Observable<LoadData<PostDTO>> {
        return apiService.getRandomPost(json = true)
                .map {
                    LoadData.data(it)
                }
                .onErrorReturn { error ->
                    LoadData.error(error)
                }
                .subscribeOn(Schedulers.io())
            .startWith(LoadData.loading())
    }
}