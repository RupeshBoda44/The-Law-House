package com.thelawhouse.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.thelawhouse.Fragment.GFragDashboard;
import com.thelawhouse.Model.BroadcastMessageModel;
import com.thelawhouse.Model.ExpandedMenuModel;
import com.thelawhouse.R;
import com.thelawhouse.ScrollViewText;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.ActivityGmainBinding;
import com.thelawhouse.databinding.CustomDialogBinding;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;

public class GMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    ActivityGmainBinding mBinding;
    GMainActivity mContext = GMainActivity.this;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.activity_gmain);
//        mBinding.drawer.llAddNewCase.setVisibility(View.GONE);
//        mBinding.drawer.llViewAllCase.setVisibility(View.GONE);
//        mBinding.drawer.llReminder.setVisibility(View.GONE);
//        mBinding.drawer.llFindCaseByMobile.setVisibility(View.GONE);
//        mBinding.drawer.llFindCaseByDate.setVisibility(View.GONE);
//        mBinding.drawer.llCaseCalender.setVisibility(View.GONE);
//        mBinding.drawer.llCompletedCases.setVisibility(View.GONE);
//        mBinding.drawer.llUnUpdatedCases.setVisibility(View.GONE);
//        mBinding.drawer.llReminder.setVisibility(View.GONE);
//        mBinding.drawer.llAboutUs.setVisibility(View.GONE);
//        mBinding.drawer.llBroadcast.setVisibility(View.GONE);
//        navigation Drawer
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                mContext, mBinding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mBinding.drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
        selectFirstItemAsDefault();
//        mBinding.navView.setNavigationItemSelectedListener(mContext);
//        mBinding.toolbar.ivNavigation.setOnClickListener(mContext);
        mBinding.toolbar.ivBack.setOnClickListener(mContext);
//        mBinding.drawer.tvLogout.setOnClickListener(mContext);
//        mBinding.drawer.tvUsefulLink.setOnClickListener(mContext);
//        mBinding.drawer.tvPhoneBook.setOnClickListener(mContext);
//        mBinding.drawer.tvSegment.setOnClickListener(mContext);
//        mBinding.drawer.tvAppointment.setOnClickListener(mContext);
//        mBinding.drawer.tvAllAppointment.setOnClickListener(mContext);
//        mBinding.drawer.tvSegment.setOnClickListener(mContext);
        ScrollViewText scrollViewText = (ScrollViewText) findViewById(R.id.tvBottom);
        scrollViewText.startScroll();
        scrollViewText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    scrollViewText.pauseScroll();
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    scrollViewText.resumeScroll();
                    return true;
                }
                return false;
            }
        });
        broadCastMessage();
    }

    public void changeFragment(String id) {
//        FragmentManager fragmentManager2 = getSupportFragmentManager();
//        if (fragmentManager2 != null) {
//            Fragment fragment2 = new GFragAddAppointment();
//            Bundle bundle = new Bundle();
//            bundle.putString("appointmentId", id);
//            fragment2.setArguments(bundle);
//            fragmentManager2.beginTransaction().replace(R.id.contain_layout, fragment2).commit();
//        }
    }

    public void editAppointment() {
//        mBinding.toolbar.tvTitle.setText("Show Appointment");
    }

    public void viewAppointment() {
//        FragmentManager fragmentManager2 = getSupportFragmentManager();
//        if (fragmentManager2 != null) {
//            Fragment fragment2 = new GMyAppointmentsFragment();
//            fragmentManager2.beginTransaction().replace(R.id.contain_layout, fragment2).commit();
//        }
    }

    public void addAppointment() {
//        mBinding.toolbar.tvTitle.setText("Appointment");
    }

    private void broadCastMessage() {
        if (isInternetAvailable(mContext)) {
            ProgressHUD mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().BroadCastMessage().enqueue(new Callback<BroadcastMessageModel>() {
                @Override
                public void onResponse(Call<BroadcastMessageModel> call, Response<BroadcastMessageModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        if (response.body().message.equalsIgnoreCase("success")) {
                            mBinding.tvBottom.setText(response.body().broadcast_data);
                        }
                    }
                }

                @Override
                public void onFailure(Call<BroadcastMessageModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(mContext, "Check Your Internet.");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public void selectFirstItemAsDefault() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.app_name));
            Fragment fragment = new GFragDashboard();
            fragmentManager.beginTransaction().replace(R.id.contain_layout, fragment).addToBackStack(null).commit();
        }
    }

    private void logoutDialog() {
        final Dialog dialog = new Dialog(mContext);
        CustomDialogBinding mCustomBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.custom_dialog, null, false);
        dialog.setContentView(mCustomBinding.getRoot());
        dialog.setCancelable(false);
        mCustomBinding.dialogHeader.setText("Logout");
        mCustomBinding.dialogText.setText("Are You Sure you want to logout ?");
        mCustomBinding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceHelper.putBoolean(Constants.IS_LOGIN, false);
                PreferenceHelper.clearPreference();
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();
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

    public void ClickcableFalse() {
        mBinding.toolbar.ivBack.setColorFilter(getResources().getColor(R.color.colorPrimary));
        mBinding.toolbar.ivBack.setEnabled(false);
        mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.app_name));
    }

    public void ClickcableTrue() {
        mBinding.toolbar.ivBack.setColorFilter(getResources().getColor(R.color.white));
        mBinding.toolbar.ivBack.setEnabled(true);
    }

    public void appointmentTitle() {
        mBinding.toolbar.tvTitle.setText("Appointment");
    }

    public void serviceTitle() {
        mBinding.toolbar.tvTitle.setText("Service");
    }

    public void phoneBookTitle() {
        mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.phone_book));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.ivNavigation:
//                if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    mBinding.drawerLayout.closeDrawer(GravityCompat.START);
//                } else {
//                    mBinding.drawerLayout.openDrawer(GravityCompat.START);
//                }
//                break;
//            case R.id.tvLogout:
//                logoutDialog();
//                break;
//            case R.id.tvUsefulLink:
//                mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.useful_link));
//                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
//                FragmentManager fragmentManager6 = getSupportFragmentManager();
//                Fragment fragment6 = new GFragUsefullLinkList();
//                fragmentManager6.beginTransaction().replace(R.id.contain_layout, fragment6).addToBackStack(null).commit();
//                break;
//            case R.id.tvPhoneBook:
//                selectFirstItemAsDefault();
//                break;
//            case R.id.tvSegment:
//                mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.segments_of_law_and_legal));
//                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
//                FragmentManager fragmentManager8 = getSupportFragmentManager();
//                Fragment fragment8 = new GFragSegmentLawList();
//                fragmentManager8.beginTransaction().replace(R.id.contain_layout, fragment8).addToBackStack(null).commit();
//                break;
//            case R.id.tvAppointment:
//                mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.appointment));
//                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
//                FragmentManager fragmentManager4 = getSupportFragmentManager();
//                Fragment fragment4 = new GFragAddAppointment();
//                fragmentManager4.beginTransaction().replace(R.id.contain_layout, fragment4).addToBackStack(null).commit();
//                break;
//            case R.id.tvAllAppointment:
//                mBinding.toolbar.tvTitle.setText("Appointments");
//                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
//                FragmentManager fragmentManager5 = getSupportFragmentManager();
//                Fragment fragment5 = new GMyAppointmentsFragment();
//                fragmentManager5.beginTransaction().replace(R.id.contain_layout, fragment5).addToBackStack(null).commit();
//                break;
            case R.id.ivBack:
                selectFirstItemAsDefault();
                break;
            default:
                break;
        }
    }
}
