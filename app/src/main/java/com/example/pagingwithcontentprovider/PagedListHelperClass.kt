package com.example.pagingwithcontentprovider

import android.util.Log
import androidx.paging.PagedList

class PagedListHelperClass<T>(list: PagedList<T>? = null) {

    val pageSize = 40
    var mPagedList = list


    fun getItemsForPage(i: Int): ArrayList<T>? {
        val newPage = arrayListOf<T>()
        val startIndex = (i - 1) * pageSize
        val endIndex = i * pageSize
        for (index in startIndex..endIndex) {
            if (mPagedList == null || mPagedList?.size!! <= index ) {
                return null
            }
            mPagedList?.loadAround(index)
            mPagedList?.get(index)?.let { newPage.add(it) }
        }
        return newPage
    }

    fun getItemAt(position: Int): T? {
        if (mPagedList == null || mPagedList?.size!! <= position ) {
            return null
        }
        mPagedList?.loadAround(position)
        mPagedList?.get(position)?.let { return it } ?: return null
    }

    fun submitList(list: PagedList<T>?){
        mPagedList = list
    }

    fun getItemCount(): Int? {
        return mPagedList?.size
    }

    fun getItems(startingIndex: Int, count: Int): PageResponse<T> {
        val newPage = arrayListOf<T>()
        for (index in startingIndex until startingIndex + count){
            if (mPagedList == null || mPagedList?.size!! <= index ) {
                continue
            }
            mPagedList?.loadAround(index)
            mPagedList?.get(index)?.let {
                Log.d("inserted Element", it.toString())
                newPage.add(it) }

        }
        val pageResponse = PageResponse<T>(newPage, getItemCount(), startingIndex)
        validateList(pageResponse, count)
        return pageResponse
    }

    private fun validateList(list: PageResponse<T>, requestedSize: Int) {
        Log.d("pagesize", list.list?.size.toString())
    }

}