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
 * See [MetadataRenderer.Output]
 */
typealias MetadataListener = (metadata: Metadata) -> Unit

/**
 * See [com.google.android.exoplayer2.video.VideoRendererEventListener.onVideoSizeChanged]
 */
typealias VideoSizeChangedListener = (width: Int, height: Int, unappliedRotationDegrees: Int,
                                      pixelWidthHeightRatio: Float) -> Unit
/**
 * See [com.google.android.exoplayer2.audio.AudioCapabilitiesReceiver.Listener.onAudioCapabilitiesChanged]
 */
typealias AudioCapabilitiesChangedListener = (AudioCapabilities) -> Unit

/**
 * See [com.google.android.exoplayer2.source.MediaSourceEventListener.onLoadError]
 */
typealias MediaSourceLoadErrorListener = (dataSpec: DataSpec?, dataType: Int, trackType: Int,
                                          trackFormat: Format?, trackSelectionReason: Int,
                                          trackSelectionData: Any?, mediaStartTimeMs: Long,
                                          mediaEndTimeMs: Long, elapsedRealtimeMs: Long,
                                          loadDurationMs: Long, bytesLoaded: Long,
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