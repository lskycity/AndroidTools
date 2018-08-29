package com.lskycity.androidtools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ConfigurationHelper;
import android.support.v4.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lskycity.androidtools.utils.DensityUtils;

import java.util.ArrayList;

/**
 * Created by zhaofliu on 10/1/16.
 *
 */

public class ConfigurationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_config, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(new ConfigAdapter(getActivity(), fetchConfigInfo()));
    }

    private ArrayList<InfoBin> fetchConfigInfo() {

        ArrayList<InfoBin> arrayList = new ArrayList<>();
        arrayList.add(getScreenResolution(getActivity()));
        arrayList.add(getNavigationBarHeight(getActivity()));
        arrayList.add(getStatusBarHeight(getActivity()));
        arrayList.add(getLocals(getResources()));
        arrayList.add(getFontScale(getResources()));
        arrayList.add(getDensityDpi(getResources()));

        arrayList.add(getOthers(getResources()));

        return arrayList;
    }

    public static InfoBin getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        int navigationBarHeight = resources.getDimensionPixelSize(resourceId);

        InfoBin bin = new InfoBin();
        bin.name = activity.getString(R.string.navigation_bar_height);
        bin.value = navigationBarHeight +"(" + DensityUtils.pxTodp(activity, navigationBarHeight)+"dp)";
        return bin;
    }

    public static InfoBin getStatusBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height",
                "dimen", "android");
        int navigationBarHeight = resources.getDimensionPixelSize(resourceId);

        InfoBin bin = new InfoBin();
        bin.name = activity.getString(R.string.status_bar_height);
        bin.value = navigationBarHeight +"(" + DensityUtils.pxTodp(activity, navigationBarHeight)+"dp)";
        return bin;
    }

    public static InfoBin getScreenResolution(Activity activity) {
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);

        Point screenSize = new Point();
        wm.getDefaultDisplay().getSize(screenSize);

        Resources res = activity.getResources();

        InfoBin bin = new InfoBin();
        bin.name = res.getString(R.string.screen_resolution);
        bin.value = screenSize.x +" * " +screenSize.y + "("+res.getConfiguration().screenWidthDp +"dp * " +res.getConfiguration().screenHeightDp+"dp)";

        return bin;
    }


    public static InfoBin getLocals(Resources res) {
        InfoBin bin = new InfoBin();
        bin.name = res.getString(R.string.locals);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            bin.value = res.getConfiguration().getLocales().toString();
        } else {
            bin.value = res.getConfiguration().locale.toString();
        }

        return bin;
    }

    public static InfoBin getFontScale(Resources res) {
        InfoBin bin = new InfoBin();
        bin.name = res.getString(R.string.font_scale);
        bin.value = String.valueOf(res.getConfiguration().fontScale);
        return bin;
    }

    public static InfoBin getDensityDpi(Resources res) {
        InfoBin bin = new InfoBin();
        bin.name = res.getString(R.string.density_dpi);
        bin.value = String.valueOf(ConfigurationHelper.getDensityDpi(res));
        return bin;
    }

    public static InfoBin getOthers(Resources res) {
        InfoBin bin = new InfoBin();
        bin.name = res.getString(R.string.others);
        bin.value = res.getConfiguration().toString();
        return bin;
    }


    class ConfigAdapter extends BaseAdapter {

        private ArrayList<InfoBin> arrayList = new ArrayList<>();
        private LayoutInflater inflater;


        ConfigAdapter(Context context, ArrayList<InfoBin> list) {
            inflater = LayoutInflater.from(context);
            arrayList.addAll(list);
        }


        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public InfoBin getItem(int position) {
            return arrayList.get(position);
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
