package com.lskycity.androidtools;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Created by zhaofliu on 10/1/16.
 */

public class DeviceFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = (TextView) view.findViewById(R.id.text_view);
        textView.setText(getSystemInfo());
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
