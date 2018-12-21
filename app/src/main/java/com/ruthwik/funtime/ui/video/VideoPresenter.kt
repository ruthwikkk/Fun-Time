package com.ruthwik.funtime.ui.video

import com.google.android.exoplayer2.Player
import com.ruthwik.funtime.player.MediaPlayerImpl
import io.reactivex.Observable
import java.lang.ref.WeakReference



class VideoPresenter(videoView : VideoContract.View) : VideoContract.Presenter {


    private val view = WeakReference<VideoContract.View> (videoView)
    private val mediaPlayer = MediaPlayerImpl(this)

    override fun dispose() {

    }

    override fun getPlayer() = mediaPlayer

    override fun play(url: String) = mediaPlayer.play(url)

    override fun releasePlayer() = mediaPlayer.releasePlayer()

    override fun onFullScreenToggle(toggle: Boolean ) {
        if(toggle)
            openFullScreenDialog()
        else
            closeFullScreenDialog()
    }

    override fun openFullScreenDialog() {
        view.get()?.openFullScreenDialog()
    }

    override fun closeFullScreenDialog() {
        view.get()?.closeFullScreenDialog()
    }

    override fun subscribeToEvents() = mediaPlayer.subscribeToEvents()

}