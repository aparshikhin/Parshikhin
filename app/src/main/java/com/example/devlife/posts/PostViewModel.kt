package com.example.devlife.posts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.devlife.model.ApiService
import com.example.devlife.model.RemoteRepository
import com.example.devlife.model.models.DataStatus
import com.example.devlife.model.models.LoadData
import com.example.devlife.model.models.PostDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

class PostViewModel : ViewModel() {
    private lateinit var disposable: Disposable
    private val postCollection: PostCollection = PostCollection()
    private val downloadPostSubject = BehaviorSubject.create<Boolean>()
    val post: MutableLiveData<LoadData<PostDTO>> by lazy {
        MutableLiveData<LoadData<PostDTO>>()
    }
    val visibilityPreviousButton = Transformations.map(post) {
        !postCollection.currentIsFirst()
    }

    object RemoteRepositoryProvider {
        fun provideRemoteRepository(): RemoteRepository {
            return RemoteRepository(ApiService.create())
        }
    }

    fun start() {
        val repository = RemoteRepositoryProvider.provideRemoteRepository()
        disposable = downloadPostSubject
            .flatMap { repository.getRandomPost() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.status == DataStatus.Successful) {
                    postCollection.add(it.data!!)
                }
                post.value = it
            }, { throwable ->
                Log.e("DevLife", throwable.message!!)
                post.value = LoadData.error(throwable)
            })
    }

    fun nextPost() {
        if (postCollection.currentIsLast()) {
            downloadPostSubject.onNext(true)
        } else {
            post.value = LoadData.data(postCollection.getNext())
        }
    }

    fun previousPost() {
        post.value = LoadData.data(postCollection.getPrevious())
    }

    fun destroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}