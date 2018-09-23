package com.lskycity.androidtools

import android.content.Context
import android.hardware.Camera
import androidx.loader.content.AsyncTaskLoader

import java.util.ArrayList

/**
 * @author zhaofliu
 * @since 9/2/18.
 */
class CameraParameterLoader(context: Context) : AsyncTaskLoader<ArrayList<String>>(context) {

    private var result: ArrayList<String>? = null

    override fun deliverResult(data: ArrayList<String>?) {
        if (isReset) {
            return
        }
        val oldData = result
        result = data

        if (isStarted) {
            super.deliverResult(data)
        }

        if (oldData != null && oldData !== data) {
            releaseData(oldData)
        }
    }

    override fun onStartLoading() {
        if (result != null) {
            deliverResult(result)
        }

        if (takeContentChanged() || result == null) {
            forceLoad()
        }
    }

    override fun onStopLoading() {
        cancelLoad()
    }

    override fun onReset() {
        onStopLoading()
        if (result != null) {
            releaseData(result)
        }
    }

    private fun releaseData(data: ArrayList<String>?) {
        data?.clear()
    }

    override fun loadInBackground(): ArrayList<String>? {
        val result = ArrayList<String>()
        val number = Camera.getNumberOfCameras()

        if (number > 0) {
            val info1 = Camera.CameraInfo()
            Camera.getCameraInfo(0, info1)
            val size1 = getCameraSize(0)
            if (info1.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                result.add("Back (" + size1!!.width + " * " + size1.height + ")")
            } else {
                result.add("Front (" + size1!!.width + " * " + size1.height + ")")
            }

            if (number > 1) {
                val info2 = Camera.CameraInfo()
                Camera.getCameraInfo(1, info2)
                val size2 = getCameraSize(1)
                if (info2.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    result.add("Back (" + size2!!.width + " * " + size2.height + ")")
                } else {
                    result.add("Front (" + size2!!.width + " * " + size2.height + ")")
                }
            }
        }

        this.result = result
        return result
    }

    private fun getCameraSize(id: Int): Camera.Size? {
        val camera = Camera.open(id)
        val parameters = camera.parameters
        val supportedPictureSizes = parameters.supportedPictureSizes

        if (supportedPictureSizes.size > 0) {
            camera.release()
            return supportedPictureSizes[0]
        }
        return null
    }
}
