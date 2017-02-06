package com.lskycity.androidtools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.lskycity.androidtools.ui.DisclaimerActivity;
import com.lskycity.androidtools.ui.SettingsActivity;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

/**
 * Created by zhaofliu on 16-9-13.
 *
 */
public class HomeActivity extends AppCompatActivity implements OnMenuTabSelectedListener {

    private BottomBar bottomBar;

    private InformReceiver informReceiver;

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

        IntentFilter filter = new IntentFilter(InformCheck.ACTION_INFORM_CHANGED);
        registerReceiver(informReceiver = new InformReceiver(), filter);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(InformCheck.shouldCheckInform(HomeActivity.this)) {
            InformCheck.checkInform();
        }

        if(DisclaimerActivity.shouldStartDisclaimerActivity(this)) {
            DisclaimerActivity.startDisclaimerActivity(this, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode!=RESULT_OK) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(informReceiver);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_settings) {
            openSettingsActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
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

    class InformReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            System.out.println("11111111 onReceive");

            InformCheck.Inform inform = InformCheck.getInformFromSharedPreference(HomeActivity.this);

            System.out.println("11111111 onReceive " +inform);
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setMessage(inform.content);
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.show();


        }
    }
}
