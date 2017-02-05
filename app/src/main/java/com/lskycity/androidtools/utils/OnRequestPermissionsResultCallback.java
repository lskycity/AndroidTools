package com.lskycity.androidtools.utils;

import android.support.annotation.NonNull;

/**
 * Created by liuzhaofeng on 12/7/15.
 */
public interface OnRequestPermissionsResultCallback {

    void onRequestPermissionsAllow(int requestCode, @NonNull String[] permissions);

    void onRequestPermissionsDeny(int requestCode, @NonNull String[] permissions);

}
