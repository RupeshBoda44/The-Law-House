package com.thelawhouse.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.thelawhouse.Activity.GMainActivity;
import com.thelawhouse.Activity.LoginActivity;
import com.thelawhouse.R;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.databinding.CustomDialogBinding;
import com.thelawhouse.databinding.GfragDashboardBinding;

public class GFragDashboard extends Fragment {
    private GfragDashboardBinding mBinding;
    private GMainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.gfrag_dashboard, container, false);
        mainActivity = (GMainActivity) getActivity();
        mainActivity.ClickcableFalse();
        onClickListener();
        mBinding.llApproveList.setVisibility(View.GONE);
        mBinding.llNews.setVisibility(View.GONE);
        return mBinding.getRoot();
    }

    private void onClickListener() {
        mBinding.llAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new GFragAppointment();
                fragmentManager.beginTransaction().replace(R.id.contain_layout, fragment).addToBackStack(null).commit();
            }
        });
        mBinding.llServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new GFragServices();
                fragmentManager.beginTransaction().replace(R.id.contain_layout, fragment).addToBackStack(null).commit();
            }
        });
        mBinding.llPhoneBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new GFragPhoneBookList();
                fragmentManager.beginTransaction().replace(R.id.contain_layout, fragment).addToBackStack(null).commit();
            }
        });
        mBinding.llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });
        mBinding.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getActivity().getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check this App The Law House at: https://play.google.com/store/apps/details?id=" + appPackageName);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    private void logoutDialog() {
        final Dialog dialog = new Dialog(getActivity());
        CustomDialogBinding mCustomBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.custom_dialog, null, false);
        dialog.setContentView(mCustomBinding.getRoot());
        dialog.setCancelable(false);
        mCustomBinding.dialogHeader.setText("Logout");
        mCustomBinding.dialogText.setText("Are You Sure you want to logout ?");
        mCustomBinding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceHelper.putBoolean(Constants.IS_LOGIN, false);
                PreferenceHelper.clearPreference();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                dialog.dismiss();


            }
        });
        mCustomBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    mainActivity.hideSoftKeyboard(getActivity(), v);
                    onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

    public void onBackPressed() {
//        mainActivity.selectFirstItemAsDefault();
        Utils.onBackPressed(getActivity(), getResources().getString(R.string.exit_dialog_text));

    }
}
