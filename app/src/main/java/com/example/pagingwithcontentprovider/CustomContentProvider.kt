package com.example.pagingwithcontentprovider

import android.content.*
import android.database.Cursor
import android.net.Uri
import android.util.Log
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.icu.lang.UCharacter.GraphemeClusterBreak.T





class CustomContentProvider: ContentProvider() {
    private var contentResolver: ContentResolver? = null
    private var dbHelper: DatabaseHelper? = null

    companion object {
        private const val AUTHORITY = "com.example.pagingwithcontentprovider.customContentProvider"
        const val CONTENT_URI = "content://" + AUTHORITY  + "/" + DatabaseHelper.TABLE_NAME
        val QUERY_PARAMETER_LIMIT = "limit"
        val QUERY_PARAMETER_OFFSET = "offset"
    }
    
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var id: Long?
        context?.let {context ->
            val db = getDbHelper(context)?.writableDatabase
            id = db?.insert(DatabaseHelper.TABLE_NAME, null, values)
            contentResolver?.notifyChange(uri, null)
            Log.d("Insert::", "Insert successful")
            id?.let { return Uri.parse("$CONTENT_URI/$id") }
        }
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        var result: Cursor? = null
        val limit = uri.getQueryParameter(QUERY_PARAMETER_LIMIT)
        val offset = uri.getQueryParameter(QUERY_PARAMETER_OFFSET)

        context?.let { context ->
            val db = getDbHelper(context)?.writableDatabase
            if (limit != null && offset != null) {
                result = db?.query(
                    DatabaseHelper.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder,
                    "$offset,$limit"
                )
            } else {
                result = db?.query(
                    DatabaseHelper.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                )
            }
        }
        result?.setNotificationUri(contentResolver, uri)
        return result

    }

    override fun onCreate(): Boolean {
        initDB()
        return true
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return -1
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return -1
    }

    override fun getType(p0: Uri): String? {
        return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + DatabaseHelper.TABLE_NAME
    }

    private fun initDB() {
        val context = if (this.context == null) context else this.context
        if (context != null) {
            getDbHelper(context)
            contentResolver = context.contentResolver
        }
    }

    private fun getDbHelper(context: Context): DatabaseHelper? {
        if (dbHelper == null) {
            synchronized(DatabaseHelper::class.java) {
                if (dbHelper == null) {
                    dbHelper = DatabaseHelper(context)
                }
            }
        }
        return dbHelper
    }

    fun buildUriMatcher() {
        val matcher = UriMatcher(UriMatcher.NO_MATCH)
//        matcher.addURI()
    }

}