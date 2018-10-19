package com.github.aakira.playermanager

import android.content.Context
import com.google.android.exoplayer2.upstream.DataSource

interface DataSourceCreatorInterface {
    fun create(context: Context, dataSourceFactory: DataSource.Factory): DataSource.Factory
}