package com.github.aakira.playermanager.sample

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import com.github.aakira.playermanager.DataSourceCreator
import com.github.aakira.playermanager.ExoPlayerManager
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util
import kotterknife.bindView
import timber.log.Timber

/**
 * An activity that plays media using [SimpleExoPlayer].
 */
class PlayerActivity : Activity() {

    companion object {
        // Apple Hls video [https://developer.apple.com/streaming/examples/]
        const val HLS_SAMPLE_URL =
                "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_ts/master.m3u8"
    }

    private val playerView: PlayerView by bindView(R.id.player_view)
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

        playerManager.injectView(playerView)
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
            val currentSpeed = playerManager.getPlayBackParameters()?.speed
                    ?: return@setOnClickListener
            (if (currentSpeed > 2f) 0.4f else currentSpeed + 0.2f).let {
                playerManager.setPlaybackParameters(it, 1f)
            }
        }
        playerManager.addOnAudioCapabilitiesChangedListener {

        }
        playerManager.addOnVideoSizeChangedListener { eventTime, width, height, unappliedRotationDegrees,
                                                      pixelWidthHeightRatio ->

        }
        playerManager.addOnMediaSourceLoadErrorListener { eventTime, loadEventInfo, mediaLoadData,
                                                          error, wasCanceled ->

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
