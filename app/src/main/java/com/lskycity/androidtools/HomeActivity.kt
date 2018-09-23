package com.lskycity.androidtools

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lskycity.androidtools.ui.DisclaimerActivity
import com.lskycity.androidtools.ui.SettingsActivity


/**
 * Created by zhaofliu on 16-9-13.
 *
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var bottomBar: BottomNavigationView

    private var informReceiver: InformReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_container)
        bottomBar = findViewById(R.id.bottomNavigation)

        bottomBar.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.nav_device_info -> applyFragment(DeviceFragment::class.java)
                R.id.nav_network_info -> applyFragment(NetworkFragment::class.java)
                R.id.nav_configuration -> applyFragment(ConfigurationFragment::class.java)
            }
        }

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
