package com.lskycity.androidtools

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.TextUtils
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

import com.lskycity.support.utils.DeviceUtils

import java.lang.reflect.InvocationTargetException
import java.net.NetworkInterface
import java.net.SocketException
import java.util.ArrayList

import android.content.Context.WIFI_SERVICE
import androidx.fragment.app.Fragment
import com.lskycity.support.utils.ViewUtils

/**
 * Created by zhaofliu on 10/1/16.
 *
 */

class NetworkFragment : Fragment() {

    private val infoBinArrayList = ArrayList<InfoBin>()

    private lateinit var adapter: NetAdapter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_network_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listView = view.findViewById<View>(R.id.list_view) as ListView
        adapter = NetAdapter(activity!!)
        listView.adapter = adapter
        listView.emptyView = view.findViewById(R.id.empty_view)
        updateNetworkInfo()
        setHasOptionsMenu(true)
    }

    private fun updateNetworkInfo() {

        val connMgr = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        infoBinArrayList.clear()
        if (networkInfo != null) {
            infoBinArrayList.add(getNetworkType(networkInfo))
            infoBinArrayList.add(getNetworkState(networkInfo))
            infoBinArrayList.add(getNetworkExtra(networkInfo))
            infoBinArrayList.add(getNetworkExtraInfo(networkInfo))
            infoBinArrayList.addAll(getNetworkIpAddressFormInterface())
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                infoBinArrayList.addAll(getNetworkIpAddressFormWifiManager(activity!!))
            }
            infoBinArrayList.add(getDnsInfo())
        }
        adapter.notifyDataSetChanged()

    }

    private fun getDnsInfo(): InfoBin {
        val bin = InfoBin()

        bin.name = getString(R.string.dns)
        var SystemProperties: Class<*>? = null
        try {
            SystemProperties = Class.forName("android.os.SystemProperties")
            val method = SystemProperties!!.getMethod("get", *arrayOf<Class<*>>(String::class.java))
            val servers = ArrayList<String>()
            for (name in arrayOf("net.dns1", "net.dns2", "net.dns3", "net.dns4")) {
                val value = method.invoke(null, name) as String
                if ("" != value && !servers.contains(value))
                    servers.add(value)
            }

            bin.value = servers.toString()

        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        return bin
    }

    private fun getNetworkIpAddressFormInterface(): ArrayList<InfoBin> {

        val list = ArrayList<InfoBin>(5)

        try {
            val networkInterfaceList = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaceList.hasMoreElements()) {
                val interfacess = networkInterfaceList.nextElement()
                val name = interfacess.name

                val infoBin = InfoBin()
                infoBin.name = getString(R.string.network_interface) + "/" + name
                infoBin.value = ""


                val inetAddresses = interfacess.inetAddresses

                while (inetAddresses.hasMoreElements()) {
                    val address = inetAddresses.nextElement()
                    if (!address.isLoopbackAddress) {

                        if (!TextUtils.isEmpty(infoBin.value)) {
                            infoBin.value += '\n'.toString()
                        }

                        var addressStr = address.hostAddress

                        val index: Int = addressStr.indexOf('%')
                        if (index > 0) {
                            addressStr = addressStr.substring(0, index)
                        }

                        infoBin.value += addressStr
                    }
                }

                if (!TextUtils.isEmpty(infoBin.value)) {
                    list.add(infoBin)

                }


            }

        } catch (e: SocketException) {
            e.printStackTrace()
        }

        return list
    }

    private fun getNetworkType(networkInfo: NetworkInfo): InfoBin {
        val bin = InfoBin()
        bin.name = getString(R.string.network_type)
        bin.value = networkInfo.typeName
        if (!TextUtils.isEmpty(networkInfo.subtypeName)) {
            bin.value += "[" + networkInfo.subtypeName + "]"
        }
        return bin
    }

    private fun getNetworkState(networkInfo: NetworkInfo): InfoBin {
        val bin = InfoBin()
        bin.name = getString(R.string.network_state)
        bin.value = networkInfo.state.toString() + "/" + networkInfo.detailedState
        return bin
    }

    private fun getNetworkExtra(networkInfo: NetworkInfo): InfoBin {
        val bin = InfoBin()
        bin.name = getString(R.string.network_name)
        bin.value = networkInfo.extraInfo
        return bin
    }

    private fun getNetworkExtraInfo(networkInfo: NetworkInfo): InfoBin {
        val bin = InfoBin()
        bin.name = getString(R.string.network_extra_info)

        val builder = StringBuilder()
        if (networkInfo.isFailover) {
            builder.append("failover, ")
        } else {
            builder.append("not failover, ")
        }

        if (networkInfo.isAvailable) {
            builder.append("available, ")
        } else {
            builder.append("not available, ")
        }

        if (networkInfo.isRoaming) {
            builder.append("roaming")
        } else {
            builder.append("not roaming")
        }

        bin.value = builder.toString()

        return bin
    }

    private fun getNetworkIpAddressFormWifiManager(context: Context): ArrayList<InfoBin> {

        val list = ArrayList<InfoBin>(7)

        val wm = context.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val ip = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)

        val dhcpInfo = wm.dhcpInfo

        var name = getString(R.string.wifi_ip_address)
        if (DeviceUtils.isInArc()) {
            name = name + "(" + getString(R.string.chromebook) + ")"
        }
        val value = Formatter.formatIpAddress(dhcpInfo.ipAddress) + "/" + ip
        list.add(InfoBin(name, value))

        val gateway = InfoBin(getString(R.string.gateway), Formatter.formatIpAddress(dhcpInfo.gateway))
        list.add(gateway)

        val netmask = InfoBin(getString(R.string.netmask), Formatter.formatIpAddress(dhcpInfo.netmask))
        list.add(netmask)

        val dns1 = InfoBin(getString(R.string.dns1), Formatter.formatIpAddress(dhcpInfo.dns1))
        list.add(dns1)

        val dns2 = InfoBin(getString(R.string.dns2), Formatter.formatIpAddress(dhcpInfo.dns2))
        list.add(dns2)

        val serverAddress = InfoBin(getString(R.string.server_address), Formatter.formatIpAddress(dhcpInfo.serverAddress))
        list.add(serverAddress)

        val leaseDuration = InfoBin(getString(R.string.lease_duration), Formatter.formatIpAddress(dhcpInfo.leaseDuration))
        list.add(leaseDuration)

        return list
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.refresh, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.refresh) {
            Toast.makeText(activity, R.string.refreshing, Toast.LENGTH_LONG).show()
            updateNetworkInfo()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    internal inner class NetAdapter(context: Context) : BaseAdapter() {

        override fun getCount(): Int = infoBinArrayList.size

        override fun getItem(position: Int): InfoBin = infoBinArrayList[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val rootView = convertView ?: ViewUtils.inflate(R.layout.item_config_list, parent, false);

            val titleView = rootView.findViewById<View>(R.id.title) as TextView
            val summaryView = rootView.findViewById<View>(R.id.summary) as TextView
            titleView.text = getItem(position).name
            summaryView.text = getItem(position).value
            return rootView
        }
    }

}
