package com.github.aakira.playermanager

import android.net.Uri
import okhttp3.OkHttpClient

class DataSourceCreator private constructor(val uri: Uri, val userAgent: String, val okHttpClient: OkHttpClient? = null,
                                            val dataSourceCreatorInterface: DataSourceCreatorInterface?) {
    data class UrlBuilder(
            val url: String,
            val userAgent: String,
            val okHttpClient: OkHttpClient? = null,
            val dataSourceCreatorInterface: DataSourceCreatorInterface? = null
    ) {
        fun build(): DataSourceCreator = DataSourceCreator(Uri.parse(url), userAgent, okHttpClient,
                dataSourceCreatorInterface)
    }

    data class UriBuilder(
            val uri: Uri,
            val userAgent: String,
            val okHttpClient: OkHttpClient? = null,
            val dataSourceCreatorInterface: DataSourceCreatorInterface? = null
    ) {
        fun build(): DataSourceCreator = DataSourceCreator(uri, userAgent, okHttpClient,
                dataSourceCreatorInterface)
    }
}