package lskycity.androidtools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by zhaofliu on 9/1/16.
 */
public class NetworkInfoActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_info);
        textView = (TextView) findViewById(R.id.network_info);

        fetchNetworkInfo();
    }

    private void fetchNetworkInfo() {

        NetworkInfo networkInfo = getActiveNetworkInfo();

        if(networkInfo == null) {
            textView.setText("isOnline = false");
        } else {
            textView.setText("isOnline = "+ networkInfo.isConnected());
            textView.append("\n");
            textView.append("network type = " +networkInfo.getTypeName());
            textView.append("\n");
            textView.append("network sub type = " +networkInfo.getSubtypeName());
            textView.append("\n");
            textView.append("network state = " +networkInfo.getState());
            textView.append("\n");
            textView.append("network detail state = " +networkInfo.getDetailedState());
            textView.append("\n");
            textView.append("network reason = " +networkInfo.getReason());
            textView.append("\n");
            textView.append("network extra = " +networkInfo.getExtraInfo());
            textView.append("\n");
            textView.append("network = " +networkInfo.toString());

            WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

            textView.append("\n");
            textView.append("getDhcpInfo = " +wm.getDhcpInfo().toString());


            textView.append("\n");
            textView.append("network ip address = " +ip);

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

                textView.append("\n");
                textView.append("network servers address = " +servers);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }


            textView.append("\n");
            textView.append("getDhcpInfo = " +wm.getDhcpInfo().toString());

            try {
                Enumeration<NetworkInterface>  networkInterfaceList = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaceList.hasMoreElements()) {
                    NetworkInterface interfacess = networkInterfaceList.nextElement();
                    textView.append("\n");
                    textView.append("interfacess = " +interfacess.isVirtual()+", "+interfacess.toString());

                }

            } catch (SocketException e) {
                e.printStackTrace();
            }


        }
    }

    private NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        return connMgr.getActiveNetworkInfo();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.refresh) {
            fetchNetworkInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
