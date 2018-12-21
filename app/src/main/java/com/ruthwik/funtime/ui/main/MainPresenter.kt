package com.ruthwik.funtime.ui.main

import android.app.Activity
import android.content.Intent
import com.ruthwik.funtime.api.VideosService
import com.ruthwik.funtime.model.ApiResponse
import com.ruthwik.funtime.ui.video.VideoActivity
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

class MainPresenter(view : MainContract.View) : MainContract.Presenter {


    private val disposables = CompositeDisposable()
    private val view = WeakReference<MainContract.View> (view)

    override fun fetchSampleVideos() {
        disposables.add(VideosService.fetchVideos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({apiResponse -> onVideosFetchedSuccessfully(apiResponse) },
                {throwable -> onVideosFetchError(throwable)  }))

    }

    override fun dispose() {
        disposables.clear()
    }

    override fun showVideoScreen(url : String) {
        val intent = Intent((view.get() as Activity), VideoActivity::class.java)
        intent.putExtra(VideoActivity.VIDEO_URL_EXTRA, url)
        (view.get() as Activity).startActivity(intent)
    }



    private fun onVideosFetchedSuccessfully(apiResponse: ApiResponse?) {
        view.get()?.renderVideos(apiResponse?.resources!!)
    }

    private fun onVideosFetchError(throwable: Throwable?) {

    }
}