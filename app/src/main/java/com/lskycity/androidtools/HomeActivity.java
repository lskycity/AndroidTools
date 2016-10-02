package com.lskycity.androidtools;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

/**
 * Created by zhaofliu on 16-9-13.
 */
public class HomeActivity extends AppCompatActivity implements OnMenuTabSelectedListener {

    private BottomBar bottomBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_container);

        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.bottom_bar, this);

        //bottomBar.getCurrentTabPosition()

        if(savedInstanceState == null) {
            applyFragment(DeviceFragment.class);
        }
    }

    private void applyFragment(Class<? extends Fragment> c) {
        Fragment fragment = Fragment.instantiate(this, c.getName());
        getSupportFragmentManager().beginTransaction().replace(R.id.content_container, fragment).commitAllowingStateLoss();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bottomBar.onSaveInstanceState(outState);
    }


    @Override
    public void onMenuItemSelected(@IdRes int menuItemId) {
        switch (menuItemId) {
            case R.id.nav_device_info:
                applyFragment(DeviceFragment.class);
                break;
            case R.id.nav_network_info:
                applyFragment(NetworkFragment.class);
                break;
            case R.id.nav_configuration:
                applyFragment(ConfigurationFragment.class);
                break;
            default:
                break;
        }
    }
}
