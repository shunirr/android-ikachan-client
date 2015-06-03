package jp.s5r.android.ikachanclient.app.ui.send;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import jp.s5r.android.ikachanclient.R;
import jp.s5r.android.ikachanclient.app.ui.BaseActivity;

public class SendActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowTitleEnabled(false);
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
            ab.setCustomView(R.layout.action_bar);
        }

        addFragment(new SelectRoomFragment());
    }

    @Override
    protected int getContentResId() {
        return R.id.content;
    }
}
