package com.lskycity.androidtools

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.res.ConfigurationHelper
import androidx.fragment.app.Fragment

import com.lskycity.support.utils.ViewUtils

import java.util.ArrayList

/**
 * Created by zhaofliu on 10/1/16.
 *
 */

class ConfigurationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_config, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listView = ViewUtils.findViewById<ListView>(view, R.id.list_view)
        listView.adapter = ConfigAdapter(activity!!, fetchConfigInfo())
    }

    private fun fetchConfigInfo(): ArrayList<InfoBin> {

        val arrayList = ArrayList<InfoBin>()
        arrayList.add(getScreenResolution(activity!!))
        arrayList.add(getNavigationBarHeight(activity!!))
        arrayList.add(getStatusBarHeight(activity!!))
        arrayList.add(getLocals(resources))
        arrayList.add(getFontScale(resources))
        arrayList.add(getDensityDpi(resources))

        arrayList.add(getOthers(resources))

        return arrayList
    }


    internal inner class ConfigAdapter(context: Context, list: ArrayList<InfoBin>) : BaseAdapter() {

        private val arrayList = ArrayList<InfoBin>()
        private val inflater: LayoutInflater = LayoutInflater.from(context)


        init {
            arrayList.addAll(list)
        }


        override fun getCount(): Int = arrayList.size

        override fun getItem(position: Int): InfoBin = arrayList[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_config_list, parent, false)
            }
            val titleView = convertView!!.findViewById<View>(R.id.title) as TextView
            val summaryView = convertView.findViewById<View>(R.id.summary) as TextView
            titleView.text = getItem(position).name
            summaryView.text = getItem(position).value
            return convertView
        }
    }

    companion object {

        fun getNavigationBarHeight(activity: Activity): InfoBin {
            val resources = activity.resources
            val resourceId = resources.getIdentifier("navigation_bar_height",
                    "dimen", "android")
            val navigationBarHeight = resources.getDimensionPixelSize(resourceId)

            val name = activity.getString(R.string.navigation_bar_height)
            val value = navigationBarHeight.toString() + "(" + com.lskycity.support.utils.DensityUtils.pxTodp(activity, navigationBarHeight.toFloat()) + "dp)"
            return InfoBin(name, value)
        }

        fun getStatusBarHeight(activity: Activity): InfoBin {
            val resources = activity.resources
            val resourceId = resources.getIdentifier("status_bar_height",
                    "dimen", "android")
            val navigationBarHeight = resources.getDimensionPixelSize(resourceId)

            val name = activity.getString(R.string.status_bar_height)
            val value = navigationBarHeight.toString() + "(" + com.lskycity.support.utils.DensityUtils.pxTodp(activity, navigationBarHeight.toFloat()) + "dp)"
            return InfoBin(name, value)
        }

        fun getScreenResolution(activity: Activity): InfoBin {
            val wm = activity
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager

            val screenSize = Point()
            wm.defaultDisplay.getSize(screenSize)

            val res = activity.resources

            val name = res.getString(R.string.screen_resolution)
            val value = screenSize.x.toString() + " * " + screenSize.y + "(" + res.configuration.screenWidthDp + "dp * " + res.configuration.screenHeightDp + "dp)"

            return InfoBin(name, value)
        }


        fun getLocals(res: Resources): InfoBin {
            val name = res.getString(R.string.locals)
            val value : String
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                value = res.configuration.locales.toString()
            } else {
                value = res.configuration.locale.toString()
            }

            return InfoBin(name, value)
        }

        fun getFontScale(res: Resources): InfoBin {
            return InfoBin(res.getString(R.string.font_scale), res.configuration.fontScale.toString())
        }

        fun getDensityDpi(res: Resources): InfoBin {
            val name = res.getString(R.string.density_dpi)
            val value = ConfigurationHelper.getDensityDpi(res).toString()
            return InfoBin(name, value)
        }

        fun getOthers(res: Resources): InfoBin {
            val name = res.getString(R.string.others)
            val value = res.configuration.toString()
            return InfoBin(name, value)
        }
    }

}
