package com.lskycity.androidtools.utils

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.util.Pair

import com.lskycity.androidtools.BuildConfig

import java.util.ArrayList
import java.util.Arrays
import java.util.HashMap

/**
 * Created by liuzhaofeng on 12/7/15.
 *
 */
@TargetApi(Build.VERSION_CODES.M)
object PermissionUtils {

    fun checkPermission(context: Context, vararg permissions: String): Boolean {
        for (p in permissions) {
            if (ActivityCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun verifyPermissions(grantResults: IntArray): Boolean {
        if (grantResults.size < 1) {
            return false
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }


}
