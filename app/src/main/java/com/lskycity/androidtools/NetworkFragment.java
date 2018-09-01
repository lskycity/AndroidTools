package com.lskycity.androidtools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lskycity.support.utils.DeviceUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by zhaofliu on 10/1/16.
 *
 */

public class NetworkFragment extends Fragment {

    private ArrayList<InfoBin> infoBinArrayList = new ArrayList<>();

    private NetAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_network_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter = new NetAdapter(getActivity()));
        listView.setEmptyView(view.findViewById(R.id.empty_view));
        updateNetworkInfo();
        setHasOptionsMenu(true);
    }

    private void updateNetworkInfo() {

        NetworkInfo networkInfo = getActiveNetworkInfo();
        infoBinArrayList.clear();
        if(networkInfo != null) {
            infoBinArrayList.add(getNetworkType(networkInfo));
            infoBinArrayList.add(getNetworkState(networkInfo));
            infoBinArrayList.add(getNetworkExtra(networkInfo));
            infoBinArrayList.add(getNetworkExtraInfo(networkInfo));
            infoBinArrayList.addAll(getNetworkIpAddressFormInterface());
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                infoBinArrayList.addAll(getNetworkIpAddressFormWifiManager(getActivity()));
            }
            infoBinArrayList.add(getDNSInfo());
        }
        adapter.notifyDataSetChanged();

    }

    private InfoBin getNetworkType(NetworkInfo networkInfo) {
        InfoBin bin = new InfoBin();
        bin.name = getString(R.string.network_type);
        bin.value = networkInfo.getTypeName();
        if(!TextUtils.isEmpty(networkInfo.getSubtypeName())) {
            bin.value += "["+networkInfo.getSubtypeName()+"]";
        }
        return bin;
    }

    private InfoBin getNetworkState(NetworkInfo networkInfo) {
        InfoBin bin = new InfoBin();
        bin.name = getString(R.string.network_state);
        bin.value = networkInfo.getState()+"/"+networkInfo.getDetailedState();
        return bin;
    }

    private InfoBin getNetworkExtra(NetworkInfo networkInfo) {
        InfoBin bin = new InfoBin();
        bin.name = getString(R.string.network_name);
        bin.value = networkInfo.getExtraInfo();
        return bin;
    }

    private InfoBin getNetworkExtraInfo(NetworkInfo networkInfo) {
        InfoBin bin = new InfoBin();
        bin.name = getString(R.string.network_extra_info);

        StringBuilder builder = new StringBuilder();
        if(networkInfo.isFailover()) {
            builder.append("failover, ");
        } else {
            builder.append("not failover, ");
        }

        if(networkInfo.isAvailable()) {
            builder.append("available, ");
        } else {
            builder.append("not available, ");
        }

        if(networkInfo.isRoaming()) {
            builder.append("roaming");
        } else {
            builder.append("not roaming");
        }

        bin.value = builder.toString();

        return bin;
    }

    private ArrayList<InfoBin> getNetworkIpAddressFormInterface() {

        ArrayList<InfoBin> list = new ArrayList<>(5);

        try {
            Enumeration<NetworkInterface> networkInterfaceList = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceList.hasMoreElements()) {
                NetworkInterface interfacess = networkInterfaceList.nextElement();
                String name = interfacess.getName();

                InfoBin infoBin = new InfoBin();
                infoBin.name = getString(R.string.network_interface)+"/"+name;
                infoBin.value = "";


                Enumeration<InetAddress> inetAddresses = interfacess.getInetAddresses();

                while (inetAddresses.hasMoreElements()) {
                    InetAddress address = inetAddresses.nextElement();
                    if(!address.isLoopbackAddress()) {

                        if(!TextUtils.isEmpty(infoBin.value)) {
                            infoBin.value += '\n';
                        }

                        String addressStr = address.getHostAddress();

                        int index;
                        if((index = addressStr.indexOf('%'))>0) {
                            addressStr = addressStr.substring(0, index);
                        }

                        infoBin.value += addressStr;
                    }
                }

                if(!TextUtils.isEmpty(infoBin.value)) {
                    list.add(infoBin);

                }


            }

        } catch (SocketException e) {
            e.printStackTrace();
        }

        return list;
    }

    private ArrayList<InfoBin> getNetworkIpAddressFormWifiManager(Context context) {

        ArrayList<InfoBin> list = new ArrayList<>(5);

        WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        DhcpInfo dhcpInfo = wm.getDhcpInfo();

        InfoBin ipBin = new InfoBin();
        ipBin.name = getString(R.string.wifi_ip_address);
        if(DeviceUtils.isInArc()) {
            ipBin.name += "("+getString(R.string.chromebook) +")";
        }
        ipBin.value = Formatter.formatIpAddress(dhcpInfo.ipAddress) +"/"+ip;
        list.add(ipBin);

        InfoBin gateway = new InfoBin();
        gateway.name = getString(R.string.gateway);
        gateway.value = Formatter.formatIpAddress(dhcpInfo.gateway);
        list.add(gateway);

        InfoBin netmask = new InfoBin();
        netmask.name = getString(R.string.netmask);
        netmask.value = Formatter.formatIpAddress(dhcpInfo.netmask);
        list.add(netmask);

        InfoBin dns1 = new InfoBin();
        dns1.name = getString(R.string.dns1);
        dns1.value = Formatter.formatIpAddress(dhcpInfo.dns1);
        list.add(dns1);

        InfoBin dns2 = new InfoBin();
        dns2.name = getString(R.string.dns2);
        dns2.value = Formatter.formatIpAddress(dhcpInfo.dns2);
        list.add(dns2);

        InfoBin serverAddress = new InfoBin();
        serverAddress.name = getString(R.string.server_address);
        serverAddress.value = Formatter.formatIpAddress(dhcpInfo.serverAddress);
        list.add(serverAddress);

        InfoBin leaseDuration = new InfoBin();
        leaseDuration.name = getString(R.string.lease_duration);
        leaseDuration.value = Formatter.formatIpAddress(dhcpInfo.leaseDuration);
        list.add(leaseDuration);

        return list;
    }

    private  InfoBin getDNSInfo() {
        InfoBin bin = new InfoBin();

        bin.name = getString(R.string.dns);
        Class<?> SystemProperties = null;
        try {
            SystemProperties = Class.forName("android.os.SystemProperties");
            Method method = SystemProperties.getMethod("get", new Class[] { String.class });
            ArrayList<String> servers = new ArrayList<String>();
            for (String name : new String[] { "net.dns1", "net.dns2", "net.dns3", "net.dns4", }) {
                String value = (String) method.invoke(null, name);
                if (value != null && !"".equals(value) && !servers.contains(value))
                    servers.add(value);
            }

            bin.value = servers.toString();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        return bin;
    }



    private NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connMgr.getActiveNetworkInfo();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.refresh) {
            Toast.makeText(getActivity(), R.string.refreshing, Toast.LENGTH_LONG).show();
            updateNetworkInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class NetAdapter extends BaseAdapter {

        private LayoutInflater inflater;


        NetAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return infoBinArrayList.size();
        }

        @Override
        public InfoBin getItem(int position) {
            return infoBinArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.item_config_list, parent, false);
            }
            TextView titleView = (TextView) convertView.findViewById(R.id.title);
            TextView summaryView = (TextView) convertView.findViewById(R.id.summary);
            titleView.setText(getItem(position).name);
            summaryView.setText(getItem(position).value);
            return convertView;
        }
    }

}
