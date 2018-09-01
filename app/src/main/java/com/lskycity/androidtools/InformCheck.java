package com.lskycity.androidtools;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lskycity.androidtools.app.ToolApplication;
import com.lskycity.androidtools.utils.CharsetJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * check inform from server
 *
 * @author zhaofliu
 * @since 2/5/17
 */

public class InformCheck {

    public static final long TIME_GREP_CHECK_INFORM = 1000*60*60*24*2;
    public static final String ACTION_INFORM_CHANGED = "com.lskycity.androidtools.action_inform_changed";


    public static boolean shouldCheckInform(Context context) {
        String lastCheckTime = com.lskycity.support.utils.SharedPreUtils.getString(context, AppConstants.SHARED_KEY_INFORM_CHECK_TIME);

        return TextUtils.isEmpty(lastCheckTime)
                || (System.currentTimeMillis() - Long.parseLong(lastCheckTime)) > TIME_GREP_CHECK_INFORM;
    }

    public static void checkInform() {
        CharsetJsonRequest jsonObjectRequest = new CharsetJsonRequest(AppConstants.INFORM_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Inform latest = getInformFromSharedPreference(ToolApplication.get());

                    Inform info = getInform(jsonObject);

                    putToSharedPre(ToolApplication.get(), info);

                    if(TextUtils.isEmpty(latest.id) || !TextUtils.equals(latest.id, info.id)) {
                        sendBroadcast();

                    }

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


    public static Inform getInform(JSONObject jsonObject) throws JSONException {
        Inform info = new Inform();
        info.id = jsonObject.getString("inform_id");
        info.content = jsonObject.getString("inform_content");
        info.checkTime = String.valueOf(System.currentTimeMillis());
        return info;
    }

    public static void putToSharedPre(Context context, Inform info) {
        com.lskycity.support.utils.SharedPreUtils.putString(context, AppConstants.SHARED_KEY_INFORM_CHECK_TIME, info.checkTime);
        com.lskycity.support.utils.SharedPreUtils.putString(context, AppConstants.SHARED_KEY_INFORM_ID, info.id);
        com.lskycity.support.utils.SharedPreUtils.putString(context, AppConstants.SHARED_KEY_INFORM_CONTENT, info.content);
    }

    public static Inform getInformFromSharedPreference(Context context) {
        Inform info = new Inform();
        info.id = com.lskycity.support.utils.SharedPreUtils.getString(context, AppConstants.SHARED_KEY_INFORM_ID);
        info.content = com.lskycity.support.utils.SharedPreUtils.getString(context, AppConstants.SHARED_KEY_INFORM_CONTENT);
        info.checkTime = com.lskycity.support.utils.SharedPreUtils.getString(context, AppConstants.SHARED_KEY_INFORM_CHECK_TIME);
        return info;
    }

    private static void sendBroadcast() {
        Intent intent=new Intent();
        intent.setAction(ACTION_INFORM_CHANGED);
//        intent.putExtra(ACTION_INFORM_CHANGED, true);
        ToolApplication.get().sendBroadcast(intent);

    }


    public static class Inform {
        public String id;
        public String content;
        public String checkTime;

        @Override
        public String toString() {
            return "Inform{" +
                    "id='" + id + '\'' +
                    ", content='" + content + '\'' +
                    ", checkTime='" + checkTime + '\'' +
                    '}';
        }
    }
}
