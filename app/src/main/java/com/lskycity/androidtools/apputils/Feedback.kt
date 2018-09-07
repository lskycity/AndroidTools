package com.lskycity.androidtools.apputils

import android.content.Context
import android.os.Build

import com.lskycity.androidtools.BuildConfig
import com.lskycity.support.utils.DeviceUtils

import org.json.JSONException
import org.json.JSONObject

import java.util.HashMap

/**
 * Created by zhaofliu on 1/15/17.
 */

class Feedback {
    var deviceId: String? = null
    var description: String? = null
    var appVersion: String? = null
    var deviceBrand: String? = null
    var deviceMode: String? = null
    var deviceVersion: String? = null
    var feedbackTime: String? = null
    var applicationId: String? = null

    fun toJSONObject(): JSONObject {
        val map = HashMap<String, String?>()
        map["device_id"] = deviceId
        map["description"] = description
        map["app_version"] = appVersion
        map["device_brand"] = deviceBrand
        map["device_mode"] = deviceMode
        map["device_version"] = deviceVersion
        map["application_id"] = applicationId
        return JSONObject(map)
    }

    override fun toString(): String {
        return "Feedback{" +
                "deviceId='" + deviceId + '\''.toString() +
                ", description='" + description + '\''.toString() +
                ", appVersion='" + appVersion + '\''.toString() +
                ", deviceBrand='" + deviceBrand + '\''.toString() +
                ", deviceMode='" + deviceMode + '\''.toString() +
                ", deviceVersion='" + deviceVersion + '\''.toString() +
                ", applicationId='" + applicationId + '\''.toString() +
                '}'.toString()
    }

    companion object {

        fun obtain(jsonObject: JSONObject): Feedback {
            val `object` = Feedback()
            try {
                `object`.deviceId = jsonObject.getString("device_id")
                `object`.description = jsonObject.getString("description")
                `object`.appVersion = jsonObject.getString("app_version")
                `object`.deviceBrand = jsonObject.getString("device_brand")
                `object`.deviceMode = jsonObject.getString("device_mode")
                `object`.deviceVersion = jsonObject.getString("device_version")
                `object`.feedbackTime = jsonObject.getString("feedback_time")
                `object`.applicationId = jsonObject.getString("application_id")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return `object`
        }

        fun obtain(context: Context, description: String): Feedback {
            val `object` = Feedback()
            `object`.deviceId = DeviceUtils.getDeviceId(context)
            `object`.description = description
            `object`.appVersion = BuildConfig.VERSION_NAME
            `object`.deviceBrand = Build.BRAND
            `object`.deviceMode = Build.MODEL
            `object`.deviceVersion = Build.VERSION.SDK_INT.toString()
            `object`.applicationId = BuildConfig.APPLICATION_ID
            return `object`
        }
    }
}
