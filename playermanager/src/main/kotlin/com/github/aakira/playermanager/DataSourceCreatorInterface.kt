package com.github.aakira.playermanager

import android.content.Context
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.TransferListener

interface DataSourceCreatorInterface {
    fun create(context: Context, listener: TransferListener<in DataSource>,
               dataSourceFactory: DataSource.Factory): DataSource.Factory
}