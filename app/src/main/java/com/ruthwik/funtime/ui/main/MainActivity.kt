package com.ruthwik.funtime.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.ruthwik.funtime.R
import com.ruthwik.funtime.model.ApiVideo

class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var progeressBar : ProgressBar
    private lateinit var tvMessage : TextView
    private lateinit var rvVideoes : RecyclerView

    private lateinit var presenter : MainContract.Presenter
    private lateinit var videoAdapter : MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {

        progeressBar = findViewById(R.id.pb_main)
        tvMessage = findViewById(R.id.tv_empty)
        rvVideoes = findViewById(R.id.rv_videos)

        initRecyclerView()

        presenter = MainPresenter(this)
        presenter.fetchSampleVideos()
        showLoadingIndicator()
        hideEmptyView()
    }

    private fun initRecyclerView() {

        rvVideoes.layoutManager = LinearLayoutManager(this)
        rvVideoes.setHasFixedSize(true)

        videoAdapter = MainAdapter()
        videoAdapter.onItemClick().subscribe(this::onVideoItemClick)
        rvVideoes.adapter = videoAdapter

    }

    private fun onVideoItemClick(video : ApiVideo){
        presenter.showVideoScreen(createVideoUrl(video))
    }

    override fun renderVideos(vidoes : List<ApiVideo>) {
        hideLoadingIndicator()
        hideEmptyView()
        videoAdapter.onVideosUpdate(vidoes)

    }

    override fun showErrorMessage() {
        hideLoadingIndicator()
        showEmptyView()
    }

    private fun showLoadingIndicator() {
        progeressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingIndicator() {
        progeressBar.visibility = View.GONE
    }

    private fun hideEmptyView() {
        tvMessage.visibility = View.GONE
    }

    private fun showEmptyView() {
        tvMessage.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }

    private fun createVideoUrl(video: ApiVideo) =
        "https://res.cloudinary.com/demo/video/${video.type}/v${video.version}/${video.publicId}.${video.format}"
}
