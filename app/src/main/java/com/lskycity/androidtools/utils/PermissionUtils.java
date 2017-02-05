package com.lskycity.androidtools.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.Pair;

import com.lskycity.androidtools.BuildConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuzhaofeng on 12/7/15.
 *
 */
@TargetApi(Build.VERSION_CODES.M)
public class PermissionUtils {
    public static final String PHONE = Manifest.permission_group.PHONE;
    public static final String MICROPHONE = Manifest.permission_group.MICROPHONE;
    public static final String CONTACTS = Manifest.permission_group.CONTACTS;
    public static final String CAMERA = Manifest.permission_group.CAMERA;
    public static final String STORAGE = Manifest.permission_group.STORAGE;
    public static final String CALENDAR = Manifest.permission_group.CALENDAR;

    public static final int PERMISSION_PHONE_REQUEST = 0x10;

    public static final Map<String, String[]> PERMISSION_MAP = new HashMap<>(6);

    static {
        PERMISSION_MAP.put(PHONE, new String[]{Manifest.permission.READ_PHONE_STATE,});
        PERMISSION_MAP.put(MICROPHONE, new String[]{Manifest.permission.RECORD_AUDIO});
        PERMISSION_MAP.put(CONTACTS, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS});
        PERMISSION_MAP.put(CAMERA, new String[]{Manifest.permission.CAMERA});
        PERMISSION_MAP.put(STORAGE, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        PERMISSION_MAP.put(CALENDAR, new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR});
    }


    public static void requestPermission(@NonNull Activity activity, @NonNull String permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, PERMISSION_MAP.get(permission), requestCode);
    }

    public static boolean requestPermissions(@NonNull Activity activity, int requestCode, @NonNull String... permissions) {
        ArrayList<String> arrayList = new ArrayList<>(8);
        for(String permissionGroup: permissions) {
            if(!checkPermission(activity, permissionGroup)) {
                arrayList.addAll(Arrays.asList(PERMISSION_MAP.get(permissionGroup)));
            }
        }

        if(!arrayList.isEmpty()) {
            ActivityCompat.requestPermissions(activity, arrayList.toArray(new String[arrayList.size()]), requestCode);
            return true;
        } else {
            return false;
        }

    }

    public static Pair<String[], String[]> getPermissionsFromPermissionGroup(Context context, String[] group) {
        ArrayList<String> grantList = new ArrayList<>(8);
        ArrayList<String> notGrantList = new ArrayList<>(8);
        for(String permissionGroup: group) {
            if(checkPermission(context, permissionGroup)) {
                grantList.addAll(Arrays.asList(PERMISSION_MAP.get(permissionGroup)));
            } else {
                notGrantList.addAll(Arrays.asList(PERMISSION_MAP.get(permissionGroup)));
            }

        }
        return new Pair<>(grantList.toArray(new String[grantList.size()]), notGrantList.toArray(new String[notGrantList.size()]));
    }

    public static boolean checkPermission(@NonNull Context context, @NonNull String permission) {
        String[] permissions = PERMISSION_MAP.get(permission);
        for(String p : permissions) {
            if(ActivityCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean shouldShowRequestPermissionRationale(@NonNull Activity activity, @NonNull String permission) {
        String[] permissions = PERMISSION_MAP.get(permission);
        for(String p : permissions) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, p)) {
                return true;
            }
        }
        return false;
    }

    public static boolean verifyPermissions(int[] grantResults) {
        if(grantResults.length < 1){
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    public static void dispatchRequestListener(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, OnRequestPermissionsResultCallback callback) {
        if(permissions.length == grantResults.length) {
            ArrayList<String> grantList = new ArrayList<>();
            ArrayList<String> notGrantList = new ArrayList<>();
            for(int i=0; i<permissions.length; i++) {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    grantList.add(permissions[i]);
                } else {
                    notGrantList.add(permissions[i]);
                }
            }
            if(!grantList.isEmpty()) {
                callback.onRequestPermissionsAllow(requestCode, grantList.toArray(new String[grantList.size()]));
            }
            if(!notGrantList.isEmpty()) {
                callback.onRequestPermissionsDeny(requestCode, notGrantList.toArray(new String[notGrantList.size()]));
            }
        }
    }

    public static boolean isContainsPermission(String permission, String[] permissions) {
        String[] permissionGroup = PERMISSION_MAP.get(permission);
        for(String p1 : permissionGroup) {
            for(String p2 : permissions) {
                if(p1.equals(p2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void requestOverlayPermission(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:"+ BuildConfig.APPLICATION_ID));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
