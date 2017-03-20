package com.github.aakira.playermanager

import android.net.Uri
import okhttp3.OkHttpClient

/**
 * Create data source.
 *
 * You can customize video parameters.
 * cf. [com.google.android.exoplayer2.trackselection]
 */
class DataSourceCreator private constructor(
        val uri: Uri,
        val userAgent: String,
        val preferredAudioLanguage: String?,
        val preferredTextLanguage: String?,
        val allowMixedMimeAdaptiveness: Boolean,
        val allowNonSeamlessAdaptiveness: Boolean,
        val maxVideoWidth: Int,
        val maxVideoHeight: Int,
        val maxVideoBitrate: Int,
        val exceedVideoConstraintsIfNecessary: Boolean,
        val exceedRendererCapabilitiesIfNecessary: Boolean,
        val viewportWidth: Int,
        val viewportHeight: Int,
        val orientationMayChange: Boolean,
        val okHttpClient: OkHttpClient? = null,
        val dataSourceCreatorInterface: DataSourceCreatorInterface?
) {
    /**
     * @param url The video path (stream : url, file: path)
     * @param userAgent Your user agent
     * @param preferredAudioLanguage The preferred language for audio, as well as for forced text
     *     tracks as defined by RFC 5646. {@code null} to select the default track, or first track
     *     if there's no default.
     * @param preferredTextLanguage The preferred language for text tracks as defined by RFC 5646.
     *     {@code null} to select the default track, or first track if there's no default.
     * @param allowMixedMimeAdaptiveness Whether to allow selections to contain mixed mime types.
     * @param allowNonSeamlessAdaptiveness Whether non-seamless adaptation is allowed.
     * @param maxVideoWidth Maximum allowed video width.
     * @param maxVideoHeight Maximum allowed video height.
     * @param maxVideoBitrate Maximum allowed video bitrate.
     * @param exceedVideoConstraintsIfNecessary Whether to exceed video constraints when no
     *     selection can be made otherwise.
     * @param exceedRendererCapabilitiesIfNecessary Whether to exceed renderer capabilities when no
     *     selection can be made otherwise.
     * @param viewportWidth Viewport width in pixels.
     * @param viewportHeight Viewport height in pixels.
     * @param orientationMayChange Whether orientation may change during playback.
     * @param okHttpClient Set your OkHttp client if you want use it.
     * @param dataSourceCreatorInterface Set your data source if you want use it.
     */
    data class UrlBuilder(
            val url: String,
            val userAgent: String,
            val preferredAudioLanguage: String? = null,
            val preferredTextLanguage: String? = null,
            val allowMixedMimeAdaptiveness: Boolean = false,
            val allowNonSeamlessAdaptiveness: Boolean = true,
            val maxVideoWidth: Int = Integer.MAX_VALUE,
            val maxVideoHeight: Int = Integer.MAX_VALUE,
            val maxVideoBitrate: Int = Integer.MAX_VALUE,
            val exceedVideoConstraintsIfNecessary: Boolean = true,
            val exceedRendererCapabilitiesIfNecessary: Boolean = true,
            val viewportWidth: Int = Integer.MAX_VALUE,
            val viewportHeight: Int = Integer.MAX_VALUE,
            val orientationMayChange: Boolean = true,
            val okHttpClient: OkHttpClient? = null,
            val dataSourceCreatorInterface: DataSourceCreatorInterface? = null
    ) {
        fun build(): DataSourceCreator = DataSourceCreator(Uri.parse(url), userAgent, preferredAudioLanguage,
                preferredTextLanguage, allowMixedMimeAdaptiveness, allowNonSeamlessAdaptiveness,
                maxVideoWidth, maxVideoHeight, maxVideoBitrate, exceedVideoConstraintsIfNecessary,
                exceedRendererCapabilitiesIfNecessary, viewportWidth, viewportHeight, orientationMayChange,
                okHttpClient, dataSourceCreatorInterface)
    }

    /**
     * @param uri The video uri
     * @param userAgent Your user agent
     * @param preferredAudioLanguage The preferred language for audio, as well as for forced text
     *     tracks as defined by RFC 5646. {@code null} to select the default track, or first track
     *     if there's no default.
     * @param preferredTextLanguage The preferred language for text tracks as defined by RFC 5646.
     *     {@code null} to select the default track, or first track if there's no default.
     * @param allowMixedMimeAdaptiveness Whether to allow selections to contain mixed mime types.
     * @param allowNonSeamlessAdaptiveness Whether non-seamless adaptation is allowed.
     * @param maxVideoWidth Maximum allowed video width.
     * @param maxVideoHeight Maximum allowed video height.
     * @param maxVideoBitrate Maximum allowed video bitrate.
     * @param exceedVideoConstraintsIfNecessary Whether to exceed video constraints when no
     *     selection can be made otherwise.
     * @param exceedRendererCapabilitiesIfNecessary Whether to exceed renderer capabilities when no
     *     selection can be made otherwise.
     * @param viewportWidth Viewport width in pixels.
     * @param viewportHeight Viewport height in pixels.
     * @param orientationMayChange Whether orientation may change during playback.
     * @param okHttpClient Set your OkHttp client if you want use it.
     * @param dataSourceCreatorInterface Set your data source if you want use it.
     */
    data class UriBuilder(
            val uri: Uri,
            val userAgent: String,
            val preferredAudioLanguage: String? = null,
            val preferredTextLanguage: String? = null,
            val allowMixedMimeAdaptiveness: Boolean = false,
            val allowNonSeamlessAdaptiveness: Boolean = true,
            val maxVideoWidth: Int = Integer.MAX_VALUE,
            val maxVideoHeight: Int = Integer.MAX_VALUE,
            val maxVideoBitrate: Int = Integer.MAX_VALUE,
            val exceedVideoConstraintsIfNecessary: Boolean = true,
            val exceedRendererCapabilitiesIfNecessary: Boolean = true,
            val viewportWidth: Int = Integer.MAX_VALUE,
            val viewportHeight: Int = Integer.MAX_VALUE,
            val orientationMayChange: Boolean = true,
            val okHttpClient: OkHttpClient? = null,
            val dataSourceCreatorInterface: DataSourceCreatorInterface? = null
    ) {
        fun build(): DataSourceCreator = DataSourceCreator(uri, userAgent, preferredAudioLanguage,
                preferredTextLanguage, allowMixedMimeAdaptiveness, allowNonSeamlessAdaptiveness,
                maxVideoWidth, maxVideoHeight, maxVideoBitrate, exceedVideoConstraintsIfNecessary,
                exceedRendererCapabilitiesIfNecessary, viewportWidth, viewportHeight, orientationMayChange,
                okHttpClient, dataSourceCreatorInterface)
    }
}