package com.github.aakira.playermanager.sample

import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.decoder.DecoderCounters

fun SimpleExoPlayer.playerStateString() = "playWhenReady:$playWhenReady playbackState:" +
        when (playbackState) {
            Player.STATE_BUFFERING -> "buffering"
            Player.STATE_ENDED -> "ended"
            Player.STATE_IDLE -> "idle"
            Player.STATE_READY -> "ready"
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
            " db:" + counters.droppedBufferCount +
            " mcdb:" + counters.maxConsecutiveDroppedBufferCount
}