package com.ruthwik.funtime.player

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import io.reactivex.Observable

interface MediaPlayer {

    fun play(url: String)
    fun addListener(listener : Player.EventListener)
    fun getPlayerImpl(context: Context): ExoPlayer
    fun releasePlayer()
    fun subscribeToEvents() : Observable<Int>
}