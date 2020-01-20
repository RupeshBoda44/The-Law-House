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

import com.thelawhouse.Activity.LoginActivity;
import com.thelawhouse.Activity.MainActivity;
import com.thelawhouse.R;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.databinding.CustomDialogBinding;
import com.thelawhouse.databinding.FragDashboardBinding;

public class FragDashboard extends Fragment {
    private FragDashboardBinding mBinding;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frag_dashboard, container, false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.ClickcableFalse();
        onClickListener();
        String login_access = PreferenceHelper.getString(Constants.ADMIN_ACCESS, "");
        String[] separated = login_access.split(",", 2);
        String user_a = separated[0].trim();
        String user_b = separated[1].trim();
        if (PreferenceHelper.getString(Constants.USER_ID, "").equalsIgnoreCase(user_a) ||
                PreferenceHelper.getString(Constants.USER_ID, "").equalsIgnoreCase(user_b)) {
            mBinding.llApproveList.setVisibility(View.VISIBLE);
            mBinding.llNews.setVisibility(View.VISIBLE);
        } else {
            mBinding.llApproveList.setVisibility(View.GONE);
            mBinding.llNews.setVisibility(View.GONE);
        }
        return mBinding.getRoot();
    }

    private void onClickListener() {
        mBinding.llCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new FragCase();
                fragmentManager.beginTransaction().replace(R.id.contain_layout, fragment).addToBackStack(null).commit();
            }
        });
        mBinding.llAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new FragAppointment();
                fragmentManager.beginTransaction().replace(R.id.contain_layout, fragment).addToBackStack(null).commit();
            }
        });
        mBinding.llServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new FragServices();
                fragmentManager.beginTransaction().replace(R.id.contain_layout, fragment).addToBackStack(null).commit();
            }
        });
        mBinding.llNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new FragNewsList();
                fragmentManager.beginTransaction().replace(R.id.contain_layout, fragment).addToBackStack(null).commit();
            }
        });
        mBinding.llPhoneBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new FragPhoneBookList();
                fragmentManager.beginTransaction().replace(R.id.contain_layout, fragment).addToBackStack(null).commit();
            }
        });
        mBinding.llBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new FragBroadcast();
                fragmentManager.beginTransaction().replace(R.id.contain_layout, fragment).addToBackStack(null).commit();
            }
        });
        mBinding.llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });
        mBinding.llApproveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new FragApproveList();
                fragmentManager.beginTransaction().replace(R.id.contain_layout, fragment).addToBackStack(null).commit();
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
        Utils.onBackPressed(getActivity(), getResources().getString(R.string.exit_dialog_text));
    }
}
