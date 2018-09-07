package com.lskycity.androidtools

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ConfigurationInfo
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.lskycity.androidtools.utils.ClassUtils
import com.lskycity.androidtools.utils.PermissionUtils
import com.lskycity.support.utils.DeviceUtils

import java.util.ArrayList
import java.util.Arrays


/**
 * Created by zhaofliu on 10/1/16.
 *
 */

class DeviceFragment : Fragment(), LoaderManager.LoaderCallbacks<ArrayList<String>> {

    private var deviceName: TextView? = null
    private var androidVersion: TextView? = null
    private var imei: TextView? = null

    private var support32: TextView? = null
    private var support64: TextView? = null
    private var cpuFeature: TextView? = null

    private var backend: TextView? = null
    private var front: TextView? = null
    private var grantCameraPermission: Button? = null

    private var more: TextView? = null
    private var glVersion: TextView? = null


    private val systemInfo: CharSequence
        get() {
            val sb = StringBuilder()
            sb.append("-------build info -------\n\n")
            ClassUtils.fetchClassInfo(sb, Build::class.java)
            sb.append('\n')

            sb.append("Radio version is ")
            sb.append(Build.getRadioVersion())
            sb.append('\n')
            sb.append('\n')
            sb.append("-------version info -------\n\n")
            ClassUtils.fetchClassInfo(sb, Build.VERSION::class.java)

            return sb
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_devices, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deviceName = view.findViewById(R.id.device_name) as TextView
        androidVersion = view.findViewById(R.id.android_version) as TextView
        imei = view.findViewById(R.id.imei) as TextView

        support32 = view.findViewById(R.id.support_32) as TextView
        support64 = view.findViewById(R.id.support_64) as TextView
        cpuFeature = view.findViewById(R.id.cpu_feature) as TextView

        backend = view.findViewById(R.id.backend) as TextView
        front = view.findViewById(R.id.front) as TextView
        grantCameraPermission = view.findViewById(R.id.grant_camera_permission) as Button
        grantCameraPermission!!.setOnClickListener { requestPermissions(arrayOf(Manifest.permission.CAMERA), permissionId)}

        more = view.findViewById(R.id.more) as TextView
        more!!.setOnClickListener {
            clickMore()
        }
        glVersion = view.findViewById(R.id.gl_version) as TextView

        updateInfo()
    }

    @SuppressLint("SetTextI18n")
    private fun updateInfo() {
        deviceName!!.text = Build.BRAND + " " + Build.MODEL
        androidVersion!!.text = getString(R.string.android_version) + ": " + Build.VERSION.RELEASE + " / " + Build.VERSION.SDK_INT
        if (PermissionUtils.checkPermission(activity!!, Manifest.permission.READ_PHONE_STATE)) {
            imei!!.text = "IMEI: " + DeviceUtils.getIMEI(activity!!)
        } else {
            imei!!.text = getString(R.string.serial) + ": " + Build.SERIAL
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            support32!!.text = "64 ABI: " + Arrays.toString(Build.SUPPORTED_64_BIT_ABIS)
        } else {
            support32!!.text = "ABI: " + Build.CPU_ABI
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            support64!!.text = "32 ABI: " + Arrays.toString(Build.SUPPORTED_32_BIT_ABIS)
        } else {
            //noinspection deprecated
            if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
                support64!!.text = "ABI2: " + Build.CPU_ABI2
            } else {
                support64!!.visibility = View.GONE
            }
        }

        cpuFeature!!.text = getCpuFeature()

        glVersion!!.text = getGlVersion()

        try {
            updateCameraInfo()
        } catch (e: Exception) {
            backend!!.text = "No Camera found."
            front!!.visibility = View.GONE
            grantCameraPermission!!.visibility = View.GONE
        }

    }

    private fun getGlVersion(): String {
        val am = context!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = am.deviceConfigurationInfo
        val glversion = info.reqGlEsVersion
        return (glversion shr 16).toString() + "." + (glversion and 0xffff).toString()
    }

    fun clickMore() {
        val builder = AlertDialog.Builder(activity!!)
        builder.setMessage(systemInfo)
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (PermissionUtils.verifyPermissions(grantResults)) {
            updateInfo()
        } else {
            Toast.makeText(activity, "No grant Permission", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateCameraInfo() {

        if (PermissionUtils.checkPermission(activity!!, Manifest.permission.CAMERA)) {
            loaderManager.initLoader(0, null, this)
            grantCameraPermission!!.visibility = View.GONE
            backend!!.setText(R.string.loading)
            front!!.setText(R.string.loading)

        } else {
            backend!!.setText(R.string.grant_camera_permission_msg)
            front!!.visibility = View.GONE
            grantCameraPermission!!.visibility = View.VISIBLE
        }

    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<String>> {
        return CameraParameterLoader(context!!)
    }

    override fun onLoadFinished(loader: Loader<ArrayList<String>>, data: ArrayList<String>) {

        if (data.size > 0) {
            backend!!.text = data[0]
            if (data.size > 1) {
                front!!.text = data[1]
                front!!.visibility = View.VISIBLE
            } else {
                front!!.visibility = View.GONE
            }
        } else {
            backend!!.setText(R.string.no_camera_found)
            front!!.visibility = View.GONE
        }

    }

    override fun onLoaderReset(loader: Loader<ArrayList<String>>) {

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    private external fun getCpuFeature(): String

    companion object {

        private val permissionId = 11


        init {
            System.loadLibrary("native-lib")
        }
    }
}
