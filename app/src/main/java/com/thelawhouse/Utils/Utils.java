package com.thelawhouse.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tapadoo.alerter.Alerter;
import com.thelawhouse.R;

public class Utils {

    public static void showDialog(Context context, String msg) {
       /* AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();*/
        Alerter.create((Activity) context)
                .setTitle(msg)
                .setBackgroundColorRes(R.color.black)
                .enableIconPulse(false)
                .hideIcon()
                .setDuration(3000)
                .show();
    }

    public static void showAlertDialogWithTwoClickListener(Context mActivity, String message, String mPositiveMsg, String mNagetiveMsg, DialogInterface.OnClickListener mPositiveListener, DialogInterface.OnClickListener mNagetiveListner) {
        new AlertDialog.Builder(mActivity)
                .setIcon(ContextCompat.getDrawable(mActivity, R.mipmap.ic_launcher))
                .setMessage(message)
                .setPositiveButton(mPositiveMsg, mPositiveListener)
                .setNegativeButton(mNagetiveMsg, mNagetiveListner)
                .setCancelable(false)
                .create()
                .show();
    }

    public static void onBackPressed(final Context mContext, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(mContext.getString(android.R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                ActivityCompat.finishAffinity((Activity) mContext);
                System.exit(0);
            }
        });
        alertDialog.setNegativeButton(mContext.getString(android.R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static void showToast(Context context, String text) {
        /*SuperActivityToast.create(context, new Style(), Style.TYPE_STANDARD)
                .setText(text)
                .setDuration(Style.DURATION_LONG)
                .setFrame(Style.FRAME_LOLLIPOP)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setAnimations(Style.ANIMATIONS_FADE).show();*/

        Alerter.create((Activity) context)
                .setTitle(text)
                .setBackgroundColorRes(R.color.black)
                .enableIconPulse(false)
                .hideIcon()
                .setDuration(3000)
                .show();
    }

    public static void showSettingsDialog(Context context) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.dialog_permission_title));
        builder.setMessage(context.getString(R.string.dialog_permission_message));
        builder.setPositiveButton(context.getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings(context);
        });
        builder.setNegativeButton(context.getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    public static void openSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        ActivityCompat.startActivityForResult((Activity) context, intent, 101, null);
    }
}
