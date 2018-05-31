package com.github.aakira.playermanager

import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.audio.AudioCapabilities
import com.google.android.exoplayer2.metadata.Metadata
import com.google.android.exoplayer2.source.MediaSourceEventListener
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import java.io.IOException

/**
 * See [com.google.android.exoplayer2.Player.EventListener.onPlayerStateChanged]
 */
typealias PlayerStateChangedListener = (playWhenReady: Boolean, playbackState: Int) -> Unit

/**
 * See [com.google.android.exoplayer2.Player.EventListener.onTracksChanged]
 */
typealias TracksChangedListener = (trackSelections: TrackSelectionArray) -> Unit

/**
 * See [com.google.android.exoplayer2.Player.EventListener.onPlayerError]
 */
typealias PlayerErrorListener = (e: Exception) -> Unit

/**
 * See [com.google.android.exoplayer2.Player.EventListener.onPlaybackParametersChanged]
 */
typealias PlaybackParametersChangedListener = (playbackParameters: PlaybackParameters) -> Unit

/**
 * See [com.google.android.exoplayer2.Player.EventListener.onRepeatModeChanged]
 */
typealias RepeatModeChangedListener = (repeatMode: Int) -> Unit

/**
 * See [com.google.android.exoplayer2.metadata.MetadataOutput]
 */
typealias MetadataListener = (eventTime: AnalyticsListener.EventTime?, metadata: Metadata?) -> Unit

/**
 * See [com.google.android.exoplayer2.video.VideoRendererEventListener.onVideoSizeChanged]
 */
typealias VideoSizeChangedListener = (eventTime: AnalyticsListener.EventTime?, width: Int,
                                      height: Int, unappliedRotationDegrees: Int,
                                      pixelWidthHeightRatio: Float) -> Unit

/**
 * See [com.google.android.exoplayer2.audio.AudioCapabilitiesReceiver.Listener.onAudioCapabilitiesChanged]
 */
typealias AudioCapabilitiesChangedListener = (AudioCapabilities) -> Unit

/**
 * See [com.google.android.exoplayer2.source.MediaSourceEventListener.onLoadError]
 */
typealias MediaSourceLoadErrorListener = (eventTime: AnalyticsListener.EventTime?,
                                          loadEventInfo: MediaSourceEventListener.LoadEventInfo?,
                                          mediaLoadData: MediaSourceEventListener.MediaLoadData?,
                                          error: IOException?, wasCanceled: Boolean) -> Unit

/**
 * See [com.google.android.exoplayer2.source.ExtractorMediaSource.EventListener]
 */
typealias ExtractorMediaSourceLoadErrorListener = (error: IOException) -> Unit

/**
 * See [com.google.android.exoplayer2.video.VideoRendererEventListener.onVideoEnabled]
 * See [com.google.android.exoplayer2.video.VideoRendererEventListener.onVideoDisabled]
 */
typealias VideoRenderedListener = (enable: Boolean) -> Unit
