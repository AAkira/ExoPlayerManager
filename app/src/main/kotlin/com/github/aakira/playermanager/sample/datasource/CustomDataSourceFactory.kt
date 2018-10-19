package com.github.aakira.playermanager.sample.datasource

import android.content.Context
import com.google.android.exoplayer2.upstream.DataSource

class CustomDataSourceFactory(
        private val context: Context,
        private val dataSourceFactory: DataSource.Factory
) : DataSource.Factory {

    override fun createDataSource(): DataSource {
        return PlayerDataSource(context, dataSourceFactory.createDataSource())
    }
}