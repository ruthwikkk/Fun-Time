package com.ruthwik.funtime.ui.main

import com.ruthwik.funtime.model.ApiVideo

interface MainContract {

    interface Presenter{

        fun fetchSampleVideos()
        fun dispose()
        fun showVideoScreen(url : String)

    }

    interface  View{

        fun renderVideos(vidoes : List<ApiVideo>)
        fun showErrorMessage()

    }
}