package com.lskycity.androidtools

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ConfigurationHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView

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

            val bin = InfoBin()
            bin.name = activity.getString(R.string.navigation_bar_height)
            bin.value = navigationBarHeight.toString() + "(" + com.lskycity.support.utils.DensityUtils.pxTodp(activity, navigationBarHeight.toFloat()) + "dp)"
            return bin
        }

        fun getStatusBarHeight(activity: Activity): InfoBin {
            val resources = activity.resources
            val resourceId = resources.getIdentifier("status_bar_height",
                    "dimen", "android")
            val navigationBarHeight = resources.getDimensionPixelSize(resourceId)

            val bin = InfoBin()
            bin.name = activity.getString(R.string.status_bar_height)
            bin.value = navigationBarHeight.toString() + "(" + com.lskycity.support.utils.DensityUtils.pxTodp(activity, navigationBarHeight.toFloat()) + "dp)"
            return bin
        }

        fun getScreenResolution(activity: Activity): InfoBin {
            val wm = activity
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager

            val screenSize = Point()
            wm.defaultDisplay.getSize(screenSize)

            val res = activity.resources

            val bin = InfoBin()
            bin.name = res.getString(R.string.screen_resolution)
            bin.value = screenSize.x.toString() + " * " + screenSize.y + "(" + res.configuration.screenWidthDp + "dp * " + res.configuration.screenHeightDp + "dp)"

            return bin
        }


        fun getLocals(res: Resources): InfoBin {
            val bin = InfoBin()
            bin.name = res.getString(R.string.locals)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bin.value = res.configuration.locales.toString()
            } else {
                bin.value = res.configuration.locale.toString()
            }

            return bin
        }

        fun getFontScale(res: Resources): InfoBin {
            val bin = InfoBin()
            bin.name = res.getString(R.string.font_scale)
            bin.value = res.configuration.fontScale.toString()
            return bin
        }

        fun getDensityDpi(res: Resources): InfoBin {
            val bin = InfoBin()
            bin.name = res.getString(R.string.density_dpi)
            bin.value = ConfigurationHelper.getDensityDpi(res).toString()
            return bin
        }

        fun getOthers(res: Resources): InfoBin {
            val bin = InfoBin()
            bin.name = res.getString(R.string.others)
            bin.value = res.configuration.toString()
            return bin
        }
    }

}
