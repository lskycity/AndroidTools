package com.lskycity.androidtools.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

import com.lskycity.androidtools.R
import com.lskycity.androidtools.apputils.AppSharedPreUtils
import com.lskycity.support.utils.AppUtils
import com.lskycity.support.utils.ViewUtils


class DisclaimerActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disclaimer)

        val buttonBar = findViewById<View>(R.id.buttonBar) as LinearLayout
        val agreeButton = findViewById<View>(R.id.agree) as Button
        val disagreeButton = findViewById<View>(R.id.disagree) as Button

        agreeButton.setOnClickListener(this)
        disagreeButton.setOnClickListener(this)

        val versionCode = com.lskycity.support.utils.SharedPreUtils.getInt(this, AppSharedPreUtils.KEY_LATEST_APP_VERSION_CODE)
        val currentCode = AppUtils.getVersionCode(this)
        ViewUtils.setVisible(buttonBar, currentCode > versionCode)

        supportActionBar?.setDisplayHomeAsUpEnabled(currentCode <= versionCode)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onBackPressed() {
        val versionCode = com.lskycity.support.utils.SharedPreUtils.getInt(this, AppSharedPreUtils.KEY_LATEST_APP_VERSION_CODE)
        val currentCode = AppUtils.getVersionCode(this)
        if (versionCode == currentCode) {
            super.onBackPressed()
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.agree) {
            com.lskycity.support.utils.SharedPreUtils.putInt(this, AppSharedPreUtils.KEY_LATEST_APP_VERSION_CODE, AppUtils.getVersionCode(this))
            setResult(Activity.RESULT_OK)
            supportFinishAfterTransition()
        } else if (v.id == R.id.disagree) {
            setResult(Activity.RESULT_CANCELED)
            supportFinishAfterTransition()
        }
    }

    companion object {

        fun shouldStartDisclaimerActivity(context: Context): Boolean {
            val versionCode = com.lskycity.support.utils.SharedPreUtils.getInt(context, AppSharedPreUtils.KEY_LATEST_APP_VERSION_CODE)
            val currentCode = AppUtils.getVersionCode(context)
            return currentCode > versionCode
        }

        fun startDisclaimerActivity(activity: Activity, requestCode: Int) {
            val intent = Intent(activity, DisclaimerActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
        }
    }
}
