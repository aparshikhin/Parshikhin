package com.example.devlife.posts

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.devlife.R
import com.example.devlife.model.models.DataStatus
import com.example.devlife.model.models.LoadData
import com.example.devlife.model.models.PostDTO

class PostFragment : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorView: LinearLayout
    private lateinit var viewModel: PostViewModel
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var retryButton: Button
    private lateinit var descriptionTextView: TextView
    private val postObserver = Observer<LoadData<PostDTO>> {
        when (it.status) {
            DataStatus.Loading -> {
                progressBar.visibility = View.VISIBLE
                errorView.visibility = View.GONE
            }
            DataStatus.Error -> {
                progressBar.visibility = View.GONE
                imageView.visibility = View.GONE
                descriptionTextView.visibility = View.GONE
                errorView.visibility = View.VISIBLE
                nextButton.visibility = View.GONE
                previousButton.visibility = View.GONE
            }
            DataStatus.Successful -> renderPost(it.data!!)
        }
    }

    private val previousButtonObserver = Observer<Boolean> {
        previousButton.isEnabled = it
    }
    private val glideRequestListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            p0: GlideException?,
            p1: Any?,
            p2: Target<Drawable>?,
            p3: Boolean
        ): Boolean {
            Toast.makeText(context, p0?.message, Toast.LENGTH_LONG).show()
            return false
        }

        override fun onResourceReady(
            p0: Drawable?,
            p1: Any?,
            p2: Target<Drawable>?,
            p3: DataSource?,
            p4: Boolean
        ): Boolean {
            progressBar.visibility = View.GONE
            return false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.post_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.imageView)
        errorView = view.findViewById(R.id.errorView)
        errorView.visibility = View.GONE
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        nextButton = view.findViewById(R.id.nextButton)
        nextButton.setOnClickListener { viewModel.nextPost() }
        previousButton = view.findViewById(R.id.previousButton)
        previousButton.setOnClickListener { viewModel.previousPost() }
        descriptionTextView = view.findViewById(R.id.descriptionTextView)
        retryButton = view.findViewById(R.id.retryButton)
        retryButton.setOnClickListener {
            viewModel.nextPost()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        viewModel.start()
        viewModel.nextPost()
        viewModel.post.observe(this, postObserver)
        viewModel.visibilityPreviousButton.observe(this, previousButtonObserver)
    }

    private fun renderPost(post: PostDTO) {
        setImageToView(post.gifURL)
        descriptionTextView.text = post.description
        errorView.visibility = View.GONE
    }

    private fun setImageToView(url: String) {
        Glide.with(this)
            .load(url)
            .listener(glideRequestListener)
            .centerCrop()
            .into(imageView)
        imageView.visibility = View.VISIBLE
        descriptionTextView.visibility = View.VISIBLE
        nextButton.visibility = View.VISIBLE
        previousButton.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroy()
    }
}