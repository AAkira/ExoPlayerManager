package com.github.aakira.playermanager.sample

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.decoder.DecoderCounters

fun SimpleExoPlayer.playerStateString() = "playWhenReady:$playWhenReady playbackState:" +
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> "buffering"
            ExoPlayer.STATE_ENDED -> "ended"
            ExoPlayer.STATE_IDLE -> "idle"
            ExoPlayer.STATE_READY -> "ready"
            else -> "unknown"
        }

fun SimpleExoPlayer.playerWindowIndexString() = " window: $currentWindowIndex"

fun SimpleExoPlayer.videoString(): String {
    val format = videoFormat ?: return ""
    return "\n" + format.sampleMimeType + "(id:" + format.id + " r:" + format.width + "x" +
            (format.height).toString() + getDecoderCountersBufferCountString(videoDecoderCounters) + ")"
}

fun SimpleExoPlayer.audioString(): String {
    val format = audioFormat ?: return ""
    return "\n" + format.sampleMimeType + "(id:" + format.id + " hz:" +
            format.sampleRate + " ch:" + format.channelCount +
            getDecoderCountersBufferCountString(audioDecoderCounters) + ")"
}

private fun getDecoderCountersBufferCountString(counters: DecoderCounters): String {
    counters.ensureUpdated()
    return " rb:" + counters.renderedOutputBufferCount +
            " sb:" + counters.skippedOutputBufferCount +
            " db:" + counters.droppedOutputBufferCount +
            " mcdb:" + counters.maxConsecutiveDroppedOutputBufferCount
}