package jp.s5r.android.ikachanclient.app.ui.send;

import android.os.Bundle;

import jp.s5r.android.ikachanclient.R;
import jp.s5r.android.ikachanclient.app.ui.BaseActivity;

public class SendActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        addFragment(new SelectRoomFragment());
    }

    @Override
    protected int getContentResId() {
        return R.id.content;
    }
}
