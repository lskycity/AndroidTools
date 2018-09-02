package com.lskycity.androidtools;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.res.ResourcesCompat;

/**
 * Created by zhaofliu on 2/5/17.
 */

public class AppConstants {
    public static final String SHARED_KEY_INFORM_ID = "shared_key_inform_id";
    public static final String SHARED_KEY_INFORM_CONTENT = "shared_key_inform_content";
    public static final String SHARED_KEY_INFORM_CHECK_TIME = "shared_key_inform_check_time";



    public static final String INFORM_URL = "http://www.lskycity.com/androidtools/inform.json";
    public static final String CHECK_VERSION_URL = "http://www.lskycity.com/androidtools/latestversion.json";
    public static final String MAIN_PAGE_URL = "http://www.lskycity.com/androidtools/index.html";
    public static final String FEEDBACK_URL = "http://www.lskycity.com:5000/feedback";

    public static void startUrlWithCustomTab(Context context, String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_arrow_back));
        builder.setToolbarColor(ResourcesCompat.getColor(context.getResources(), R.color.colorPrimary, context.getTheme()));
        builder.addDefaultShareMenuItem();
        builder.setShowTitle(true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
