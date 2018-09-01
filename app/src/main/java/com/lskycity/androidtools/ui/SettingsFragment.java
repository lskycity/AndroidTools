package com.lskycity.androidtools.ui;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.lskycity.androidtools.BuildConfig;
import com.lskycity.androidtools.R;
import com.lskycity.androidtools.apputils.AppSharedPreUtils;

/**
 * Created by zhaofliu on 2/5/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        getPreferenceManager().setSharedPreferencesName(AppSharedPreUtils.SHARED_PREFERENCE_NAME);

        Preference version = getPreferenceScreen().findPreference("app_version");
        version.setSummary(BuildConfig.VERSION_NAME);

        int versionCode = com.lskycity.support.utils.SharedPreUtils.getInt(getActivity(), AppSharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_CODE);
        if(versionCode > BuildConfig.VERSION_CODE) {
            String versionName = com.lskycity.support.utils.SharedPreUtils.getString(getActivity(), AppSharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_NAME);
            version.setSummary(getString(R.string.current_version_and_have_new_version, BuildConfig.VERSION_NAME, versionName));

        }

    }
}
