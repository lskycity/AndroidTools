package com.lskycity.androidtools

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.res.ResourcesCompat

/**
 * Created by zhaofliu on 2/5/17.
 */

object AppConstants {
    const val SHARED_KEY_INFORM_ID = "shared_key_inform_id"
    const val SHARED_KEY_INFORM_CONTENT = "shared_key_inform_content"
    const val SHARED_KEY_INFORM_CHECK_TIME = "shared_key_inform_check_time"


    const val INFORM_URL = "http://www.lskycity.com/androidtools/inform.json"
    const val CHECK_VERSION_URL = "http://www.lskycity.com/androidtools/latestversion.json"
    const val MAIN_PAGE_URL = "http://www.lskycity.com/androidtools/index.html"
    const val FEEDBACK_URL = "http://www.lskycity.com:5000/feedback"

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
