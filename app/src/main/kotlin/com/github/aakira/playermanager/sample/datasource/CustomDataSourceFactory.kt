package com.github.aakira.playermanager.sample.datasource

import android.content.Context
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.TransferListener

class CustomDataSourceFactory(val context: Context, val listener: TransferListener<in DataSource>,
                              val dataSourceFactory: DataSource.Factory) : DataSource.Factory {

    override fun createDataSource(): DataSource {
        return PlayerDataSource(context, listener, dataSourceFactory.createDataSource())
    }
}