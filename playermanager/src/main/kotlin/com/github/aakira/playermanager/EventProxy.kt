package com.github.aakira.playermanager

import android.view.Surface
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.decoder.DecoderCounters
import com.google.android.exoplayer2.metadata.Metadata
import com.google.android.exoplayer2.source.MediaSourceEventListener
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import java.io.IOException

class EventProxy : Player.EventListener, AnalyticsListener {

    var onTracksChangedListener: TracksChangedListener? = null
    var onPlayerStateChangedListener: PlayerStateChangedListener? = null
    var onPlayerErrorListener: PlayerErrorListener? = null
    var onPlaybackParametersChangedListener: PlaybackParametersChangedListener? = null
    var onRepeatModeChangedListener: RepeatModeChangedListener? = null
    var onMetadataListener: MetadataListener? = null
    var onVideoSizeChangedListener: VideoSizeChangedListener? = null
    var onMediaSourceLoadErrorListener: MediaSourceLoadErrorListener? = null
    var onVideoRenderedListener: VideoRenderedListener? = null

    // Player.EventListener
    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
        // Do nothing.
    }

    // Player.EventListener
    override fun onTracksChanged(trackGroups: TrackGroupArray,
                                 trackSelections: TrackSelectionArray) {
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

    /**
     * AnalyticsListener
     * repeated [onTimelineChanged]
     */
    override fun onTimelineChanged(eventTime: AnalyticsListener.EventTime?, reason: Int) {
        // Repeated the Player.EventListener
    }

    /**
     * AnalyticsListener
     * repeated [onTracksChanged]
     */
    override fun onTracksChanged(eventTime: AnalyticsListener.EventTime?,
                                 trackGroups: TrackGroupArray?,
                                 trackSelections: TrackSelectionArray?) {
        // Repeated the Player.EventListener
    }

    /**
     * AnalyticsListener
     * repeated [onPlayerStateChanged]
     */
    override fun onLoadingChanged(eventTime: AnalyticsListener.EventTime?, isLoading: Boolean) {
        // Repeated the Player.EventListener
    }

    /**
     * AnalyticsListener
     * repeated [onPlayerStateChanged]
     */
    override fun onPlayerStateChanged(eventTime: AnalyticsListener.EventTime?,
                                      playWhenReady: Boolean, playbackState: Int) {
        // Repeated the Player.EventListener
    }

    /**
     * AnalyticsListener
     * repeated [onRepeatModeChanged]
     */
    override fun onRepeatModeChanged(eventTime: AnalyticsListener.EventTime?, repeatMode: Int) {
        // Repeated the Player.EventListener
    }

    /**
     * AnalyticsListener
     * repeated [onPlayerError]
     */
    override fun onPlayerError(eventTime: AnalyticsListener.EventTime?,
                               error: ExoPlaybackException?) {
        // Repeated the Player.EventListener
    }

    /**
     * AnalyticsListener
     * repeated [onPositionDiscontinuity]
     */
    override fun onPositionDiscontinuity(eventTime: AnalyticsListener.EventTime?, reason: Int) {
        // Repeated the Player.EventListener
    }

    /**
     * AnalyticsListener
     * repeated [onPlaybackParametersChanged]
     */
    override fun onPlaybackParametersChanged(eventTime: AnalyticsListener.EventTime?,
                                             playbackParameters: PlaybackParameters?) {
        // Repeated the Player.EventListener
    }

    /**
     * AnalyticsListener
     * repeated [onSeekProcessed]
     */
    override fun onSeekProcessed(eventTime: AnalyticsListener.EventTime?) {
        // Repeated the Player.EventListener
    }

    /**
     * AnalyticsListener
     * [com.google.android.exoplayer2.metadata.MetadataOutput.onMetadata]
     **/
    override fun onMetadata(eventTime: AnalyticsListener.EventTime?, metadata: Metadata?) {
        onMetadataListener?.invoke(eventTime, metadata)
    }

    // AnalyticsListener
    /**
     * AnalyticsListener
     * [com.google.android.exoplayer2.video.VideoListener.onVideoSizeChanged]
     * [com.google.android.exoplayer2.video.VideoRendererEventListener.onVideoSizeChanged]
     **/
    override fun onVideoSizeChanged(eventTime: AnalyticsListener.EventTime?, width: Int,
                                    height: Int, unappliedRotationDegrees: Int,
                                    pixelWidthHeightRatio: Float) {
        onVideoSizeChangedListener?.invoke(eventTime, width, height, unappliedRotationDegrees,
                pixelWidthHeightRatio)
    }

    /**
     * AnalyticsListener
     * [com.google.android.exoplayer2.video.VideoRendererEventListener.onRenderedFirstFrame]
     **/
    override fun onRenderedFirstFrame(eventTime: AnalyticsListener.EventTime?, surface: Surface?) {
        // Do nothing.
    }

    /**
     * AnalyticsListener
     * [com.google.android.exoplayer2.source.MediaSourceEventListener.onLoadStarted]
     **/
    override fun onLoadStarted(eventTime: AnalyticsListener.EventTime?,
                               loadEventInfo: MediaSourceEventListener.LoadEventInfo?,
                               mediaLoadData: MediaSourceEventListener.MediaLoadData?) {
        // Do nothing.
    }

    /**
     * AnalyticsListener
     * [com.google.android.exoplayer2.source.MediaSourceEventListener.onDownstreamFormatChanged]
     **/
    override fun onDownstreamFormatChanged(eventTime: AnalyticsListener.EventTime?,
                                           mediaLoadData: MediaSourceEventListener.MediaLoadData?) {
        // Do nothing.
    }

    /**
     * AnalyticsListener
     * [com.google.android.exoplayer2.source.MediaSourceEventListener.onUpstreamDiscarded]
     **/
    override fun onUpstreamDiscarded(eventTime: AnalyticsListener.EventTime?,
                                     mediaLoadData: MediaSourceEventListener.MediaLoadData?) {
        // Do nothing.
    }

    /**
     * AnalyticsListener
     * [com.google.android.exoplayer2.source.MediaSourceEventListener.onLoadCanceled]
     **/
    override fun onLoadCanceled(eventTime: AnalyticsListener.EventTime?,
                                loadEventInfo: MediaSourceEventListener.LoadEventInfo?,
                                mediaLoadData: MediaSourceEventListener.MediaLoadData?) {
        // Do nothing.
    }

    /**
     * AnalyticsListener
     * [com.google.android.exoplayer2.source.MediaSourceEventListener.onLoadCompleted]
     **/
    override fun onLoadCompleted(eventTime: AnalyticsListener.EventTime?,
                                 loadEventInfo: MediaSourceEventListener.LoadEventInfo?,
                                 mediaLoadData: MediaSourceEventListener.MediaLoadData?) {
        // Do nothing.
    }

    /**
     * AnalyticsListener
     * [com.google.android.exoplayer2.source.MediaSourceEventListener.onLoadError]
     **/
    override fun onLoadError(eventTime: AnalyticsListener.EventTime?,
                             loadEventInfo: MediaSourceEventListener.LoadEventInfo?,
                             mediaLoadData: MediaSourceEventListener.MediaLoadData?,
                             error: IOException?, wasCanceled: Boolean) {
        onMediaSourceLoadErrorListener?.invoke(eventTime, loadEventInfo, mediaLoadData, error,
                wasCanceled)
    }

    /**
     * AnalyticsListener
     * [com.google.android.exoplayer2.source.MediaSourceEventListener.onMediaPeriodCreated]
     **/
    override fun onMediaPeriodCreated(eventTime: AnalyticsListener.EventTime?) {
        // Do nothing.
    }

    /**
     * AnalyticsListener
     * [com.google.android.exoplayer2.source.MediaSourceEventListener.onMediaPeriodReleased]
     **/
    override fun onMediaPeriodReleased(eventTime: AnalyticsListener.EventTime?) {
        // Do nothing.
    }

    /**
     * AnalyticsListener
     * [com.google.android.exoplayer2.source.MediaSourceEventListener.onReadingStarted]
     **/
    override fun onReadingStarted(eventTime: AnalyticsListener.EventTime?) {
        // Do nothing.
    }

    // AnalyticsListener
    override fun onSeekStarted(eventTime: AnalyticsListener.EventTime?) {
    }

    // AnalyticsListener
    override fun onDrmKeysLoaded(eventTime: AnalyticsListener.EventTime?) {
    }

    // AnalyticsListener
    override fun onBandwidthEstimate(eventTime: AnalyticsListener.EventTime?, totalLoadTimeMs: Int,
                                     totalBytesLoaded: Long, bitrateEstimate: Long) {
    }

    // AnalyticsListener
    override fun onDrmKeysRestored(eventTime: AnalyticsListener.EventTime?) {
    }

    /**
     * AnalyticsListener
     * [com.google.android.exoplayer2.video.VideoRendererEventListener.onVideoDisabled]
     **/
    override fun onDecoderDisabled(eventTime: AnalyticsListener.EventTime?, trackType: Int,
                                   decoderCounters: DecoderCounters?) {
        if (trackType == C.TRACK_TYPE_VIDEO) onVideoRenderedListener?.invoke(false)
    }

    // AnalyticsListener
    override fun onShuffleModeChanged(eventTime: AnalyticsListener.EventTime?,
                                      shuffleModeEnabled: Boolean) {
    }

    /**
     * AnalyticsListener
     * [com.google.android.exoplayer2.video.VideoRendererEventListener.onVideoInputFormatChanged]
     **/
    override fun onDecoderInputFormatChanged(eventTime: AnalyticsListener.EventTime?,
                                             trackType: Int, format: Format?) {
    }

    // AnalyticsListener
    override fun onAudioSessionId(eventTime: AnalyticsListener.EventTime?, audioSessionId: Int) {
    }

    // AnalyticsListener
    override fun onDrmSessionManagerError(eventTime: AnalyticsListener.EventTime?,
                                          error: Exception?) {
    }

    /**
     * AnalyticsListener
     * [com.google.android.exoplayer2.video.VideoRendererEventListener.onVideoDecoderInitialized]
     **/
    override fun onDecoderInitialized(eventTime: AnalyticsListener.EventTime?, trackType: Int,
                                      decoderName: String?, initializationDurationMs: Long) {
    }

    /**
     * AnalyticsListener
     * [com.google.android.exoplayer2.video.VideoRendererEventListener.onDroppedFrames]
     **/
    override fun onDroppedVideoFrames(eventTime: AnalyticsListener.EventTime?, droppedFrames: Int,
                                      elapsedMs: Long) {
    }

    /**
     * AnalyticsListener
     * [com.google.android.exoplayer2.video.VideoRendererEventListener.onVideoEnabled]
     **/
    override fun onDecoderEnabled(eventTime: AnalyticsListener.EventTime?, trackType: Int,
                                  decoderCounters: DecoderCounters?) {
        if (trackType == C.TRACK_TYPE_VIDEO) onVideoRenderedListener?.invoke(true)
    }

    // AnalyticsListener
    override fun onAudioUnderrun(eventTime: AnalyticsListener.EventTime?, bufferSize: Int,
                                 bufferSizeMs: Long, elapsedSinceLastFeedMs: Long) {
    }

    // AnalyticsListener
    override fun onDrmKeysRemoved(eventTime: AnalyticsListener.EventTime?) {
    }
}
