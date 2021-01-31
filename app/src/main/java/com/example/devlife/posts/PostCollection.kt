package com.example.devlife.posts

import com.example.devlife.model.models.PostDTO

class PostCollection {
    private val posts: ArrayList<PostDTO> = ArrayList()
    private var currentindex = -1

    fun add(post: PostDTO) {
        currentindex += 1
        posts.add(currentindex, post)
    }

    fun getPrevious(): PostDTO {
        currentindex -= 1
        return posts[currentindex]
    }

    fun getNext(): PostDTO {
        currentindex += 1
        return posts[currentindex]
    }

    fun currentIsLast(): Boolean {
        return currentindex == posts.size - 1 || currentindex == -1
    }

    fun currentIsFirst(): Boolean {
        return currentindex == 0 || currentindex == -1
    }
}