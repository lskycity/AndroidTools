package com.lskycity.androidtools.app

import android.app.Application
import android.content.Context

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/**
 * main application
 *
 */
class ToolApplication : Application() {

    private var requestQueue: RequestQueue? = null

    fun getRequestQueue(): RequestQueue {
        return requestQueue ?: synchronized(this) {
            requestQueue ?: Volley.newRequestQueue(this).also { requestQueue = it }
        }
    }

    init {
        instance = this
    }

    companion object {
        lateinit var instance: ToolApplication

        fun get(): ToolApplication {
            return instance
        }
    }
}
