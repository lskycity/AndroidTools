package com.lskycity.androidtools

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import com.lskycity.androidtools.ui.DisclaimerActivity
import com.lskycity.androidtools.ui.SettingsActivity
import com.roughike.bottombar.BottomBar
import com.roughike.bottombar.OnMenuTabSelectedListener

/**
 * Created by zhaofliu on 16-9-13.
 *
 */
class HomeActivity : AppCompatActivity(), OnMenuTabSelectedListener {

    private var bottomBar: BottomBar? = null

    private var informReceiver: InformReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_container)

        bottomBar = BottomBar.attach(this, savedInstanceState)
        bottomBar!!.setItemsFromMenu(R.menu.bottom_bar, this)

        //bottomBar.getCurrentTabPosition()

        if (savedInstanceState == null) {
            applyFragment(DeviceFragment::class.java)
        }

        val filter = IntentFilter(InformCheck.ACTION_INFORM_CHANGED)
        informReceiver = InformReceiver()
        registerReceiver(informReceiver, filter)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        if (DisclaimerActivity.shouldStartDisclaimerActivity(this)) {
            DisclaimerActivity.startDisclaimerActivity(this, DISCLAIMER_REQUEST_CODE)
        } else {
            if (InformCheck.shouldCheckInform(this@HomeActivity)) {
                InformCheck.checkInform()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DISCLAIMER_REQUEST_CODE && resultCode == RESULT_OK) {
            if (InformCheck.shouldCheckInform(this@HomeActivity)) {
                InformCheck.checkInform()
            }
        } else if (requestCode == DISCLAIMER_REQUEST_CODE) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(informReceiver)
    }

    private fun applyFragment(c: Class<out Fragment>) {
        val fragment = Fragment.instantiate(this, c.name)
        supportFragmentManager.beginTransaction().replace(R.id.content_container, fragment).commitAllowingStateLoss()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        bottomBar!!.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            openSettingsActivity()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun onMenuItemSelected(@IdRes menuItemId: Int) {
        when (menuItemId) {
            R.id.nav_device_info -> applyFragment(DeviceFragment::class.java)
            R.id.nav_network_info -> applyFragment(NetworkFragment::class.java)
            R.id.nav_configuration -> applyFragment(ConfigurationFragment::class.java)
            else -> {
            }
        }
    }

    internal inner class InformReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val inform = InformCheck.getInformFromSharedPreference(this@HomeActivity)
            val builder = AlertDialog.Builder(this@HomeActivity)
            builder.setMessage(inform.content)
            builder.setNegativeButton(android.R.string.cancel, null)
            builder.show()


        }
    }

    companion object {

        private val DISCLAIMER_REQUEST_CODE = 0x11
    }
}
