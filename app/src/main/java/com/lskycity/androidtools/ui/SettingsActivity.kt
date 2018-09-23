package com.lskycity.androidtools.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by zhaofliu on 2/5/17.
 *
 * @author zhaofliu
 * @since 2/5/17
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, SettingsFragment())
                    .commitAllowingStateLoss()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
