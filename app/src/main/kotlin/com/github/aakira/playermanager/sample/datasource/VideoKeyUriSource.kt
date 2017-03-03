package com.github.aakira.playermanager.sample.datasource

import android.net.Uri
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import java.io.IOException
import javax.crypto.spec.SecretKeySpec

/**
 * This class is sample.
 * You implements decrypt method if your video in cipher.
 */
class VideoKeyUriSource(private val spec: SecretKeySpec) : DataSource {

    private var dataSpec: DataSpec? = null

    override fun getUri(): Uri {
        return dataSpec!!.uri
    }

    @Throws(IOException::class)
    override fun open(dataSpec: DataSpec): Long {
        return 0 // sample
    }

    @Throws(IOException::class)
    override fun close() {
    }

    @Throws(IOException::class)
    override fun read(buffer: ByteArray, offset: Int, readLength: Int): Int {
        return 0 // sample
    }
}