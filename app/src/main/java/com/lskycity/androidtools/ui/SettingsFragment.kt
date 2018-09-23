package com.lskycity.androidtools.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

import com.lskycity.androidtools.BuildConfig
import com.lskycity.androidtools.R
import com.lskycity.androidtools.apputils.AppSharedPreUtils

/**
 * Created by zhaofliu on 2/5/17.
 */

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        preferenceManager.sharedPreferencesName = AppSharedPreUtils.SHARED_PREFERENCE_NAME

        val version = preferenceScreen.findPreference("app_version")
        version.summary = BuildConfig.VERSION_NAME

        val versionCode = com.lskycity.support.utils.SharedPreUtils.getInt(activity!!, AppSharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_CODE)
        if (versionCode > BuildConfig.VERSION_CODE) {
            val versionName = com.lskycity.support.utils.SharedPreUtils.getString(activity!!, AppSharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_NAME)
            version.summary = getString(R.string.current_version_and_have_new_version, BuildConfig.VERSION_NAME, versionName)

        }

    }
}
