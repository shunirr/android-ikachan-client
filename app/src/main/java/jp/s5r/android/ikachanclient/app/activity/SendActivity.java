package jp.s5r.android.ikachanclient.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import org.json.JSONObject;

import jp.s5r.android.ikachanclient.App;
import jp.s5r.android.ikachanclient.app.dialog.SelectChannelDialog;
import jp.s5r.android.ikachanclient.util.Config;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SendActivity extends FragmentActivity {

    private ProgressDialog mProgressDialog;

    private String mChannel;
    private String mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Config.hasEndpoint(this)) {
            Toast.makeText(this, "Set ikachan endpoint.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        if (intent.getAction().equals(Intent.ACTION_SEND)
                && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (!TextUtils.isEmpty(text)) {
                setMessage(text);
                selectChannel();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    public void setChannel(String channel) {
        this.mChannel = channel;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    private void selectChannel() {
        SelectChannelDialog selectDialog = new SelectChannelDialog();
        selectDialog.show(getSupportFragmentManager(), "dialog");
    }

    public void sendToIkachan() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

        App.getInstance().getIkachanApi().notice(mChannel, mMessage, new Callback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject, Response response) {
                mProgressDialog.dismiss();
                showSuccessMessage();
                SendActivity.this.finish();
            }

            @Override
            public void failure(RetrofitError error) {
                mProgressDialog.dismiss();
                showFailedMessage(error);
                SendActivity.this.finish();
            }
        });
    }

    private void showSuccessMessage() {
        Toast.makeText(this, "Ikachan success", Toast.LENGTH_SHORT).show();
    }

    private void showFailedMessage(Throwable e) {
        if (e != null) {
            Toast.makeText(this, "Ikachan failed: " + e.toString(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ikachan failed", Toast.LENGTH_SHORT).show();
        }
    }
}
