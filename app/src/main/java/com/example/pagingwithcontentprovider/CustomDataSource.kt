package com.example.pagingwithcontentprovider

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import androidx.paging.PositionalDataSource

interface DataSourceInvalidationCallback {
    fun onContentChanged()
}
abstract class CustomDataSource<T>(private val contentResolver: ContentResolver): PositionalDataSource<T>(), DataSourceInvalidationCallback {

    override fun onContentChanged() {
        this.invalidate()
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) {
        var cursor: Cursor? = null
        try {
            cursor = getData(params.loadSize, params.startPosition)
            cursor?.let {
                val list =  convertRows(cursor)
                callback.onResult(list)
            }

        } finally {
            cursor?.close()
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<T>) {
        var list = emptyList<T>()
        var totalCount = 0
        var firstLoadPosition = 0
        var cursor: Cursor? = null
        try {
            totalCount = countItems()
            if (totalCount != 0) {
                // bound the size requested, based on known count
                firstLoadPosition = computeInitialLoadPosition(params, totalCount)
                val firstLoadSize = computeInitialLoadSize(
                    params,
                    firstLoadPosition,
                    totalCount
                )

                cursor = getData(firstLoadSize, firstLoadPosition)
                cursor?.let {
                    val rows = convertRows(cursor)
                    list = rows
                }

            }
        } finally {
            cursor?.close()

        }

        callback.onResult(list, firstLoadPosition, totalCount)
    }


    private fun getData(limit: Int, offset: Int): Cursor? {
        val uri = Uri.parse(CustomContentProvider.CONTENT_URI).buildUpon()
            .appendQueryParameter(CustomContentProvider.QUERY_PARAMETER_LIMIT, limit.toString())
            .appendQueryParameter(CustomContentProvider.QUERY_PARAMETER_OFFSET, offset.toString())
            .build()
        return contentResolver.query(uri, null, null,null,null)

    }

    private fun countItems(): Int {

       val cursor = contentResolver.query(Uri.parse(CustomContentProvider.CONTENT_URI),
           arrayOf("count(*) AS count"), null,null,null)
        try {
            return if (cursor!= null && cursor.moveToFirst()) {
                cursor.getInt(0)
            } else 0
        } finally {
            cursor?.close()
        }
    }

    abstract fun convertRows(cursor: Cursor): List<T>
}