package jp.s5r.android.ikachanclient.app.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import jp.s5r.android.ikachanclient.app.activity.SendActivity;
import jp.s5r.android.ikachanclient.util.Config;

public class SelectChannelDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final EditText editText = new EditText(getActivity());
        if (Config.hasLastChannel(getActivity())) {
            editText.setText(Config.getLastChannel(getActivity()));
        }

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Channel")
                .setView(editText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String channel = editText.getText().toString();
                        Config.setLastChannel(getActivity(), channel);

                        Activity a = getActivity();
                        if (a != null && a instanceof SendActivity) {
                            ((SendActivity) a).setChannel(channel);
                            ((SendActivity) a).sendToIkachan();
                        }
                        dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Activity a = getActivity();
                        if (a != null) {
                            a.finish();
                        }
                        dismiss();
                    }
                })
                .create();

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }
}
