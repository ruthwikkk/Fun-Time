package com.ruthwik.funtime.ui.video

import android.app.Dialog
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.PlayerView
import com.ruthwik.funtime.R



class VideoActivity : AppCompatActivity(), VideoContract.View {

    private lateinit var videoView : PlayerView
    private lateinit var progressLoading : ProgressBar
    private lateinit var debugRootView : LinearLayout
    private lateinit var fullScreenDialog : Dialog
    private lateinit var fullScreenButton : FrameLayout
    private lateinit var fullScreenIcon : ImageView

    private var mExoPlayerFullscreen = false

    companion object {
        const val VIDEO_URL_EXTRA = "video_url_extra"
    }

    private lateinit var presenter: VideoContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        init()
    }

    private fun init() {
        presenter = VideoPresenter(this)
        val videoUrl = intent.getStringExtra(VIDEO_URL_EXTRA)
        //val videoUrl = "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd"
        videoView = findViewById(R.id.ep_video_view)
        progressLoading = findViewById(R.id.pb_loading)
        debugRootView = findViewById(R.id.controls_root)
        videoView.player = presenter.getPlayer().getPlayerImpl(this)
        presenter.subscribeToEvents().subscribe(this::onVideoEvent)
        presenter.play(videoUrl)
        initFullScreenButton()
        initFullScreenDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            presenter.releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            presenter.releasePlayer()
        }
    }

    fun initFullScreenDialog(){
        fullScreenDialog = object : Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen){
            override fun onBackPressed() {
                if (mExoPlayerFullscreen)
                    presenter.closeFullScreenDialog();
                super.onBackPressed()
            }
        }
    }

    fun initFullScreenButton(){
        var controlView : PlayerControlView = videoView.findViewById(R.id.exo_controller)
        fullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon)
        fullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button)
        fullScreenButton.setOnClickListener({
            if(!mExoPlayerFullscreen)
                presenter.openFullScreenDialog()
            else
                presenter.closeFullScreenDialog()
        })

    }

    override fun openFullScreenDialog() {
        (videoView.parent as ViewGroup).removeView(videoView)
        fullScreenDialog.addContentView(videoView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        fullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_skrink))
        mExoPlayerFullscreen = true
        fullScreenDialog.show()
    }

    override fun closeFullScreenDialog() {
        (videoView.parent as ViewGroup).removeView(videoView)
        (findViewById(R.id.main_view) as ConstraintLayout
                ).addView(videoView)
        mExoPlayerFullscreen = false;
        fullScreenDialog.dismiss();
        fullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_expand));
    }

    override fun showLoading() {
        progressLoading.visibility = View.VISIBLE
        //videoView.visibility = View.GONE
    }

    override fun showEnd() {
        Toast.makeText(this, "End", Toast.LENGTH_SHORT).show()    }

    override fun hideLoading() {
        progressLoading.visibility = View.GONE   }

    override fun showError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()    }

    fun onVideoEvent(state : Int){
        when (state) {

            Player.STATE_BUFFERING -> showLoading()

            Player.STATE_ENDED -> showEnd()

            -1 -> showError()

            Player.STATE_READY -> hideLoading()

            //  else -> status = PlaybackStatus.IDLE
        }
    }


}
