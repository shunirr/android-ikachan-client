package jp.s5r.android.ikachanclient.app.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;

import jp.s5r.android.ikachanclient.app.ui.send.SendActivity;

public class BaseFragment extends Fragment {

    protected void setTitle(String title) {
        SendActivity a = getSendActivity();
        if (a != null) {
            a.setTitle(title);
        }
    }

    protected ActionBar getActionBar() {
        BaseActivity a = getBaseActivity();
        if (a != null) {
            return a.getSupportActionBar();
        }
        return null;
    }

    protected SendActivity getSendActivity() {
        FragmentActivity a = getActivity();
        if (a != null && a instanceof SendActivity) {
            return (SendActivity) a;
        }

        return null;
    }

    protected BaseActivity getBaseActivity() {
        FragmentActivity a = getActivity();
        if (a != null && a instanceof BaseActivity) {
            return (BaseActivity) a;
        }

        return null;
    }
}
