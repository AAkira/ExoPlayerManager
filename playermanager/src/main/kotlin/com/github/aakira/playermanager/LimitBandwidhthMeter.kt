package com.github.aakira.playermanager

import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.TransferListener

class LimitBandwidthMeter(
        private val default: DefaultBandwidthMeter = DefaultBandwidthMeter()
) : BandwidthMeter by default, TransferListener by default {

    private var limitBitrate = Long.MAX_VALUE

    override fun getBitrateEstimate(): Long {
        return Math.min(default.bitrateEstimate, limitBitrate)
    }

    fun setLimitBitrate(limitBitrate: Long) {
        this.limitBitrate = limitBitrate
    }
}
