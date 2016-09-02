package lskycity.androidtools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by zhaofliu on 9/2/16.
 */
public class ConfigurationInfoActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_info);
        textView = (TextView) findViewById(R.id.network_info);

        fetchConfigInfo();
    }

    private void fetchConfigInfo() {
        textView.setText(getResources().getConfiguration().toString());
    }
}
