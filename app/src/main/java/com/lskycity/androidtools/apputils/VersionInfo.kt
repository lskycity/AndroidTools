package com.lskycity.androidtools.apputils

import android.text.TextUtils

import com.lskycity.support.utils.DateUtils


/**
 * Created by zhaofliu on 1/2/17.
 *
 */

class VersionInfo {
    var packageName: String? = null
    var versionCode: Int = 0
    var versionName: String? = null
    var downloadUrl: String? = null
    var checkTime: String? = "0";

    val formatCheckTime: String
        get() = DateUtils.getTimeString(java.lang.Long.parseLong(checkTime))

    override fun toString(): String {
        return "VersionInfo{" +
                "packageName='" + packageName + '\''.toString() +
                ", versionCode='" + versionCode + '\''.toString() +
                ", versionName='" + versionName + '\''.toString() +
                ", downloadUrl='" + downloadUrl + '\''.toString() +
                '}'.toString()
    }

    fun setCheckTime(time: Long) {
        checkTime = time.toString()
    }

    fun setCurrentTimeToCheckTime() {
        checkTime = System.currentTimeMillis().toString()
    }

    fun getCheckTimeLong(): Long {
        return java.lang.Long.parseLong(checkTime)
    }

}
