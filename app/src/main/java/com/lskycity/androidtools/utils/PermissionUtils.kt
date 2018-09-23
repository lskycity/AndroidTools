package com.lskycity.androidtools.utils


import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

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
        if (grantResults.isEmpty()) {
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
