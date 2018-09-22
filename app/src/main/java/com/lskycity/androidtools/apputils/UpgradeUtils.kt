package com.lskycity.androidtools.apputils


import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.lskycity.androidtools.AppConstants
import com.lskycity.androidtools.BuildConfig
import com.lskycity.androidtools.app.ToolApplication
import com.lskycity.support.utils.SharedPreUtils
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by zhaofliu on 1/3/17.
 */

object UpgradeUtils {

    private val CHECK_VERSION_TIME_GAP = (1000 * 60 * 60 * 24 * 7).toLong() //a week


    @Throws(JSONException::class)
    fun getVersionInfo(jsonObject: JSONObject): VersionInfo {
        val info = VersionInfo()
        info.packageName = jsonObject.getString("package_name")
        info.versionCode = jsonObject.getInt("version_code")
        info.versionName = jsonObject.getString("version_name")
        info.downloadUrl = jsonObject.getString("download_url")
        info.setCurrentTimeToCheckTime()
        return info
    }

    fun getVersionInfoFromSharedPreference(context: Context): VersionInfo {
        val info = VersionInfo()
        info.packageName = BuildConfig.APPLICATION_ID
        info.versionCode = SharedPreUtils.getInt(context, AppSharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_CODE)
        info.versionName = SharedPreUtils.getString(context, AppSharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_NAME)
        info.downloadUrl = SharedPreUtils.getString(context, AppSharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_URL)
        info.checkTime = SharedPreUtils.getString(context, AppSharedPreUtils.KEY_LAST_DATE_CHECK_VERSION)
        return info
    }


    fun putToSharedPre(context: Context, versionInfo: VersionInfo) {
        SharedPreUtils.putString(context, AppSharedPreUtils.KEY_LAST_DATE_CHECK_VERSION, versionInfo.checkTime)
        SharedPreUtils.putInt(context, AppSharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_CODE, versionInfo.versionCode)
        SharedPreUtils.putString(context, AppSharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_NAME, versionInfo.versionName)
        SharedPreUtils.putString(context, AppSharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_URL, versionInfo.downloadUrl)
    }

    fun checkVersionIfTimeOut() {

        val currentTime = System.currentTimeMillis()
        val versionInfo = getVersionInfoFromSharedPreference(ToolApplication.get())
        if (currentTime - versionInfo.getCheckTimeLong() > CHECK_VERSION_TIME_GAP) {
            val jsonObjectRequest = JsonObjectRequest(AppConstants.CHECK_VERSION_URL, null, Response.Listener { jsonObject ->
                try {
                    val info = UpgradeUtils.getVersionInfo(jsonObject)
                    UpgradeUtils.putToSharedPre(ToolApplication.get(), info)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { })

            ToolApplication.get().requestQueue.add(jsonObjectRequest)
        }

    }
}
