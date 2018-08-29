package com.lskycity.androidtools;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lskycity.androidtools.utils.DeviceUtils;
import com.lskycity.androidtools.utils.PermissionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhaofliu on 10/1/16.
 *
 */

public class DeviceFragment extends Fragment {

    private static final int permissionId = 11;

    @BindView(R.id.device_name) TextView deviceName;
    @BindView(R.id.android_version) TextView androidVersion;
    @BindView(R.id.imei) TextView imei;

    @BindView(R.id.cpu_abi) TextView cpuAbi;
    @BindView(R.id.support_32) TextView support32;
    @BindView(R.id.support_64) TextView support64;

    @BindView(R.id.backend) TextView backend;
    @BindView(R.id.front) TextView front;
    @BindView(R.id.grant_camera_permission) Button grantCameraPermission;

    @BindView(R.id.more) TextView more;
    @BindView(R.id.gl_version) TextView glVersion;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        updateInfo();
    }

    @SuppressLint("SetTextI18n")
    private void updateInfo() {
        deviceName.setText(Build.BRAND+" "+Build.MODEL);
        androidVersion.setText(getString(R.string.android_version)+": " + Build.VERSION.RELEASE + " / " + Build.VERSION.SDK_INT);
        if(PermissionUtils.checkPermission(getActivity(), PermissionUtils.PHONE)) {
            imei.setText("IMEI: " +DeviceUtils.getIMEI(getActivity()));
        } else {
            imei.setText(getString(R.string.serial)+": " + Build.SERIAL);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            support32.setText("64 ABI: "+Arrays.toString(Build.SUPPORTED_64_BIT_ABIS));
        } else {
            support32.setText("ABI: "+Build.CPU_ABI);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            support64.setText("32 ABI: "+Arrays.toString(Build.SUPPORTED_32_BIT_ABIS));
        } else {
            if(!TextUtils.isEmpty(Build.CPU_ABI2)) {
                support64.setText("ABI2: "+Build.CPU_ABI2);
            } else {
                support64.setVisibility(View.GONE);
            }
        }

        glVersion.setText(getGlVersion());

        try {
            updateCameraInfo();
        }catch (Exception e) {
            backend.setText("No Camera found.");
            front.setVisibility(View.GONE);
            grantCameraPermission.setVisibility(View.GONE);
        }

    }

    private String getGlVersion() {
        ActivityManager am =(ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        int glversion = info.reqGlEsVersion;
        return String.valueOf(glversion>>16)+"."+String.valueOf(glversion&0xffff);
    }

    @OnClick(R.id.more)
    public void clickMore(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getSystemInfo());
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(PermissionUtils.verifyPermissions(grantResults)) {
            updateInfo();
        } else {
            Toast.makeText(getActivity(), "No grant Permission", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.grant_camera_permission)
    public void clickGrantCameraPermission(View view) {
        requestPermissions(PermissionUtils.PERMISSION_MAP.get(PermissionUtils.CAMERA), permissionId);
    }

    private void updateCameraInfo() {

        if(PermissionUtils.checkPermission(getActivity(), PermissionUtils.CAMERA)) {

            int number = Camera.getNumberOfCameras();

            if(number > 0)  {
                Camera.CameraInfo info1 = new Camera.CameraInfo();
                Camera.getCameraInfo(0, info1);
                Camera.Size size1 = getCameraSize(0);
                if(info1.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    backend.setText("Back ("+size1.width+" * "+size1.height+")");
                } else {
                    backend.setText("Front ("+size1.width+" * "+size1.height+")");
                }

                if(number>1) {
                    Camera.CameraInfo info2 = new Camera.CameraInfo();
                    Camera.getCameraInfo(1, info2);
                    Camera.Size size2 = getCameraSize(1);
                    if(info2.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        front.setText("Back ("+size2.width+" * "+size2.height+")");
                    } else {
                        front.setText("Front ("+size2.width+" * "+size2.height+")");
                    }
                    front.setVisibility(View.VISIBLE);
                } else {
                    front.setVisibility(View.GONE);
                }
                grantCameraPermission.setVisibility(View.GONE);
            } else {
                backend.setText("No Camera found.");
                front.setVisibility(View.GONE);
                grantCameraPermission.setVisibility(View.GONE);
            }

        } else {
            backend.setText(R.string.grant_camera_permission_msg);
            front.setVisibility(View.GONE);
            grantCameraPermission.setVisibility(View.VISIBLE);
        }

    }

    private Camera.Size getCameraSize(int id) {
        Camera camera = Camera.open(id);
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();

        if(supportedPictureSizes.size()>0) {
            camera.release();
            return supportedPictureSizes.get(0);
        }
        return null;
    }

    private CharSequence getSystemInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------build info -------\n\n");
        fetchClassInfo(sb, Build.class);
        sb.append('\n');

        sb.append("Radio version is ");
        sb.append(Build.getRadioVersion());
        sb.append('\n');
        sb.append('\n');
        sb.append("-------version info -------\n\n");
        fetchClassInfo(sb, Build.VERSION.class);

        return sb;
    }

    private void fetchClassInfo(StringBuilder sb, Class<?> build) {
        Field[] fields = build.getDeclaredFields();
        for(Field fd : fields) {
            int mode = fd.getModifiers();
            if(Modifier.isPublic(mode) && Modifier.isStatic(mode)) {
                sb.append(fd.getName());
                sb.append('=');
                try {
                    Object value = fd.get(null);
                    if(value instanceof Object[]) {
                        sb.append(Arrays.toString((Object[]) value));
                    } else {
                        sb.append(value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                sb.append('\n');
            }
        }
    }

}
