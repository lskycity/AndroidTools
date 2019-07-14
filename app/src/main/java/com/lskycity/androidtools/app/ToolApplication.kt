package com.lskycity.androidtools.app

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/**
 * main application
 *
 */
class ToolApplication : Application() {

    val requestQueue: RequestQueue by lazy { Volley.newRequestQueue(this) }

    init {
        instance = this
    }

    companion object {
        private lateinit var instance: ToolApplication

        fun get(): ToolApplication {
            return instance
        }
    }
}
