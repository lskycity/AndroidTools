package com.lskycity.androidtools.utils

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest

import org.json.JSONException
import org.json.JSONObject

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

/**
 * Created by zhaofliu on 2/6/17.
 */

class CharsetJsonRequest(url: String, listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener) : JsonObjectRequest(url, null, listener, errorListener) {

    override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {

        return try {
            val jsonString = String(response.data, Charset.defaultCharset())
            Response.success(JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (je: JSONException) {
            Response.error(ParseError(je))
        }

    }
}
