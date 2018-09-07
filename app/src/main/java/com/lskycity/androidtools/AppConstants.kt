package com.lskycity.androidtools

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.res.ResourcesCompat

/**
 * Created by zhaofliu on 2/5/17.
 */

object AppConstants {
    val SHARED_KEY_INFORM_ID = "shared_key_inform_id"
    val SHARED_KEY_INFORM_CONTENT = "shared_key_inform_content"
    val SHARED_KEY_INFORM_CHECK_TIME = "shared_key_inform_check_time"


    val INFORM_URL = "http://www.lskycity.com/androidtools/inform.json"
    val CHECK_VERSION_URL = "http://www.lskycity.com/androidtools/latestversion.json"
    val MAIN_PAGE_URL = "http://www.lskycity.com/androidtools/index.html"
    val FEEDBACK_URL = "http://www.lskycity.com:5000/feedback"

    fun startUrlWithCustomTab(context: Context, url: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_arrow_back))
        builder.setToolbarColor(ResourcesCompat.getColor(context.resources, R.color.colorPrimary, context.theme))
        builder.addDefaultShareMenuItem()
        builder.setShowTitle(true)
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }
}
