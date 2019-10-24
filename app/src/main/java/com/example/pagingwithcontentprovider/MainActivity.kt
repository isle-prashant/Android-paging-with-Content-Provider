package com.example.pagingwithcontentprovider

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.loader.app.LoaderManager
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : FragmentActivity() {

    private var adapter: RecyclerAdapter? = null
    private val pagedListHelperClass = PagedListHelperClass<Int>()
    private val listDataProvider = PagedListDataProvider(pagedListHelperClass)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initClickListeners()
        initRecyclerView()
        initDataSource()

        LoaderManager.getInstance(this)
    }


    private fun initRecyclerView() {
        adapter = RecyclerAdapter(listDataProvider)
        recyclerView.adapter = adapter
    }

    private fun initDataSource() {
        val dataSourceFactory = CustomDataSourceFactory(Uri.parse(CustomContentProvider.CONTENT_URI),this,this)

        val config = Config(20, 80, maxSize = 200)
        val livePagedList = dataSourceFactory.toLiveData(config)
        livePagedList.observe(this, Observer<PagedList<Int>> {
            Log.d("change in data:", livePagedList.toString())
            pagedListHelperClass.submitList(it)
            listDataProvider.invalidate()
            adapter?.notifyDataSetChanged()
        })
    }

    private fun initClickListeners() {
        addElementsButton.setOnClickListener {

            val cursor = contentResolver.query(
                Uri.parse(CustomContentProvider.CONTENT_URI),
                null,
                null,
                null,
                null
            )
            var lastElement = cursor?.count
            cursor?.close()
            val i = countItems()
            if (lastElement == null) {
                return@setOnClickListener
            }
            val contentValues = ContentValues()
            contentValues.put(DatabaseHelper.col1, ++lastElement)
            contentResolver.insert(Uri.parse(CustomContentProvider.CONTENT_URI), contentValues)

        }
    }

    private fun countItems(): Int {
        val cursor = contentResolver.query(
            Uri.parse(CustomContentProvider.CONTENT_URI),
            arrayOf("count(*) AS count"), null, null, null
        )
        try {
            return if (cursor != null && cursor.moveToFirst()) {
                cursor.getInt(0)
            } else 0
        } finally {
            cursor?.close()
        }
    }

}
