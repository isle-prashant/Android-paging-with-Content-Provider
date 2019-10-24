package com.example.pagingwithcontentprovider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, 2) {


    override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL("create table $TABLE_NAME(value INTEGER)")
    }

    override fun onUpgrade(database: SQLiteDatabase?, p1: Int, p2: Int) {
        database?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(database)
    }

    companion object {
        const val DATABASE_NAME = "Integer.db"
        const val TABLE_NAME = "number_table"
        const val col1 = "value"
    }
}