package com.example.pagingwithcontentprovider

import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.paging.DataSource
import java.util.*

class CustomDataSourceFactory(uri: Uri, val context: Context, val lifecycleOwner: LifecycleOwner ): DataSource.Factory<Int, Int>() {
    private var source: CustomDataSource<Int>? = null
    private var dataSourceInvalidator: DataSourceInvalidator = DataSourceInvalidator(uri, context, lifecycleOwner)
    override fun create(): DataSource<Int, Int> {
        val source: CustomDataSource<Int> = object: CustomDataSource<Int>(context.contentResolver) {
            override fun convertRows(cursor: Cursor): List<Int> {
                val list: ArrayList<Int> = arrayListOf()
                cursor.moveToFirst()
                while (cursor.isAfterLast() == false) {
//                    Log.d("elements", cursor.getString(0))
                    list.add(cursor.getInt(0))
                    cursor.moveToNext()
                }
                return list
            }
        }

        this.dataSourceInvalidator.registerDataSource(source)
        this.source = source
        return source
    }
}