package com.example.pagingwithcontentprovider

data class PageResponse<T>(var list: List<T> ? = null,
                      var totalCount: Int? = null,
                      var startingIndex: Int? = null)