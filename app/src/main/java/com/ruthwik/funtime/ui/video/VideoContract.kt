package com.ruthwik.funtime.ui.video

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.ruthwik.funtime.player.MediaPlayer
import io.reactivex.Observable


interface VideoContract {

    interface Presenter{

        fun dispose()
        fun getPlayer(): MediaPlayer
        fun play(url: String)
        fun releasePlayer()

        fun onFullScreenToggle(toggle : Boolean)
        fun openFullScreenDialog()
        fun closeFullScreenDialog()

        fun subscribeToEvents() : Observable<Int>

    }

    interface View{

        fun showLoading()
        fun hideLoading()
        fun showEnd()
        fun showError()
        fun openFullScreenDialog()
        fun closeFullScreenDialog()

    }
}