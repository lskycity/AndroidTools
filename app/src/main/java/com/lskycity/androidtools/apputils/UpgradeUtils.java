package com.lskycity.androidtools.apputils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lskycity.androidtools.AppConstants;
import com.lskycity.androidtools.BuildConfig;
import com.lskycity.androidtools.app.ToolApplication;
import com.lskycity.androidtools.utils.SharedPreUtils;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhaofliu on 1/3/17.
 */

public class UpgradeUtils {

    private static final long CHECK_VERSION_TIME_GAP = 1000 * 60 * 60 * 24 * 7; //a week


    public static VersionInfo getVersionInfo(JSONObject jsonObject) throws JSONException {
        VersionInfo info = new VersionInfo();
        info.packageName = jsonObject.getString("package_name");
        info.versionCode = jsonObject.getInt("version_code");
        info.versionName = jsonObject.getString("version_name");
        info.downloadUrl = jsonObject.getString("download_url");
        info.setCurrentTimeToCheckTime();
        return info;
    }

    public static VersionInfo getVersionInfoFromSharedPreference(Context context) {
        VersionInfo info = new VersionInfo();
        info.packageName = BuildConfig.APPLICATION_ID;
        info.versionCode = SharedPreUtils.getInt(context, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_CODE);
        info.versionName = SharedPreUtils.getString(context, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_NAME);
        info.downloadUrl = SharedPreUtils.getString(context, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_URL);
        info.checkTime = SharedPreUtils.getString(context, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION);
        return info;
    }


    public static void putToSharedPre(Context context, VersionInfo versionInfo) {
        SharedPreUtils.putString(context, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION, versionInfo.checkTime);
        SharedPreUtils.putInt(context, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_CODE, versionInfo.versionCode);
        SharedPreUtils.putString(context, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_NAME, versionInfo.versionName);
        SharedPreUtils.putString(context, SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_URL, versionInfo.downloadUrl);
    }

    public static void checkVersionIfTimeOut() {

        long currentTime = System.currentTimeMillis();
        VersionInfo versionInfo = getVersionInfoFromSharedPreference(ToolApplication.get());
        if( currentTime-versionInfo.getCheckTime() > CHECK_VERSION_TIME_GAP) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(AppConstants.CHECK_VERSION_URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        VersionInfo versionInfo = UpgradeUtils.getVersionInfo(jsonObject);
                        UpgradeUtils.putToSharedPre(ToolApplication.get(), versionInfo);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });

            ToolApplication.get().getRequestQueue().add(jsonObjectRequest);
        }

    }
}
