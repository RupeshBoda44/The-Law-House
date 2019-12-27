package com.thelawhouse.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import com.thelawhouse.R;
import com.wang.avi.AVLoadingIndicatorView;


public class ProgressHUD extends Dialog {
    private static AVLoadingIndicatorView avi;

    public ProgressHUD(Context context) {
        super(context);
    }

    public ProgressHUD(Context context, int theme) {
        super(context, theme);
    }

    //Dialog show
    public static ProgressHUD show(Context context, boolean isShow, boolean indeterminate, boolean cancelable, OnCancelListener cancelListener) {
        ProgressHUD dialog = new ProgressHUD(context, R.style.ProgressHUD);
        if (isShow) {
            dialog.setTitle("");
            dialog.setContentView(R.layout.progress_hud);
            dialog.setCancelable(cancelable);
            dialog.setOnCancelListener(cancelListener);
            dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
            dialog.show();
        } else {
            dialog.dismiss();
        }

        return dialog;
    }

    // Dismiss dialog
    public void dismissProgressDialog(ProgressHUD progressHUD) {
        try {
            if (progressHUD != null)
                progressHUD.dismiss();
        } catch (Exception e) {

        }
    }
}
