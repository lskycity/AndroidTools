package com.lskycity.androidtools;

import android.content.Context;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaofliu
 * @since 9/2/18.
 */
public class CameraParameterLoader extends AsyncTaskLoader<List<String>> {

    private List<String> result;

    public CameraParameterLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    public void deliverResult(@Nullable List<String> data) {
        if (isReset()) {
            return;
        }
        List<String> oldData = result;
        result = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        if (oldData != null && oldData != data) {
            releaseData(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (result != null) {
            deliverResult(result);
        }

        if (takeContentChanged() || result == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if (result != null) {
            releaseData(result);
        }
    }

    private void releaseData(List<String> data) {
        if (data != null) {
            data.clear();
        }
    }

    @Nullable
    @Override
    public List<String> loadInBackground() {
        List<String> result = new ArrayList<>();
        int number = Camera.getNumberOfCameras();

        if(number > 0)  {
            Camera.CameraInfo info1 = new Camera.CameraInfo();
            Camera.getCameraInfo(0, info1);
            Camera.Size size1 = getCameraSize(0);
            if(info1.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                result.add("Back ("+size1.width+" * "+size1.height+")");
            } else {
                result.add("Front ("+size1.width+" * "+size1.height+")");
            }

            if(number>1) {
                Camera.CameraInfo info2 = new Camera.CameraInfo();
                Camera.getCameraInfo(1, info2);
                Camera.Size size2 = getCameraSize(1);
                if(info2.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    result.add("Back ("+size2.width+" * "+size2.height+")");
                } else {
                    result.add("Front ("+size2.width+" * "+size2.height+")");
                }
            }
        }

        this.result = result;
        return result;
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
}
