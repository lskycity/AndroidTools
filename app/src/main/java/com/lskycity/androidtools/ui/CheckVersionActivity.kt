package com.lskycity.androidtools.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.lskycity.androidtools.AppConstants
import com.lskycity.androidtools.BuildConfig
import com.lskycity.androidtools.R
import com.lskycity.androidtools.app.BaseActivity
import com.lskycity.androidtools.app.ToolApplication
import com.lskycity.androidtools.apputils.UpgradeUtils
import com.lskycity.androidtools.apputils.VersionInfo
import com.lskycity.support.utils.IntentUtils


import org.json.JSONException
import org.json.JSONObject

/**
 * Created by zhaofliu on 1/2/17.
 * @author zhaofliu
 */

class CheckVersionActivity : BaseActivity(), View.OnClickListener, View.OnLongClickListener {

    private var lastDateTextView: TextView? = null
    private var newVersionTipText: TextView? = null
    private var download: Button? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_version)

        val version = findViewById<View>(R.id.version_info) as TextView
        version.text = BuildConfig.VERSION_NAME

        findViewById<View>(R.id.check_version).setOnClickListener(this)
        findViewById<View>(R.id.forward_to_website).setOnClickListener(this)
        findViewById<View>(R.id.share_to_friend).setOnClickListener(this)

        download = findViewById<View>(R.id.new_version_download) as Button
        download!!.setOnClickListener(this)
        download!!.setOnLongClickListener(this)

        lastDateTextView = findViewById<View>(R.id.check_version_information) as TextView
        newVersionTipText = findViewById<View>(R.id.new_version_text) as TextView

        setupNewVersionArea(true)
    }

    @SuppressLint("SetTextI18n")
    private fun setupNewVersionArea(firstSetup: Boolean) {
        val versionInfo = UpgradeUtils.getVersionInfoFromSharedPreference(this)
        if (!TextUtils.isEmpty(versionInfo.checkTime)) {
            lastDateTextView!!.text = getString(R.string.last_check_date) + versionInfo.formatCheckTime
            if (versionInfo.versionCode > BuildConfig.VERSION_CODE) {

                newVersionTipText!!.visibility = View.VISIBLE

                download!!.visibility = View.VISIBLE
                download!!.tag = versionInfo.downloadUrl

                newVersionTipText!!.text = getString(R.string.have_new_version, versionInfo.versionName)

            } else if (firstSetup) {
                newVersionTipText!!.visibility = View.GONE
                download!!.visibility = View.GONE
            } else {
                newVersionTipText!!.visibility = View.VISIBLE
                newVersionTipText!!.setText(R.string.no_new_version)
                download!!.visibility = View.GONE
            }
        } else {
            lastDateTextView!!.text = getString(R.string.last_check_date) + getString(R.string.no_check_date)
            newVersionTipText!!.visibility = View.GONE
            download!!.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.check_version) {
            fetchLatestVersion()
        } else if (v.id == R.id.new_version_download) {
            IntentUtils.startUrl(this, v.tag as String)
        } else if (v.id == R.id.forward_to_website) {
            AppConstants.startUrlWithCustomTab(this, AppConstants.MAIN_PAGE_URL)
        } else if (v.id == R.id.share_to_friend) {
            IntentUtils.shareText(this, getString(R.string.share_to_friend), AppConstants.MAIN_PAGE_URL)
        }
    }

    private fun fetchLatestVersion() {
        val jsonObjectRequest = JsonObjectRequest(AppConstants.CHECK_VERSION_URL, null, Response.Listener { jsonObject ->
            try {
                val versionInfo = UpgradeUtils.getVersionInfo(jsonObject)
                UpgradeUtils.putToSharedPre(this@CheckVersionActivity, versionInfo)
                setupNewVersionArea(false)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { })

        ToolApplication.get().getRequestQueue().add(jsonObjectRequest)

    }


    override fun onLongClick(v: View): Boolean {
        if (v.id == R.id.new_version_download) {
            Toast.makeText(this, v.tag as String, Toast.LENGTH_LONG).show()
            return true
        }
        return false
    }
}
