package com.github.aakira.playermanager.sample

import android.app.Activity
import android.os.Bundle
import android.widget.FrameLayout
import com.github.aakira.playermanager.ExoPlayerManager
import com.github.aakira.playermanager.ima.AdPlayerController
import com.google.ads.interactivemedia.v3.api.AdErrorEvent
import com.google.ads.interactivemedia.v3.api.AdEvent
import com.google.ads.interactivemedia.v3.api.UiElement
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.util.Util
import kotterknife.bindView

/**
 * An activity that plays ad video using ima sdk v3.
 */
class AdActivity : Activity() {

    companion object {
        // Skippable Ad [https://developers.google.com/interactive-media-ads/docs/sdks/html5/tags]
        const val VAST_SAMPLE_URL = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&" +
                "iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&" +
                "env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26" +
                "sample_ct%3Dskippablelinear&correlator="
    }

    private val adUiContainer: FrameLayout by bindView(R.id.adUiContainer)
    private val playerView: PlayerView by bindView(R.id.player_view)
    private val playerManager: ExoPlayerManager by lazy(LazyThreadSafetyMode.NONE) { ExoPlayerManager(this) }
    private val adPlayerController: AdPlayerController by lazy(LazyThreadSafetyMode.NONE) {
        AdPlayerController.Builder(
                context = this,
                playerView = playerView,
                adUiContainer = adUiContainer,
                language = "us",
                userAgent = Util.getUserAgent(this, "UserAgent"),
                playerManager = playerManager
        ).create()
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ad_activity)

        adPlayerController.adsRenderingSettings.setUiElements(hashSetOf(UiElement.AD_ATTRIBUTION, UiElement.COUNTDOWN))
        adPlayerController.addAdEventListener(AdEvent.AdEventListener {

        })
        adPlayerController.addAdErrorListener(AdErrorEvent.AdErrorListener {

        })
        adPlayerController.requestAndPlayAds(VAST_SAMPLE_URL)
    }

    public override fun onStart() {
        super.onStart()
    }

    public override fun onResume() {
        super.onResume()
        adPlayerController.resume()
    }

    public override fun onPause() {
        super.onPause()
        adPlayerController.pause()
    }

    public override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        adPlayerController.destroy()
        super.onDestroy()
    }

    override fun onDetachedFromWindow() {
        adPlayerController.release()
        super.onDetachedFromWindow()
    }
}
