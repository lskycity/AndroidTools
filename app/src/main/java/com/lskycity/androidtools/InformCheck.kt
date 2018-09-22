package com.lskycity.androidtools

import android.content.Context
import android.content.Intent
import android.text.TextUtils

import com.android.volley.Response
import com.android.volley.VolleyError
import com.lskycity.androidtools.app.ToolApplication
import com.lskycity.androidtools.utils.CharsetJsonRequest
import com.lskycity.support.utils.SharedPreUtils

import org.json.JSONException
import org.json.JSONObject

/**
 * check inform from server
 *
 * @author zhaofliu
 * @since 2/5/17
 */

object InformCheck {

    val TIME_GREP_CHECK_INFORM = (1000 * 60 * 60 * 24 * 2).toLong()
    val ACTION_INFORM_CHANGED = "com.lskycity.androidtools.action_inform_changed"


    fun shouldCheckInform(context: Context): Boolean {
        val lastCheckTime = SharedPreUtils.getString(context, AppConstants.SHARED_KEY_INFORM_CHECK_TIME)

        return TextUtils.isEmpty(lastCheckTime) || System.currentTimeMillis() - java.lang.Long.parseLong(lastCheckTime) > TIME_GREP_CHECK_INFORM
    }

    fun checkInform() {
        val jsonObjectRequest = CharsetJsonRequest(AppConstants.INFORM_URL, Response.Listener { jsonObject ->
            try {
                val latest = getInformFromSharedPreference(ToolApplication.get())

                val info = getInform(jsonObject)

                putToSharedPre(ToolApplication.get(), info)

                if (TextUtils.isEmpty(latest.id) || !TextUtils.equals(latest.id, info.id)) {
                    sendBroadcast()

                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { })


        ToolApplication.get().requestQueue.add(jsonObjectRequest)
    }


    @Throws(JSONException::class)
    fun getInform(jsonObject: JSONObject): Inform {
        val info = Inform()
        info.id = jsonObject.getString("inform_id")
        info.content = jsonObject.getString("inform_content")
        info.checkTime = System.currentTimeMillis().toString()
        return info
    }

    fun putToSharedPre(context: Context, info: Inform) {
        com.lskycity.support.utils.SharedPreUtils.putString(context, AppConstants.SHARED_KEY_INFORM_CHECK_TIME, info.checkTime)
        com.lskycity.support.utils.SharedPreUtils.putString(context, AppConstants.SHARED_KEY_INFORM_ID, info.id)
        com.lskycity.support.utils.SharedPreUtils.putString(context, AppConstants.SHARED_KEY_INFORM_CONTENT, info.content)
    }

    fun getInformFromSharedPreference(context: Context): Inform {
        val info = Inform()
        info.id = com.lskycity.support.utils.SharedPreUtils.getString(context, AppConstants.SHARED_KEY_INFORM_ID)
        info.content = com.lskycity.support.utils.SharedPreUtils.getString(context, AppConstants.SHARED_KEY_INFORM_CONTENT)
        info.checkTime = com.lskycity.support.utils.SharedPreUtils.getString(context, AppConstants.SHARED_KEY_INFORM_CHECK_TIME)
        return info
    }

    private fun sendBroadcast() {
        val intent = Intent()
        intent.action = ACTION_INFORM_CHANGED
        //        intent.putExtra(ACTION_INFORM_CHANGED, true);
        ToolApplication.get().sendBroadcast(intent)

    }


    class Inform {
        var id: String? = null
        var content: String? = null
        var checkTime: String? = null

        override fun toString(): String {
            return "Inform{" +
                    "id='" + id + '\''.toString() +
                    ", content='" + content + '\''.toString() +
                    ", checkTime='" + checkTime + '\''.toString() +
                    '}'.toString()
        }
    }
}
