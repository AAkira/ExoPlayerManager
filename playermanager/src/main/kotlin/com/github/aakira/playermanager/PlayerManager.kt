package com.github.aakira.playermanager

import android.content.Context
import android.os.Handler
import com.google.android.exoplayer2.BuildConfig
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.TransferListener
import okhttp3.OkHttpClient
import java.io.IOException

class PlayerManager(val context: Context, val debugLogger: Boolean = BuildConfig.DEBUG) {

    var player: SimpleExoPlayer? = null
        private set

    private val bandwidthMeter = LimitBandwidthMeter()
    private var eventProxy = EventProxy()
    private val mainHandler = Handler()
    private var mediaSource: MediaSource? = null
    private var playerNeedsPrepare = false
    private var trackSelector: DefaultTrackSelector? = null

    private val onAdaptiveMediaSourceLoadErrorListeners = ArrayList<AdaptiveMediaSourceLoadErrorListener>()
    private var onAudioCapabilitiesChangedListeners = ArrayList<AudioCapabilitiesChangedListener>()
    private val onExtractorMediaSourceLoadErrorListeners = ArrayList<ExtractorMediaSourceLoadErrorListener>()
    private val onMetadataListeners = ArrayList<MetadataListener>()
    private val onPlayerErrorListeners = ArrayList<PlayerErrorListener>()
    private val onPlayerStateChangedListeners = ArrayList<PlayerStateChangedListener>()
    private var onTracksChangedListeners = ArrayList<TracksChangedListener>()
    private val onVideoSizeChangedListeners = ArrayList<VideoSizeChangedListener>()
    private val onVideoRenderedListeners = ArrayList<VideoRenderedListener>()

    init {
        eventProxy.onAdaptiveMediaSourceLoadErrorListener = {
            dataSpec: DataSpec?, dataType: Int, trackType: Int, trackFormat: Format?,
            trackSelectionReason: Int, trackSelectionData: Any?,
            mediaStartTimeMs: Long, mediaEndTimeMs: Long, elapsedRealtimeMs: Long,
            loadDurationMs: Long, bytesLoaded: Long, error: IOException?, wasCanceled: Boolean ->

            onAdaptiveMediaSourceLoadErrorListeners.forEach {
                it.invoke(dataSpec, dataType, trackType, trackFormat, trackSelectionReason,
                        trackSelectionData, mediaStartTimeMs, mediaEndTimeMs, elapsedRealtimeMs,
                        loadDurationMs, bytesLoaded, error, wasCanceled)
            }
        }
        eventProxy.onAudioCapabilitiesChangedListener = {
            onAudioCapabilitiesChangedListeners.forEach { listener ->
                listener.invoke(it)
            }
        }
        eventProxy.onExtractorMediaSourceLoadErrorListener = {
            onExtractorMediaSourceLoadErrorListeners.forEach { listener ->
                listener.invoke(it)
            }
        }
        eventProxy.onMetadataListener = {
            onMetadataListeners.forEach { listener -> listener.invoke(it) }
        }
        eventProxy.onPlayerErrorListener = {
            playerNeedsPrepare = true
            onPlayerErrorListeners.forEach { listener -> listener.invoke(it) }
        }
        eventProxy.onPlayerStateChangedListener = { playWhenReady: Boolean, playbackState: Int ->
            onPlayerStateChangedListeners.forEach { it.invoke(playWhenReady, playbackState) }
        }
        eventProxy.onTracksChangedListener = {
            onTracksChangedListeners.forEach { listener -> listener.invoke(it) }
        }
        eventProxy.onVideoSizeChangedListener = { width, height, unappliedRotationDegrees, pixelWidthHeightRatio ->
            onVideoSizeChangedListeners.forEach {
                it.invoke(width, height, unappliedRotationDegrees, pixelWidthHeightRatio)
            }
        }
        eventProxy.onVideoRenderedListener = {
            onVideoRenderedListeners.forEach { listener -> listener.invoke(it) }
        }

        trackSelector = DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, DefaultLoadControl()).apply {
            addListener(eventProxy)
            setVideoListener(eventProxy)
            setMetadataOutput(eventProxy)
            setVideoDebugListener(eventProxy)

            if (debugLogger) {
                EventLogger(trackSelector).let {
                    addListener(it)
                    setAudioDebugListener(it)
                    setVideoDebugListener(it)
                    setMetadataOutput(it)
                }
            }
        }
        playerNeedsPrepare = true
    }

    fun injectView(simpleExoPlayerView: SimpleExoPlayerView) {
        simpleExoPlayerView.player = player
    }

    fun setHlsSource(dataSourceCreator: DataSourceCreator) {
        val dataSource = buildDataSourceFactory(dataSourceCreator.userAgent, bandwidthMeter, dataSourceCreator.okHttpClient)

        trackSelector?.parameters = DefaultTrackSelector.Parameters(
                dataSourceCreator.preferredAudioLanguage, dataSourceCreator.preferredTextLanguage,
                dataSourceCreator.allowMixedMimeAdaptiveness, dataSourceCreator.allowNonSeamlessAdaptiveness,
                dataSourceCreator.maxVideoWidth, dataSourceCreator.maxVideoHeight, dataSourceCreator.maxVideoBitrate,
                dataSourceCreator.exceedVideoConstraintsIfNecessary, dataSourceCreator.exceedRendererCapabilitiesIfNecessary,
                dataSourceCreator.viewportWidth, dataSourceCreator.viewportHeight, dataSourceCreator.orientationMayChange)

        mediaSource = HlsMediaSource(dataSourceCreator.uri, dataSourceCreator.dataSourceCreatorInterface?.let {
            dataSourceCreator.dataSourceCreatorInterface.create(context, bandwidthMeter, dataSource)
        } ?: dataSource, mainHandler, eventProxy)
        playerNeedsPrepare = true
    }

    fun setExtractorMediaSource(dataSourceCreator: DataSourceCreator) {
        val dataSource = buildDataSourceFactory(dataSourceCreator.userAgent, bandwidthMeter, dataSourceCreator.okHttpClient)

        trackSelector?.parameters = DefaultTrackSelector.Parameters(
                dataSourceCreator.preferredAudioLanguage, dataSourceCreator.preferredTextLanguage,
                dataSourceCreator.allowMixedMimeAdaptiveness, dataSourceCreator.allowNonSeamlessAdaptiveness,
                dataSourceCreator.maxVideoWidth, dataSourceCreator.maxVideoHeight, dataSourceCreator.maxVideoBitrate,
                dataSourceCreator.exceedVideoConstraintsIfNecessary, dataSourceCreator.exceedRendererCapabilitiesIfNecessary,
                dataSourceCreator.viewportWidth, dataSourceCreator.viewportHeight, dataSourceCreator.orientationMayChange)

        mediaSource = ExtractorMediaSource(dataSourceCreator.uri, dataSourceCreator.dataSourceCreatorInterface?.let {
            dataSourceCreator.dataSourceCreatorInterface.create(context, bandwidthMeter, dataSource)
        } ?: dataSource, DefaultExtractorsFactory(), mainHandler, eventProxy)
        playerNeedsPrepare = true
    }

    fun release() {
        player?.release()
        player?.removeListener(eventProxy)
        player = null
    }

    fun play() {
        mediaSource ?: return

        if (playerNeedsPrepare) {
            player?.prepare(mediaSource)
            playerNeedsPrepare = false
        }

        player?.playWhenReady = true
    }

    fun pause() {
        player?.playWhenReady = false
    }

    fun stop() {
        player?.stop()
        playerNeedsPrepare = true
    }

    fun restartCurrentPosition() {
        val positionMs = getCurrentPosition()
        stop()
        play()
        seekTo(positionMs)
    }

    fun seekTo(positionMs: Long) {
        player?.seekTo(positionMs)
    }

    fun getDuration() = player?.duration ?: 0

    fun getCurrentPosition() = player?.currentPosition ?: 0

    fun getBufferedPercentage() = player?.bufferedPercentage ?: 0

    fun getBufferedPosition() = player?.bufferedPosition ?: 0

    fun isPlaying() = player?.playWhenReady ?: false

    fun getPlaybackState() = player?.playbackState ?: ExoPlayer.STATE_IDLE

    fun getVolume() = player?.volume ?: 0f

    fun toMute() {
        player?.volume = 0f
    }

    fun toUnMute() {
        player?.volume = 1f
    }

    fun setMaxVideoBitrate(maxVideoBitrate: Long) {
        bandwidthMeter.setLimitBitrate(maxVideoBitrate)
    }

    fun addOnAdaptiveMediaSourceLoadErrorListener(listener: AdaptiveMediaSourceLoadErrorListener) {
        onAdaptiveMediaSourceLoadErrorListeners.add(listener)
    }

    fun removeAdaptiveMediaSourceErrorListener(listener: AdaptiveMediaSourceLoadErrorListener) {
        onAdaptiveMediaSourceLoadErrorListeners.remove(listener)
    }

    fun clearAdaptiveMediaSourceErrorListeners() {
        onAdaptiveMediaSourceLoadErrorListeners.clear()
    }

    fun addOnAudioCapabilitiesChangedListener(listener: AudioCapabilitiesChangedListener) {
        onAudioCapabilitiesChangedListeners.add(listener)
    }

    fun removeAudioCapabilitiesReceiverListener(listener: AudioCapabilitiesChangedListener) {
        onAudioCapabilitiesChangedListeners.remove(listener)
    }

    fun clearAudioCapabilitiesReceiverListeners() {
        onAudioCapabilitiesChangedListeners.clear()
    }

    fun addOnMetadataListener(listener: MetadataListener) {
        onMetadataListeners.add(listener)
    }

    fun removeMetadataListener(listener: MetadataListener) {
        onMetadataListeners.remove(listener)
    }

    fun clearOnMetadataListeners() {
        onMetadataListeners.clear()
    }

    fun addOnPlayerErrorListener(listener: PlayerErrorListener) {
        onPlayerErrorListeners.add(listener)
    }

    fun removePlayerErrorListener(listener: PlayerErrorListener) {
        onPlayerErrorListeners.remove(listener)
    }

    fun clearPlayerErrorListeners() {
        onPlayerErrorListeners.clear()
    }

    fun addOnStateChangedListener(listener: PlayerStateChangedListener) {
        onPlayerStateChangedListeners.add(listener)
    }

    fun removeStateChangedListener(listener: PlayerStateChangedListener) {
        onPlayerStateChangedListeners.remove(listener)
    }

    fun clearStateChangedListeners() {
        onPlayerStateChangedListeners.clear()
    }

    fun addOnTracksChangedListener(listener: TracksChangedListener) {
        onTracksChangedListeners.add(listener)
    }

    fun removeTracksChangedListener(listener: TracksChangedListener) {
        onTracksChangedListeners.remove(listener)
    }

    fun clearTracksChangedListeners() {
        onTracksChangedListeners.clear()
    }

    fun addOnVideoSizeChangedListener(listener: VideoSizeChangedListener) {
        onVideoSizeChangedListeners.add(listener)
    }

    fun removeVideoSizeChangedListener(listener: VideoSizeChangedListener) {
        onVideoSizeChangedListeners.remove(listener)
    }

    fun clearVideoSizeChangedListeners() {
        onVideoSizeChangedListeners.clear()
    }

    fun addOnExtractorMediaSourceLoadErrorListener(listener: ExtractorMediaSourceLoadErrorListener) {
        onExtractorMediaSourceLoadErrorListeners.add(listener)
    }

    fun removeExtractorMediaSourceLoadErrorListener(listener: ExtractorMediaSourceLoadErrorListener) {
        onExtractorMediaSourceLoadErrorListeners.remove(listener)
    }

    fun clearExtractorMediaSourceLoadErrorListeners() {
        onExtractorMediaSourceLoadErrorListeners.clear()
    }

    fun addOnVideoRenderedListener(listener: VideoRenderedListener) {
        onVideoRenderedListeners.add(listener)
    }

    fun removeVideoRenderedListener(listener: VideoRenderedListener) {
        onVideoRenderedListeners.remove(listener)
    }

    fun clearVideoRenderedListeners() {
        onVideoRenderedListeners.clear()
    }

    private fun buildDataSourceFactory(userAgent: String,
                                       bandwidthMeter: TransferListener<in DataSource>,
                                       okHttpClient: OkHttpClient?) = okHttpClient?.let {
        buildOkHttpDataSourceFactory(userAgent, bandwidthMeter, okHttpClient)
    } ?: buildDefaultHttpDataSourceFactory(userAgent, bandwidthMeter)

    private fun buildDefaultHttpDataSourceFactory(userAgent: String, listener: TransferListener<in DataSource>,
                                                  allowCrossProtocolRedirects: Boolean = false) =
            DefaultHttpDataSourceFactory(userAgent, listener,
                    DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                    DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, allowCrossProtocolRedirects)

    private fun buildOkHttpDataSourceFactory(userAgent: String, listener: TransferListener<in DataSource>,
                                             okHttpClient: OkHttpClient) =
            OkHttpDataSourceFactory(okHttpClient, userAgent, listener)
}