package com.example.pagingwithcontentprovider

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import androidx.paging.PageKeyedDataSource
import androidx.paging.PositionalDataSource

abstract class DataSourceWithPageKeyed<K, V>(val contentResolver: ContentResolver):PageKeyedDataSource<K, V>(), DataSourceInvalidationCallback {
    override fun onContentChanged() {
        this.invalidate()
    }

    override fun loadInitial(params: LoadInitialParams<K>, callback: LoadInitialCallback<K, V>) {
        TODO("need changes in cp")

    }

    override fun loadAfter(params: LoadParams<K>, callback: LoadCallback<K, V>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadBefore(params: LoadParams<K>, callback: LoadCallback<K, V>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun countItems(): Int {

        val cursor = contentResolver.query(
            Uri.parse(CustomContentProvider.CONTENT_URI),
            arrayOf("count(*) AS count"), null,null,null)
        try {
            return if (cursor!= null && cursor.moveToFirst()) {
                cursor.getInt(0)
            } else 0
        } finally {
            cursor?.close()
        }
    }
    abstract fun convertRows(cursor: Cursor)
}