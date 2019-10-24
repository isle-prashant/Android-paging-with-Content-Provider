package com.example.pagingwithcontentprovider

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent


class DataSourceInvalidator(val uri: Uri, val context: Context, lifecycleOwner: LifecycleOwner) {
    var invalidateCallback: DataSourceInvalidationCallback? = null
    var contentObserver: ContentObserver? = object: ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            invalidateCallback?.onContentChanged()

        }
    }

    private val lifecycleObserver: LifecycleObserver = object: LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun pause() {
            contentObserver?.let { context.contentResolver.unregisterContentObserver(it) };
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun resume() {
            contentObserver?.let { context.contentResolver.registerContentObserver(uri, true, it) }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            contentObserver = null;
        }
    }

    init {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver);
    }

    fun registerDataSource(callback: DataSourceInvalidationCallback) {
        this.invalidateCallback = callback
    }
}
