package com.github.aakira.playermanager.sample.datasource

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.Util
import java.io.IOException

class PlayerDataSource(context: Context, listener: TransferListener?,
                       private var baseDataSource: DataSource) : DataSource {

    init {
        addTransferListener(listener)
    }

    private var dataSource: DataSource? = null
    private val fileDataSource: DataSource = FileDataSource()
    private val assetDataSource: DataSource = AssetDataSource(context)
    private val contentDataSource: DataSource = ContentDataSource(context)

    override fun addTransferListener(transferListener: TransferListener?) {
        baseDataSource.addTransferListener(transferListener)
        fileDataSource.addTransferListener(transferListener)
        assetDataSource.addTransferListener(transferListener)
        contentDataSource.addTransferListener(transferListener)
    }

    @Throws(IOException::class)
    override fun open(dataSpec: DataSpec): Long {
        Assertions.checkState(dataSource == null)

        // Choose your data source
        val scheme = dataSpec.uri.scheme
        return when {
            Util.isLocalFileUri(dataSpec.uri) -> {
                if (dataSpec.uri.path.startsWith("/android_asset/")) assetDataSource else fileDataSource
            }
            scheme == "asset" -> assetDataSource
            scheme == "content" -> contentDataSource
            else -> baseDataSource
        }.apply {
            dataSource = this
        }.open(dataSpec)
    }

    @Throws(IOException::class)
    override fun read(buffer: ByteArray, offset: Int, readLength: Int): Int {
        return dataSource!!.read(buffer, offset, readLength)
    }

    override fun getUri(): Uri? {
        return dataSource!!.uri
    }

    @Throws(IOException::class)
    override fun close() {
        try {
            dataSource?.close()
        } finally {
            dataSource = null
        }
    }
}