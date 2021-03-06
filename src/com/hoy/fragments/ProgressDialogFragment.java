package com.hoy.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import com.hoy.R;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 5/30/13
 * Time: 10:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProgressDialogFragment extends DialogFragment {

    public ProgressDialogFragment() {


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.loading_text));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        // Disable the back and search button
        DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {


            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                return keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH;
            }


        };
        dialog.setOnKeyListener(keyListener);
        return dialog;
    }

}