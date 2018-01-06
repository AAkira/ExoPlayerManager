package com.github.aakira.playermanager.ima

import android.content.Context
import android.text.TextUtils
import android.view.ViewGroup
import com.github.aakira.playermanager.DataSourceCreator
import com.github.aakira.playermanager.ExoPlayerManager
import com.google.ads.interactivemedia.v3.api.AdErrorEvent
import com.google.ads.interactivemedia.v3.api.AdEvent
import com.google.ads.interactivemedia.v3.api.AdsLoader
import com.google.ads.interactivemedia.v3.api.AdsManager
import com.google.ads.interactivemedia.v3.api.AdsRenderingSettings
import com.google.ads.interactivemedia.v3.api.CompanionAdSlot
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory
import com.google.ads.interactivemedia.v3.api.ImaSdkSettings
import com.google.ads.interactivemedia.v3.api.player.ContentProgressProvider
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.SimpleExoPlayerView

class AdPlayerController private constructor(
        context: Context, language: String, userAgent: String,
        val adsRenderingSettings: AdsRenderingSettings,
        private val playerManager: ExoPlayerManager, private val adUiContainer: ViewGroup,
        private val companionContainer: ViewGroup?, private val companionWidth: Int,
        private val companionHeight: Int, private val sdkFactory: ImaSdkFactory
) {

    data class Builder(
            val context: Context,
            val simpleExoPlayerView: SimpleExoPlayerView,
            val adUiContainer: ViewGroup,
            val language: String = "en",
            val userAgent: String = "UserAgent",
            val playerManager: ExoPlayerManager? = null,
            val companionContainer: ViewGroup? = null,
            val companionWidth: Int = 0,
            val companionHeight: Int = 0
    ) {
        fun create(): AdPlayerController {
            val playerManager = playerManager ?: ExoPlayerManager(context)
            playerManager.injectView(simpleExoPlayerView)
            val sdkFactory: ImaSdkFactory = ImaSdkFactory.getInstance()
            return AdPlayerController(context, language, userAgent,
                    sdkFactory.createAdsRenderingSettings(), playerManager, adUiContainer,
                    companionContainer, companionWidth, companionHeight, sdkFactory
            )
        }
    }

    interface OnResumeContentListener {

        fun onResumeContentRequested()
    }

    interface OnPauseContentListener {

        fun onPauseContentRequested()
    }

    var isAdDisplayed = false
        private set
    var isAdPlaying = false
        private set
    private val videoAdPlayer: VideoAdPlayer
    private val contentProgressProvider: ContentProgressProvider
    private val adsLoader: AdsLoader
    private val adCallbacks = ArrayList<VideoAdPlayer.VideoAdPlayerCallback>(1)
    private val adEventListeners = ArrayList<AdEvent.AdEventListener>()
    private val adErrorListeners = ArrayList<AdErrorEvent.AdErrorListener>()
    private val adProgressListeners = ArrayList<AdProgressUpdatedListener>()
    private var adsManager: AdsManager? = null
    private var resumeContentListener: OnResumeContentListener? = null
    private var pauseContentListener: OnPauseContentListener? = null

    init {
        playerManager.addOnStateChangedListener { playWhenReady: Boolean, playbackState: Int ->
            if (!isAdDisplayed) return@addOnStateChangedListener

            when (playbackState) {
                Player.STATE_READY -> {
                    if (playWhenReady) {
                        for (callback in adCallbacks) {
                            callback.onPlay()
                            callback.onResume()
                        }
                    } else {
                        for (callback in adCallbacks) {
                            callback.onPause()
                        }
                    }
                }
                Player.STATE_ENDED -> {
                    for (callback in adCallbacks) {
                        callback.onEnded()
                    }
                }
            }
        }
        playerManager.addOnPlayerErrorListener {
            if (!isAdDisplayed) return@addOnPlayerErrorListener

            for (callback in adCallbacks) {
                callback.onError()
            }
        }
        playerManager.addOnMediaSourceLoadErrorListener { _, _, _, _, _, _, _, _, _, _, _, _, _ ->
            if (!isAdDisplayed) return@addOnMediaSourceLoadErrorListener

            for (callback in adCallbacks) {
                callback.onError()
            }
        }

        videoAdPlayer = object : VideoAdPlayer {
            override fun playAd() {
                isAdDisplayed = true
                playerManager.play()
            }

            override fun loadAd(url: String) {
                isAdDisplayed = true
                playerManager.setExtractorMediaSource(DataSourceCreator.UrlBuilder(
                        url = url, userAgent = userAgent).build())
            }

            override fun stopAd() {
                playerManager.stop()
            }

            override fun pauseAd() {
                playerManager.pause()
            }

            override fun resumeAd() {
                // Deprecated this method
            }

            override fun addCallback(videoAdPlayerCallback: VideoAdPlayer.VideoAdPlayerCallback) {
                adCallbacks.add(videoAdPlayerCallback)
            }

            override fun removeCallback(videoAdPlayerCallback: VideoAdPlayer.VideoAdPlayerCallback) {
                adCallbacks.remove(videoAdPlayerCallback)
            }

            override fun getAdProgress(): VideoProgressUpdate {
                if (!isAdDisplayed || playerManager.getDuration() <= 0) {
                    return VideoProgressUpdate.VIDEO_TIME_NOT_READY
                }
                val progress = VideoProgressUpdate(
                        playerManager.getCurrentPosition(),
                        playerManager.getDuration())
                for (l in adProgressListeners) {
                    l.onProgressUpdated(progress)
                }
                return progress
            }
        }
        contentProgressProvider = ContentProgressProvider {
            if (isAdDisplayed || playerManager.getDuration() <= 0) {
                return@ContentProgressProvider VideoProgressUpdate.VIDEO_TIME_NOT_READY
            }
            VideoProgressUpdate(playerManager.getCurrentPosition(), playerManager.getDuration())
        }

        adsLoader = sdkFactory.createAdsLoader(context, ImaSdkSettings().also { it.language = language })
        /**
         * An event raised when there is an error loading or playing ads.
         */
        adsLoader.addAdErrorListener { requestResumeContent() }

        /**
         * An event raised when ads are successfully loaded from the ad server via AdsLoader.
         */
        adsLoader.addAdsLoadedListener { adsManagerLoadedEvent ->
            adsManager = adsManagerLoadedEvent.adsManager
            /**
             * An event raised when there is an error loading or playing ads.
             */
            adsManager?.addAdErrorListener { requestResumeContent() }

            adsManager?.addAdEventListener { adEvent ->
                when (adEvent.type) {
                    AdEvent.AdEventType.LOADED -> adsManager?.start()
                    AdEvent.AdEventType.CONTENT_PAUSE_REQUESTED -> requestPauseContent()
                    AdEvent.AdEventType.CONTENT_RESUME_REQUESTED -> requestResumeContent()
                    AdEvent.AdEventType.PAUSED -> isAdPlaying = false
                    AdEvent.AdEventType.RESUMED -> isAdPlaying = true
                    AdEvent.AdEventType.ALL_ADS_COMPLETED -> {
                        adsManager?.destroy()
                        adsManager = null
                    }
                    else -> {
                    }
                }
            }
            for (l in adEventListeners) {
                adsManager?.addAdEventListener(l)
            }
            for (l in adErrorListeners) {
                adsManager?.addAdErrorListener(l)
            }
            adsManager?.init(adsRenderingSettings)
        }
    }

    fun requestAndPlayAds(adTagUrl: String) {
        if (TextUtils.isEmpty(adTagUrl)) {
            requestResumeContent()
            return
        }
        completeAd()

        val container = sdkFactory.createAdDisplayContainer()
        container.player = videoAdPlayer
        container.adContainer = adUiContainer

        companionContainer?.let {
            val companionAdSlot = sdkFactory.createCompanionAdSlot()
            companionAdSlot.container = it
            companionAdSlot.setSize(companionWidth, companionHeight)
            val companionAdSlots = ArrayList<CompanionAdSlot>()
            companionAdSlots.add(companionAdSlot)
            container.companionSlots = companionAdSlots
        }

        adsLoader.requestAds(
                sdkFactory.createAdsRequest().apply {
                    this.adTagUrl = adTagUrl
                    adDisplayContainer = container
                    contentProgressProvider = contentProgressProvider
                }
        )
    }

    fun pause() {
        if (isAdDisplayed) adsManager?.pause()
    }

    fun resume() {
        if (isAdDisplayed) adsManager?.resume()
    }

    fun destroy() {
        playerManager.stop()
        adEventListeners.forEach { adsManager?.removeAdEventListener(it) }
        adEventListeners.clear()
        adErrorListeners.forEach { adsManager?.removeAdErrorListener(it) }
        adErrorListeners.clear()
        adProgressListeners.clear()
    }

    fun release() {
        playerManager.release()
    }

    fun addAdEventListener(l: AdEvent.AdEventListener) {
        adEventListeners.add(l)
        adsManager?.addAdEventListener(l)
    }

    fun removeAdEventListener(l: AdEvent.AdEventListener) {
        adEventListeners.remove(l)
        adsManager?.removeAdEventListener(l)
    }

    fun addAdErrorListener(l: AdErrorEvent.AdErrorListener) {
        adErrorListeners.add(l)
        adsLoader.addAdErrorListener(l)
        adsManager?.addAdErrorListener(l)
    }

    fun removeAdErrorListener(l: AdErrorEvent.AdErrorListener) {
        adErrorListeners.remove(l)
        adsLoader.removeAdErrorListener(l)
        adsManager?.removeAdErrorListener(l)
    }

    fun addAdProgressUpdatedListener(l: AdProgressUpdatedListener) {
        adProgressListeners.add(l)
    }

    fun removeAdProgressUpdatedListener(l: AdProgressUpdatedListener) {
        adProgressListeners.remove(l)
    }

    fun setOnResumeContentListener(l: OnResumeContentListener) {
        resumeContentListener = l
    }

    fun setOnPauseContentListener(l: OnPauseContentListener) {
        pauseContentListener = l
    }

    private fun completeAd() {
        adsManager?.destroy()
        adsLoader.contentComplete()
    }

    private fun requestResumeContent() {
        isAdDisplayed = false
        isAdPlaying = false
        resumeContentListener?.onResumeContentRequested()
    }

    private fun requestPauseContent() {
        isAdPlaying = true
        pauseContentListener?.onPauseContentRequested()
    }
}