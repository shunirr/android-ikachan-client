package jp.s5r.android.ikachanclient.app.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import jp.s5r.android.ikachanclient.R;
import jp.s5r.android.ikachanclient.util.Config;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText endpoint = (EditText) findViewById(R.id.main_edittext_endpoint);
        if (Config.hasEndpoint(this)) {
            endpoint.setText(Config.getEndpoint(this));
        }
        final EditText prefix = (EditText) findViewById(R.id.main_edittext_prefix);
        if (Config.hasPrefix(this)) {
            prefix.setText(Config.getPrefix(this));
        }

        findViewById(R.id.main_button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endpointStr = endpoint.getText().toString();
                String prefixStr = prefix.getText().toString();
                Config.setEndpoint(MainActivity.this, endpointStr);
                Config.setPrefix(MainActivity.this, prefixStr);
                Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
