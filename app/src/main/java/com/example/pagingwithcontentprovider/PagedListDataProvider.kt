package com.example.pagingwithcontentprovider



class PagedListDataProvider(helperClass: PagedListHelperClass<Int>) {

    var lastFetchedResponse: PageResponse<Int>? = null
    var list: ArrayList<Int> = arrayListOf()
    var minimumFetchedIndex: Int? = null
    var maximumFetchedIndex: Int? = null
    val pageSize: Int = 20
    var firstPage: Int = 0
    var lastFetchedIndex = 0
    private val pagedListHelperClass: PagedListHelperClass<Int> ? = helperClass
    init {
//        getPage(firstPage)
    }

    fun invalidate() {
//        list.clear()
        getPage(firstPage)
        validateAvailableData(lastFetchedIndex)
    }

    fun getItemCount():Int {
        return lastFetchedResponse?.totalCount ?: pagedListHelperClass?.getItemCount() ?: 0
    }
    fun getItemAt(index: Int): Int? {
        var element: Int? = null
        if (index >= 0 && index < list.size) {
            element = list[index]
        }
        validateAvailableData(index)
        lastFetchedIndex = index
        return  element
    }

    fun validateAvailableData(index: Int){
        minimumFetchedIndex?.let { minimumIndex ->
            if (index - pageSize < minimumIndex) {
                fetchPreviousPage()
                return@validateAvailableData
            }
        }

        maximumFetchedIndex?.let {
            if (index + pageSize > it) {
                fetchNextPage()
               return@validateAvailableData
            }
        }

        if (minimumFetchedIndex == null && maximumFetchedIndex == null) {
            getPage(firstPage)
        }
    }

    private fun fetchNextPage(){
        getPage(maximumFetchedIndex ?: 0)
    }

    private fun fetchPreviousPage() {
        minimumFetchedIndex?.let {
            if (it > pageSize) { getPage(it - pageSize) }
        }
    }

    private fun getPage(startingIndex: Int) {
        val response = pagedListHelperClass?.getItems(startingIndex, pageSize)
        response?.list?.let { list -> this.list.addAll(response.startingIndex!!, list) }
        updateFetchedResponse(response)

    }

    private fun updateFetchedResponse(response: PageResponse<Int>?) {
        lastFetchedResponse = response
        response?.startingIndex?.let { startingIndex ->
            if (startingIndex < (minimumFetchedIndex ?: Integer.MAX_VALUE)) {
                minimumFetchedIndex = startingIndex
            }
            val list = response.list
            if (list != null && startingIndex + list.count() > (maximumFetchedIndex ?: Integer.MIN_VALUE) ){
                maximumFetchedIndex = startingIndex + list.count()
            }
        }
    }

}