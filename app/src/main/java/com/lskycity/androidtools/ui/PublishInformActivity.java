package com.lskycity.androidtools.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.lskycity.androidtools.AppConstants;
import com.lskycity.androidtools.R;
import com.lskycity.androidtools.utils.SharedPreUtils;

/**
 * Created by zhaofliu on 2/5/17.
 *
 */

public class PublishInformActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform);

        TextView content = (TextView) findViewById(R.id.content);
        content.setText(SharedPreUtils.getString(this, AppConstants.SHARED_KEY_INFORM_CONTENT));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
