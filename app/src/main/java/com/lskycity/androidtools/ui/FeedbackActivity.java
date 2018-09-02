package com.lskycity.androidtools.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lskycity.androidtools.AppConstants;
import com.lskycity.androidtools.R;
import com.lskycity.androidtools.app.BaseActivity;
import com.lskycity.androidtools.app.ToolApplication;
import com.lskycity.androidtools.apputils.Feedback;

import org.json.JSONObject;

/**
 * collect feedback from user
 *
 * @author zhaofliu
 * @since 1/6/17
 */

public class FeedbackActivity extends BaseActivity implements TextWatcher, View.OnClickListener {

    private static final int MAX_TEXT_COUNT = 125;

    private EditText feedback;
    private TextView textCount;

    private long clickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback = (EditText) findViewById(R.id.feedback_content);
        feedback.addTextChangedListener(this);
        textCount = (TextView) findViewById(R.id.text_count);


        TextView checkNewVersion = (TextView) findViewById(R.id.check_new_version);

        //link the check user guide activity.
        String checkNewVersionString = getString(R.string.check_new_version);
        Spannable checkNewVersionSpannable = new SpannableString(checkNewVersionString);
        checkNewVersionSpannable.setSpan(new UnderlineSpan(), 0, checkNewVersionString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        checkNewVersion.setText(checkNewVersionSpannable);

        checkNewVersion.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_send) {
            sendFeedback(feedback.getText().toString());
            return true;
        }
        return false;
    }


    private void sendFeedback(String content) {
        long currentTime = System.currentTimeMillis();
        if(currentTime-clickTime<2000) {
            return;
        }

        clickTime = currentTime;

        if(TextUtils.isEmpty(content.trim())) {
            Toast.makeText(FeedbackActivity.this, R.string.send_feedback_empty, Toast.LENGTH_LONG).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppConstants.FEEDBACK_URL, Feedback.obtain(this, content).toJSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Toast.makeText(FeedbackActivity.this, R.string.send_feedback_success, Toast.LENGTH_LONG).show();
                supportFinishAfterTransition();
            }
        },

        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(FeedbackActivity.this, getString(R.string.send_feedback_fail)+", "+volleyError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        ToolApplication.get().getRequestQueue().add(request);
        Toast.makeText(FeedbackActivity.this, R.string.sending_feedback, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void afterTextChanged(Editable s) {
        textCount.setText(s.length()+"/125");
        if(s.length() == MAX_TEXT_COUNT) {
            textCount.setTextColor(Color.RED);
        } else {
            textCount.setTextColor(getResources().getColor(R.color.text_color));
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.check_new_version) {
            AppConstants.startUrlWithCustomTab(this, AppConstants.MAIN_PAGE_URL);
        }
    }
}
