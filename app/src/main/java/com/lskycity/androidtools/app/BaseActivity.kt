package com.lskycity.androidtools.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * base activity for common thing.
 *
 * @author zhaofliu
 * @since 2016/2/13
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (!super.onSupportNavigateUp()) {
            supportFinishAfterTransition()
            return true
        }
        return false
    }
}
