package com.lskycity.androidtools.ui;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.lskycity.androidtools.R;
import com.lskycity.androidtools.utils.AppUtils;
import com.lskycity.androidtools.utils.SharedPreUtils;

/**
 * Created by zhaofliu on 2/5/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        getPreferenceManager().setSharedPreferencesName(SharedPreUtils.SHARED_PREFERENCE_NAME);

        Preference version = getPreferenceScreen().findPreference("app_version");
        version.setSummary(AppUtils.getVersionName(getActivity()));

        int versionCode = SharedPreUtils.getInt(getActivity(), SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_CODE);
        if(versionCode > AppUtils.getVersionCode(getActivity())) {
            String versionName = SharedPreUtils.getString(getActivity(), SharedPreUtils.KEY_LAST_DATE_CHECK_VERSION_NAME);
            version.setSummary(getString(R.string.current_version_and_have_new_version, AppUtils.getVersionName(getActivity()), versionName));

        }

    }
}
