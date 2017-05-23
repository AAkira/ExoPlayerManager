package com.github.aakira.playermanager

import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.audio.AudioCapabilities
import com.google.android.exoplayer2.metadata.Metadata
import com.google.android.exoplayer2.metadata.MetadataRenderer
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSpec
import java.io.IOException

/**
 * See [ExoPlayer.EventListener.onPlayerStateChanged]
 */
typealias PlayerStateChangedListener = (playWhenReady: Boolean, playbackState: Int) -> Unit

/**
 * See [ExoPlayer.EventListener.onTracksChanged]
 */
typealias TracksChangedListener = (trackSelections: TrackSelectionArray) -> Unit

/**
 * See [ExoPlayer.EventListener.onPlayerError]
 */
typealias PlayerErrorListener = (e: Exception) -> Unit

/**
 * See [ExoPlayer.EventListener.onPlaybackParametersChanged]
 */
typealias PlaybackParametersChangedListener = (playbackParameters: PlaybackParameters) -> Unit

/**
 * See [MetadataRenderer.Output]
 */
typealias MetadataListener = (metadata: Metadata) -> Unit

/**
 * See [VideoRendererEventListener.onVideoSizeChanged]
 */
typealias VideoSizeChangedListener = (width: Int, height: Int, unappliedRotationDegrees: Int,
                                      pixelWidthHeightRatio: Float) -> Unit
/**
 * See [AudioCapabilitiesReceiver.Listener.onAudioCapabilitiesChanged]
 */
typealias AudioCapabilitiesChangedListener = (AudioCapabilities) -> Unit

/**
 * See [AdaptiveMediaSourceEventListener.onLoadError]
 */
typealias AdaptiveMediaSourceLoadErrorListener = (dataSpec: DataSpec?, dataType: Int, trackType: Int,
                                                  trackFormat: Format?, trackSelectionReason: Int,
                                                  trackSelectionData: Any?, mediaStartTimeMs: Long,
                                                  mediaEndTimeMs: Long, elapsedRealtimeMs: Long,
                                                  loadDurationMs: Long, bytesLoaded: Long,
                                                  error: IOException?, wasCanceled: Boolean) -> Unit

/**
 * See [ExtractorMediaSource.EventListener]
 */
typealias ExtractorMediaSourceLoadErrorListener = (error: IOException) -> Unit

/**
 * See [VideoRendererEventListener.onVideoEnabled]
 * See [VideoRendererEventListener.onVideoDisabled]
 */
typealias VideoRenderedListener = (enable: Boolean) -> Unit