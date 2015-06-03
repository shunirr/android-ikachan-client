package jp.s5r.android.ikachanclient.app.ui;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getContentResId();

    protected void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(getContentResId(), fragment)
                .commit();
    }

    protected void pushFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(getContentResId(), fragment)
                .addToBackStack(null)
                .commit();
    }
}
