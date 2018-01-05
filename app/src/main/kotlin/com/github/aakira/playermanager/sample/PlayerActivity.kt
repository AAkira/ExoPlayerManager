package com.github.aakira.playermanager.sample

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import com.github.aakira.playermanager.DataSourceCreator
import com.github.aakira.playermanager.ExoPlayerManager
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.util.Util
import kotterknife.bindView
import timber.log.Timber
import java.io.IOException

/**
 * An activity that plays media using [SimpleExoPlayer].
 */
class PlayerActivity : Activity() {

    companion object {
        // Apple Hls video [https://developer.apple.com/streaming/examples/]
        const val HLS_SAMPLE_URL =
                "https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/bipbop_16x9_variant.m3u8"
    }

    private val simpleExoPlayerView: SimpleExoPlayerView by bindView(R.id.player_view)
    private val limitBitrateButton: Button by bindView(R.id.limitBitrateButton)
    private val pitchButton: Button by bindView(R.id.pitchButton)
    private val playButton: Button by bindView(R.id.playButton)
    private val pauseButton: Button by bindView(R.id.pauseButton)
    private val reconnectButton: Button by bindView(R.id.reconnectButton)
    private val stopButton: Button by bindView(R.id.stopButton)

    private val playerManager: ExoPlayerManager by lazy(LazyThreadSafetyMode.NONE) { ExoPlayerManager(this) }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_activity)

        playerManager.injectView(simpleExoPlayerView)
        playerManager.getPlayBackParameters()?.let { updatePitchString(it.pitch) }

        playButton.setOnClickListener {
            playerManager.play()
        }
        pauseButton.setOnClickListener {
            playerManager.pause()
        }
        stopButton.setOnClickListener {
            playerManager.stop()
        }
        reconnectButton.setOnClickListener {
            playerManager.restartCurrentPosition()
        }
        limitBitrateButton.setOnClickListener {
            playerManager.setMaxVideoBitrate((60 * 1000).toLong())
        }
        pitchButton.setOnClickListener {
            val currentPitch = playerManager.getPlayBackParameters()?.pitch ?: return@setOnClickListener
            (if (currentPitch > 2f) 0.4f else currentPitch + 0.2f).let {
                playerManager.setPlaybackParameters(it, it)
            }
        }
        playerManager.addOnAudioCapabilitiesChangedListener {

        }
        playerManager.addOnVideoSizeChangedListener { width, height, unappliedRotationDegrees, pixelWidthHeightRatio ->

        }
        playerManager.addOnAdaptiveMediaSourceLoadErrorListener {
            dataSpec: DataSpec?, dataType: Int, trackType: Int,
            trackFormat: Format?, trackSelectionReason: Int, trackSelectionData: Any?,
            mediaStartTimeMs: Long, mediaEndTimeMs: Long, elapsedRealtimeMs: Long,
            loadDurationMs: Long, bytesLoaded: Long, error: IOException?, wasCanceled: Boolean ->

        }
        playerManager.addOnStateChangedListener { playWhenReady: Boolean, playbackState: Int ->
            playerManager.player?.run {
                Timber.d(playerStateString() + playerWindowIndexString() + videoString() + audioString())
            }
        }
        playerManager.addOnPlayerErrorListener {

        }
        playerManager.addOnTracksChangedListener {

        }
        playerManager.addOnPlaybackParametersChangedListeners {
            updatePitchString(it.pitch)
        }

        val dataSource = DataSourceCreator.UrlBuilder(
                url = HLS_SAMPLE_URL,
                userAgent = Util.getUserAgent(this, "UserAgent")
        )
        playerManager.setHlsSource(dataSource.build())
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        playerManager.play()
    }

    override fun onPause() {
        super.onPause()
        playerManager.pause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        playerManager.release()
        super.onDestroy()
    }

    private fun updatePitchString(pitch: Float) {
        pitchButton.text = String.format("x %.1f", pitch)
    }
}