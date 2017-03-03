package com.github.aakira.playermanager.ima

import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate

interface AdProgressUpdatedListener {
    fun onProgressUpdated(progress: VideoProgressUpdate)
}