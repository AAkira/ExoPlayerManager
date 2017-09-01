package com.github.aakira.playermanager.sample

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import butterknife.bindView
import com.github.aakira.playermanager.DataSourceCreator
import com.github.aakira.playermanager.ExoPlayerManager
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.util.Util
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
    private val playBackSpeedButton: Button by bindView(R.id.playBackSpeedButton)
    private val playButton: Button by bindView(R.id.playButton)
    private val pauseButton: Button by bindView(R.id.pauseButton)
    private val reconnectButton: Button by bindView(R.id.reconnectButton)
    private val stopButton: Button by bindView(R.id.stopButton)

    private val playerManager: ExoPlayerManager by lazy(LazyThreadSafetyMode.NONE) { ExoPlayerManager(this) }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_activity)

        playerManager.injectView(simpleExoPlayerView)
        playerManager.getPlayBackParameters()?.let { updatePlayBackSpeedString(it.speed) }

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
        playBackSpeedButton.setOnClickListener {
            val currentSpeed = playerManager.getPlayBackParameters()?.speed ?: return@setOnClickListener
            (if (currentSpeed > 2f) 0.4f else currentSpeed + 0.2f).let {
                playerManager.setPlaybackParameters(it, 1f)
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
            updatePlayBackSpeedString(it.speed)
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

    private fun updatePlayBackSpeedString(speed: Float) {
        playBackSpeedButton.text = String.format("x %.1f", speed)
    }
}