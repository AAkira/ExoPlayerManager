package com.github.aakira.playermanager

import android.view.Surface
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.audio.AudioCapabilities
import com.google.android.exoplayer2.audio.AudioCapabilitiesReceiver
import com.google.android.exoplayer2.decoder.DecoderCounters
import com.google.android.exoplayer2.metadata.Metadata
import com.google.android.exoplayer2.metadata.MetadataOutput
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSourceEventListener
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.video.VideoRendererEventListener
import java.io.IOException

class EventProxy : Player.EventListener,
        MetadataOutput,
        SimpleExoPlayer.VideoListener,
        AudioCapabilitiesReceiver.Listener,
        VideoRendererEventListener,
        MediaSourceEventListener,
        ExtractorMediaSource.EventListener //TODO remove
//        ,
//        AdsMediaSource.EventListener,
//        DefaultDrmSessionManager.EventListener
{
    override fun onLoadError(error: IOException?) { //TODO remove
    }

    var onTracksChangedListener: TracksChangedListener? = null
    var onPlayerStateChangedListener: PlayerStateChangedListener? = null
    var onPlayerErrorListener: PlayerErrorListener? = null
    var onPlaybackParametersChangedListener: PlaybackParametersChangedListener? = null
    var onRepeatModeChangedListener: RepeatModeChangedListener? = null
    var onMetadataListener: MetadataListener? = null
    var onVideoSizeChangedListener: VideoSizeChangedListener? = null
    var onAudioCapabilitiesChangedListener: AudioCapabilitiesChangedListener? = null
    var onMediaSourceLoadErrorListener: MediaSourceLoadErrorListener? = null
    var onExtractorMediaSourceLoadErrorListener: ExtractorMediaSourceLoadErrorListener? = null //TODO remove
    var onVideoRenderedListener: VideoRenderedListener? = null

    // Player.EventListener
    override fun onTimelineChanged(timeline: Timeline, manifest: Any?) {
        // Do nothing.
    }

    // Player.EventListener
    override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
        this.onTracksChangedListener?.invoke(trackSelections)
    }

    // Player.EventListener
    override fun onLoadingChanged(isLoading: Boolean) {
        // Do nothing.
    }

    // Player.EventListener
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        onPlayerStateChangedListener?.invoke(playWhenReady, playbackState)
    }

    // Player.EventListener
    override fun onRepeatModeChanged(repeatMode: Int) {
        onRepeatModeChangedListener?.invoke(repeatMode)
    }

    // Player.EventListener
    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
        // Do nothing.
    }

    // Player.EventListener
    override fun onPlayerError(error: ExoPlaybackException) {
        onPlayerErrorListener?.invoke(error)
    }

    // Player.EventListener
    override fun onPositionDiscontinuity(reason: Int) {
        // Do nothing.
    }

    // Player.EventListener
    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
        onPlaybackParametersChangedListener?.invoke(playbackParameters)
    }

    // Player.EventListener
    override fun onSeekProcessed() {
        // Do nothing.
    }

    // MetadataRenderer.Output
    override fun onMetadata(metadata: Metadata) {
        onMetadataListener?.invoke(metadata)
    }

    // SimpleExoPlayer.VideoListener
    override fun onVideoSizeChanged(width: Int, height: Int, unappliedRotationDegrees: Int,
                                    pixelWidthHeightRatio: Float) {
        onVideoSizeChangedListener?.invoke(width, height, unappliedRotationDegrees, pixelWidthHeightRatio)
    }

    // SimpleExoPlayer.VideoListener
    override fun onRenderedFirstFrame() {
        // Do nothing.
    }

    // AudioCapabilitiesReceiver.Listener
    override fun onAudioCapabilitiesChanged(audioCapabilities: AudioCapabilities) {
        onAudioCapabilitiesChangedListener?.invoke(audioCapabilities)
    }

    // MediaSourceEventListener
    override fun onLoadStarted(dataSpec: DataSpec?, dataType: Int, trackType: Int, trackFormat: Format?,
                               trackSelectionReason: Int, trackSelectionData: Any?, mediaStartTimeMs: Long,
                               mediaEndTimeMs: Long, elapsedRealtimeMs: Long) {
        // Do nothing.
    }

    // MediaSourceEventListener
    override fun onDownstreamFormatChanged(trackType: Int, trackFormat: Format?, trackSelectionReason: Int,
                                           trackSelectionData: Any?, mediaTimeMs: Long) {
        // Do nothing.
    }

    // MediaSourceEventListener
    override fun onUpstreamDiscarded(trackType: Int, mediaStartTimeMs: Long, mediaEndTimeMs: Long) {
        // Do nothing.
    }

    // MediaSourceEventListener
    override fun onLoadCanceled(dataSpec: DataSpec?, dataType: Int, trackType: Int, trackFormat: Format?,
                                trackSelectionReason: Int, trackSelectionData: Any?, mediaStartTimeMs: Long,
                                mediaEndTimeMs: Long, elapsedRealtimeMs: Long, loadDurationMs: Long, bytesLoaded: Long) {
        // Do nothing.
    }

    // MediaSourceEventListener
    override fun onLoadCompleted(dataSpec: DataSpec?, dataType: Int, trackType: Int, trackFormat: Format?,
                                 trackSelectionReason: Int, trackSelectionData: Any?, mediaStartTimeMs: Long,
                                 mediaEndTimeMs: Long, elapsedRealtimeMs: Long, loadDurationMs: Long, bytesLoaded: Long) {
        // Do nothing.
    }

    // MediaSourceEventListener
    override fun onLoadError(dataSpec: DataSpec?, dataType: Int, trackType: Int, trackFormat: Format?,
                             trackSelectionReason: Int, trackSelectionData: Any?, mediaStartTimeMs: Long,
                             mediaEndTimeMs: Long, elapsedRealtimeMs: Long, loadDurationMs: Long,
                             bytesLoaded: Long, error: IOException?, wasCanceled: Boolean) {
        onMediaSourceLoadErrorListener?.invoke(dataSpec, dataType, trackType, trackFormat,
                trackSelectionReason, trackSelectionData, mediaStartTimeMs,
                mediaEndTimeMs, elapsedRealtimeMs, loadDurationMs,
                bytesLoaded, error, wasCanceled)
    }

    // VideoRendererEventListener
    override fun onRenderedFirstFrame(surface: Surface?) {
        // Do nothing.
    }

    // VideoRendererEventListener
    override fun onDroppedFrames(count: Int, elapsedMs: Long) {
        // Do nothing.
    }

    // VideoRendererEventListener
    override fun onVideoDecoderInitialized(decoderName: String?, initializedTimestampMs: Long, initializationDurationMs: Long) {
        // Do nothing.
    }

    // VideoRendererEventListener
    override fun onVideoEnabled(counters: DecoderCounters?) {
        onVideoRenderedListener?.invoke(true)
    }

    // VideoRendererEventListener
    override fun onVideoDisabled(counters: DecoderCounters?) {
        onVideoRenderedListener?.invoke(false)
    }

    // VideoRendererEventListener
    override fun onVideoInputFormatChanged(format: Format?) {
        // Do nothing.
    }
}