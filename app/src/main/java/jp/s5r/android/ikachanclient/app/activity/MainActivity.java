package jp.s5r.android.ikachanclient.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import jp.s5r.android.ikachanclient.App;
import jp.s5r.android.ikachanclient.R;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText endpoint = (EditText) findViewById(R.id.main_edittext_endpoint);
        if (App.getInstance().hasEndpoint()) {
            endpoint.setText(App.getInstance().getEndpoint());
        }

        findViewById(R.id.main_button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endpointStr = endpoint.getText().toString();
                App.getInstance().setEndpoint(endpointStr);
            }
        });
    }
}
