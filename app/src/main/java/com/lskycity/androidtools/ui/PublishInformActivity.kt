package com.lskycity.androidtools.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast

import com.lskycity.androidtools.InformCheck
import com.lskycity.androidtools.R
import com.lskycity.androidtools.app.BaseActivity

/**
 * Created by zhaofliu on 2/5/17.
 *
 */

class PublishInformActivity : BaseActivity() {

    private lateinit var informReceiver: PublishInformActivity.InformReceiver;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inform)

        updateContent()

        val filter = IntentFilter(InformCheck.ACTION_INFORM_CHANGED)
        informReceiver = InformReceiver();
        registerReceiver(informReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(informReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.refresh, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.refresh) {
            Toast.makeText(this, R.string.refreshing, Toast.LENGTH_LONG).show()
            InformCheck.checkInform()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateContent() {
        val inform = InformCheck.getInformFromSharedPreference(this)

        val content = findViewById<TextView>(R.id.content)
        content.text = inform.content
    }

    internal inner class InformReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) = updateContent();
    }
}
