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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.thelawhouse.Fragment.FragAddAppointment;
import com.thelawhouse.Fragment.FragAddNewCase;
import com.thelawhouse.Fragment.FragApproveList;
import com.thelawhouse.Fragment.FragCaseCalenderList;
import com.thelawhouse.Fragment.FragCompletedCases;
import com.thelawhouse.Fragment.FragFindCaseByMobile;
import com.thelawhouse.Fragment.FragNewsList;
import com.thelawhouse.Fragment.FragPhoneBookList;
import com.thelawhouse.Fragment.FragSegmentLawList;
import com.thelawhouse.Fragment.FragUnupdatedCases;
import com.thelawhouse.Fragment.FragUsefullLinkList;
import com.thelawhouse.Fragment.MyAppointmentsFragment;
import com.thelawhouse.Model.BroadcastMessageModel;
import com.thelawhouse.Model.ExpandedMenuModel;
import com.thelawhouse.R;
import com.thelawhouse.ScrollViewText;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.ActivityMainBinding;
import com.thelawhouse.databinding.CustomDialogBinding;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    ActivityMainBinding mBinding;
    MainActivity mContext = MainActivity.this;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.activity_main);
        String login_access = PreferenceHelper.getString(Constants.ADMIN_ACCESS, "");
        String[] separated = login_access.split(",", 2);
        String user_a = separated[0].trim();
        String user_b = separated[1].trim();
        if (PreferenceHelper.getString(Constants.USER_ID, "").equalsIgnoreCase(user_a) ||
                PreferenceHelper.getString(Constants.USER_ID, "").equalsIgnoreCase(user_b)) {
            mBinding.drawer.llApproveList.setVisibility(View.VISIBLE);
            mBinding.drawer.llNews.setVisibility(View.VISIBLE);
        } else {
            mBinding.drawer.llApproveList.setVisibility(View.GONE);
            mBinding.drawer.llNews.setVisibility(View.GONE);
        }
        if (PreferenceHelper.getString(Constants.LOGINTYPE, "").equalsIgnoreCase("guest")) {
            mBinding.drawer.llAddNewCase.setVisibility(View.GONE);
            mBinding.drawer.llViewAllCase.setVisibility(View.GONE);
            mBinding.drawer.llReminder.setVisibility(View.GONE);
            mBinding.drawer.llAppointment.setVisibility(View.GONE);
            mBinding.drawer.llFindCaseByMobile.setVisibility(View.GONE);
            mBinding.drawer.llFindCaseByDate.setVisibility(View.GONE);
            mBinding.drawer.llCaseCalender.setVisibility(View.GONE);
            mBinding.drawer.llCompletedCases.setVisibility(View.GONE);
            mBinding.drawer.llReminder.setVisibility(View.GONE);
            mBinding.drawer.tvAllAppointment.setVisibility(View.GONE);
            mBinding.drawer.llAboutUs.setVisibility(View.GONE);
        }
        //navigation Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                mContext, mBinding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.getString("caseId").equalsIgnoreCase("")) {
            FragmentManager fragmentManager2 = getSupportFragmentManager();
            if (fragmentManager2 != null) {
                Fragment fragment2 = new FragAddNewCase();
                Bundle bundle2 = new Bundle();
                bundle2.putString("caseId", bundle.getString("caseId"));
                fragment2.setArguments(bundle);
                fragmentManager2.beginTransaction().replace(R.id.contain_layout, fragment2).commit();
            }
        } else {
            selectFirstItemAsDefault();
        }

        mBinding.navView.setNavigationItemSelectedListener(mContext);
        mBinding.toolbar.ivNavigation.setOnClickListener(mContext);
        mBinding.toolbar.ivBack.setOnClickListener(mContext);
        mBinding.drawer.tvAddNewCase.setOnClickListener(mContext);
//        mBinding.drawer.tvViewAllCase.setOnClickListener(mContext);
        mBinding.drawer.tvLogout.setOnClickListener(mContext);
        mBinding.drawer.tvFindCaseByMobile.setOnClickListener(mContext);
        mBinding.drawer.tvFindCaseByDate.setOnClickListener(mContext);
//        mBinding.drawer.tvReminder.setOnClickListener(mContext);
        mBinding.drawer.tvAppointment.setOnClickListener(mContext);
        mBinding.drawer.tvAllAppointment.setOnClickListener(mContext);
        mBinding.drawer.tvUsefulLink.setOnClickListener(mContext);
        mBinding.drawer.tvPhoneBook.setOnClickListener(mContext);
        mBinding.drawer.tvSegment.setOnClickListener(mContext);
        mBinding.drawer.tvAboutUs.setOnClickListener(mContext);
        mBinding.drawer.tvCaseCalender.setOnClickListener(mContext);
        mBinding.drawer.tvCompletedCases.setOnClickListener(mContext);
        mBinding.drawer.tvUnUpdatedCases.setOnClickListener(mContext);
        mBinding.drawer.tvNews.setOnClickListener(mContext);
        mBinding.drawer.tvApproveList.setOnClickListener(mContext);
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
            mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.case_calender));
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            Fragment fragment = new FragCaseCalenderList();
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
        mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.case_calender));
    }

    public void ClickcableTrue() {
        mBinding.toolbar.ivBack.setColorFilter(getResources().getColor(R.color.white));
        mBinding.toolbar.ivBack.setEnabled(true);
    }

    public void editCase() {
        mBinding.toolbar.tvTitle.setText("Edit Case");
    }

    public void editAppointment() {
        mBinding.toolbar.tvTitle.setText("Edit Appointment");
    }

    public void addAppointment() {
        mBinding.toolbar.tvTitle.setText("Appointment");
    }

    public void editUsefullLink() {
        mBinding.toolbar.tvTitle.setText("Edit Usefull Link");
    }

    public void addUsefullLink() {
        mBinding.toolbar.tvTitle.setText("Add Usefull Link");
    }

    public void addCase() {
        mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.add_new_case));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivNavigation:
                if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mBinding.drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mBinding.drawerLayout.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.tvAddNewCase:
                mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.add_new_case));
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = new FragAddNewCase();
                fragmentManager.beginTransaction().replace(R.id.contain_layout, fragment).addToBackStack(null).commit();
                break;
            case R.id.tvCaseCalender:
                selectFirstItemAsDefault();
                break;
            case R.id.tvCompletedCases:
                mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.completed_cases));
                mBinding.toolbar.tvTitle.setTextColor(getResources().getColor(R.color.red));
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
                FragmentManager fragmentManager3 = getSupportFragmentManager();
                Fragment fragment3 = new FragCompletedCases();
                fragmentManager3.beginTransaction().replace(R.id.contain_layout, fragment3).addToBackStack(null).commit();
                break;
            case R.id.tvUnUpdatedCases:
                mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.unupdated_cases));
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
                FragmentManager fragmentManager10 = getSupportFragmentManager();
                Fragment fragment10 = new FragUnupdatedCases();
                fragmentManager10.beginTransaction().replace(R.id.contain_layout, fragment10).addToBackStack(null).commit();
                break;
            case R.id.tvNews:
                mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.news));
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
                FragmentManager fragmentManager11 = getSupportFragmentManager();
                Fragment fragment11 = new FragNewsList();
                fragmentManager11.beginTransaction().replace(R.id.contain_layout, fragment11).addToBackStack(null).commit();
                break;
            case R.id.tvLogout:
                logoutDialog();
                break;
            case R.id.tvFindCaseByMobile:
                mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.find_case_by_mobile));
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                Fragment fragment2 = new FragFindCaseByMobile();
                fragmentManager2.beginTransaction().replace(R.id.contain_layout, fragment2).addToBackStack(null).commit();
                break;
            case R.id.tvAppointment:
                mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.appointment));
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
                FragmentManager fragmentManager4 = getSupportFragmentManager();
                Fragment fragment4 = new FragAddAppointment();
                fragmentManager4.beginTransaction().replace(R.id.contain_layout, fragment4).addToBackStack(null).commit();
                break;
            case R.id.tvAllAppointment:
                mBinding.toolbar.tvTitle.setText("Appointments");
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
                FragmentManager fragmentManager5 = getSupportFragmentManager();
                Fragment fragment5 = new MyAppointmentsFragment();
                fragmentManager5.beginTransaction().replace(R.id.contain_layout, fragment5).addToBackStack(null).commit();
                break;
            case R.id.tvUsefulLink:
                mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.useful_link));
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
                FragmentManager fragmentManager6 = getSupportFragmentManager();
                Fragment fragment6 = new FragUsefullLinkList();
                fragmentManager6.beginTransaction().replace(R.id.contain_layout, fragment6).addToBackStack(null).commit();
                break;
            case R.id.tvPhoneBook:
                mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.phone_book));
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
                FragmentManager fragmentManager7 = getSupportFragmentManager();
                Fragment fragment7 = new FragPhoneBookList();
                fragmentManager7.beginTransaction().replace(R.id.contain_layout, fragment7).addToBackStack(null).commit();
                break;
            case R.id.tvSegment:
                mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.segments_of_law_and_legal));
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
                FragmentManager fragmentManager8 = getSupportFragmentManager();
                Fragment fragment8 = new FragSegmentLawList();
                fragmentManager8.beginTransaction().replace(R.id.contain_layout, fragment8).addToBackStack(null).commit();
                break;
            case R.id.tvApproveList:
                mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.approve_list));
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
                FragmentManager fragmentManager9 = getSupportFragmentManager();
                Fragment fragment9 = new FragApproveList();
                fragmentManager9.beginTransaction().replace(R.id.contain_layout, fragment9).addToBackStack(null).commit();
                break;
            case R.id.tvReminder:
               /* Intent intent = new Intent(mContext, CustomCalenderActivity.class);
                startActivity(intent);
                finish();*/
                break;
            case R.id.tvAboutUs:
                //Blank Link
                break;
            case R.id.ivBack:
                selectFirstItemAsDefault();
                break;
            default:
                break;
        }
    }

    public void changeFragment(String id) {
        FragmentManager fragmentManager2 = getSupportFragmentManager();
        if (fragmentManager2 != null) {
            Fragment fragment2 = new FragAddAppointment();
            Bundle bundle = new Bundle();
            bundle.putString("appointmentId", id);
            fragment2.setArguments(bundle);
            fragmentManager2.beginTransaction().replace(R.id.contain_layout, fragment2).commit();
        }
    }

    public void viewAppointment() {
        FragmentManager fragmentManager2 = getSupportFragmentManager();
        if (fragmentManager2 != null) {
            Fragment fragment2 = new MyAppointmentsFragment();
            fragmentManager2.beginTransaction().replace(R.id.contain_layout, fragment2).commit();
        }
    }
}
