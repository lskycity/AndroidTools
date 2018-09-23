package com.lskycity.androidtools.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.style.UnderlineSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.lskycity.androidtools.AppConstants
import com.lskycity.androidtools.R
import com.lskycity.androidtools.app.BaseActivity
import com.lskycity.androidtools.app.ToolApplication
import com.lskycity.androidtools.apputils.Feedback

/**
 * collect feedback from user
 *
 * @author zhaofliu
 * @since 1/6/17
 */

class FeedbackActivity : BaseActivity(), TextWatcher, View.OnClickListener {

    private lateinit var feedback: EditText
    private lateinit var textCount: TextView

    private var clickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        feedback = findViewById<View>(R.id.feedback_content) as EditText
        feedback.addTextChangedListener(this)
        textCount = findViewById<View>(R.id.text_count) as TextView


        val checkNewVersion = findViewById<View>(R.id.check_new_version) as TextView

        //link the check user guide activity.
        val checkNewVersionString = getString(R.string.check_new_version)
        val checkNewVersionSpannable = SpannableString(checkNewVersionString)
        checkNewVersionSpannable.setSpan(UnderlineSpan(), 0, checkNewVersionString.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        checkNewVersion.text = checkNewVersionSpannable

        checkNewVersion.setOnClickListener(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_feedback, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_send) {
            sendFeedback(feedback.text.toString())
            return true
        }
        return false
    }


    private fun sendFeedback(content: String) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - clickTime < 2000) {
            return
        }

        clickTime = currentTime

        if (TextUtils.isEmpty(content.trim { it <= ' ' })) {
            Toast.makeText(this@FeedbackActivity, R.string.send_feedback_empty, Toast.LENGTH_LONG).show()
            return
        }

        val request = JsonObjectRequest(Request.Method.POST, AppConstants.FEEDBACK_URL, Feedback.obtain(this, content).toJSONObject(), Response.Listener {
            Toast.makeText(this@FeedbackActivity, R.string.send_feedback_success, Toast.LENGTH_LONG).show()
            supportFinishAfterTransition()
        },

                Response.ErrorListener { volleyError -> Toast.makeText(this@FeedbackActivity, getString(R.string.send_feedback_fail) + ", " + volleyError.message, Toast.LENGTH_LONG).show() })

        ToolApplication.get().requestQueue.add(request)
        Toast.makeText(this@FeedbackActivity, R.string.sending_feedback, Toast.LENGTH_SHORT).show()

    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    @SuppressLint("SetTextI18n")
    override fun afterTextChanged(s: Editable) {
        textCount.text = s.length.toString() + "/125"
        if (s.length == MAX_TEXT_COUNT) {
            textCount.setTextColor(Color.RED)
        } else {
            textCount.setTextColor(ResourcesCompat.getColor(resources, R.color.text_color, theme))
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.check_new_version) {
            AppConstants.startUrlWithCustomTab(this, AppConstants.MAIN_PAGE_URL)
        }
    }

    companion object {
        private val MAX_TEXT_COUNT = 125
    }
}
