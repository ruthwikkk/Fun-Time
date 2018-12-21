package com.ruthwik.funtime.player

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.ruthwik.funtime.R
import com.ruthwik.funtime.model.ApiVideo
import com.ruthwik.funtime.ui.video.VideoContract
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

class MediaPlayerImpl(presenter : VideoContract.Presenter) : MediaPlayer {

    private lateinit var exoPlayer : ExoPlayer
    private lateinit var context: Context
    private val presenter = WeakReference<VideoContract.Presenter> (presenter)
    private val listenerSubject: Subject<Int> = BehaviorSubject.create()

    companion object {
        const val CLICK_THROTTLE_WINDOW_MILLIS = 300L
    }

    private fun initializePlayer() {


        val loadControl = DefaultLoadControl()
        val renderersFactory = DefaultRenderersFactory(context)
        val bandwidthMeter = DefaultBandwidthMeter()
        val trackSelector = DefaultTrackSelector()

        exoPlayer = ExoPlayerFactory.newSimpleInstance(context,renderersFactory, trackSelector, loadControl, null, bandwidthMeter)
        exoPlayer.addListener(eventListener)
        Log.e("", "Listener added")
    }

    private fun initializeButtons(){

    }

    override fun play(url: String) {

        val userAgent = Util.getUserAgent(context, context.getString(R.string.app_name))

        /*val mediaSource = ExtractorMediaSource
            .Factory(DefaultDataSourceFactory(context, userAgent))
            .setExtractorsFactory(DefaultExtractorsFactory())
            .createMediaSource(Uri.parse(url))*/

        val mediaSource = DashMediaSource
            .Factory(DefaultDataSourceFactory(context, userAgent))
            .createMediaSource(Uri.parse(url))

        exoPlayer.prepare(mediaSource)
        exoPlayer.playWhenReady = true
    }

    override fun getPlayerImpl(context: Context): ExoPlayer {
        this.context = context
        initializePlayer()
        initializeButtons()
        return exoPlayer
    }

    override fun addListener(listener: Player.EventListener) {
        exoPlayer.addListener(listener)
    }


    override fun releasePlayer() {
        exoPlayer.stop()
        exoPlayer.release()
    }

    override fun subscribeToEvents() = listenerSubject.throttleFirst(CLICK_THROTTLE_WINDOW_MILLIS, TimeUnit.MILLISECONDS)

    var eventListener : Player.EventListener = object : Player.EventListener{
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            listenerSubject.onNext(playbackState)
        }

        override fun onPlayerError(error: ExoPlaybackException?) {
            listenerSubject.onNext(-1)
        }
    }
}