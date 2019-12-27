package com.thelawhouse.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.marcohc.robotocalendar.RobotoCalendarView;
import com.thelawhouse.Activity.MainActivity;
import com.thelawhouse.Adapter.CalenderCaseListAdapter;
import com.thelawhouse.ClickListener.PaginationScrollListener;
import com.thelawhouse.ClickListener.RecyclerViewClickListener;
import com.thelawhouse.ClickListener.RecyclerViewClickListener2;
import com.thelawhouse.ClickListener.RecyclerViewClickListener3;
import com.thelawhouse.Model.CaseListModel;
import com.thelawhouse.Model.DateCountModel;
import com.thelawhouse.Model.VerifyModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.ActivityCustomCalenderBinding;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;

public class FragCaseCalenderList extends Fragment implements RobotoCalendarView.RobotoCalendarListener {
    private ActivityCustomCalenderBinding mBinding;
    private MainActivity mainActivity;

    private int page = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private CalenderCaseListAdapter mAdapter;
    String finalNectDate = null;
    int currentSelectedMonth = 0;
    int currentSelectedYear = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.activity_custom_calender, container, false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.ClickcableFalse();
        mBinding.robotoCalendarPicker.setRobotoCalendarListener(this);
        Date date = mBinding.robotoCalendarPicker.getDate();
        Log.d("date", date.toString());
        int Month = Integer.parseInt((String) android.text.format.DateFormat.format("MM", date));
        currentSelectedMonth = Month;
        int Year = Integer.parseInt((String) android.text.format.DateFormat.format("yyyy", date));
        currentSelectedYear = Year;
        getMonthCaseCount(currentSelectedYear, currentSelectedMonth);
        return mBinding.getRoot();
    }

    private void getMonthCaseCount(int year, int month) {
        if (isInternetAvailable(getActivity())) {
            ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), true, true, false, null);
            WebApiClient.getInstance().ShowCountOnMonth(paramResendOTP(String.valueOf(year), String.valueOf(month))).enqueue(new Callback<DateCountModel>() {
                @Override
                public void onResponse(Call<DateCountModel> call, Response<DateCountModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        if (response.body().message.equalsIgnoreCase("success")) {
                            ArrayList<String> date = new ArrayList<>();
                            date.add(response.body().month_case_count.a1);
                            date.add(response.body().month_case_count.a2);
                            date.add(response.body().month_case_count.a3);
                            date.add(response.body().month_case_count.a4);
                            date.add(response.body().month_case_count.a5);
                            date.add(response.body().month_case_count.a6);
                            date.add(response.body().month_case_count.a7);
                            date.add(response.body().month_case_count.a8);
                            date.add(response.body().month_case_count.a9);
                            date.add(response.body().month_case_count.a10);
                            date.add(response.body().month_case_count.a11);
                            date.add(response.body().month_case_count.a12);
                            date.add(response.body().month_case_count.a13);
                            date.add(response.body().month_case_count.a14);
                            date.add(response.body().month_case_count.a15);
                            date.add(response.body().month_case_count.a16);
                            date.add(response.body().month_case_count.a17);
                            date.add(response.body().month_case_count.a18);
                            date.add(response.body().month_case_count.a19);
                            date.add(response.body().month_case_count.a20);
                            date.add(response.body().month_case_count.a21);
                            date.add(response.body().month_case_count.a22);
                            date.add(response.body().month_case_count.a23);
                            date.add(response.body().month_case_count.a24);
                            date.add(response.body().month_case_count.a25);
                            date.add(response.body().month_case_count.a26);
                            date.add(response.body().month_case_count.a27);
                            date.add(response.body().month_case_count.a28);
                            date.add(response.body().month_case_count.a29);
                            date.add(response.body().month_case_count.a30);
                            date.add(response.body().month_case_count.a31);
                            Calendar cal = Calendar.getInstance();
                            cal.clear();
                            cal.set(year, month - 1, 1);
                            int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                            ArrayList<String> strings = new ArrayList<>();
                            for (int i = 0; i < daysInMonth; i++) {
                                System.out.println((cal.getTime()));
                                strings.add(String.valueOf((cal.getTime())));
                                cal.add(Calendar.DAY_OF_MONTH, 1);
                            }

                            for (int index = 0; index < strings.size(); index++) {
                                for (int i = 0; i < date.size(); i++) {
                                    if (index == i) {
                                        if (!date.get(i).equalsIgnoreCase("0")) {
                                            String dtStart = strings.get(index);
                                            Format dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
                                            String pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();
                                            Log.d("pattern", pattern);
                                            SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy");
                                            try {
                                                Date date1 = format.parse(dtStart);
                                                System.out.println(date1);

                                                mBinding.robotoCalendarPicker.markCircleImage1(date1);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<DateCountModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(getActivity(), "Check Your Internet.");
        }
    }

    private Map<String, String> paramResendOTP(String s, String valueOf) {
        Map<String, String> params = new HashMap<>();
        params.put("year", s);
        params.put("month", valueOf);
        return params;
    }

    //    @Override
    public void onDayClick(Date date) {
//        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
        mBinding.tvSelectedDate.setText("Date : " + (date));
        Log.d("Date: ", String.valueOf((date)));
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date1;
        try {
            date1 = inputFormat.parse(String.valueOf(date));
            finalNectDate = outputFormat.format(date1);
            Log.d("finalNectDate", finalNectDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        loadDetail();
    }

    private void loadDetail() {
        page = 0;
        isLoading = false;
        isLastPage = false;
        setData();
    }

    private void setData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.rvAllCase.setLayoutManager(linearLayoutManager);
        mAdapter = new CalenderCaseListAdapter(getActivity(),
                new RecyclerViewClickListener() {
                    @Override
                    public void ImageViewListClicked(String mobileNum) {
                        Dexter.withActivity(getActivity())
                                .withPermissions(Manifest.permission.CALL_PHONE)
                                .withListener(new MultiplePermissionsListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                                        if (report.areAllPermissionsGranted()) {
                                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                                            callIntent.setData(Uri.parse("tel:" + mobileNum));
                                            startActivity(callIntent);
                                        }

                                        if (report.isAnyPermissionPermanentlyDenied()) {
                                            showSettingsDialog();
                                        }
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                        token.continuePermissionRequest();
                                    }
                                }).check();

                    }
                },
                new RecyclerViewClickListener2() {
                    @Override
                    public void ImageViewListClicked(String id) {
                        FragmentManager fragmentManager2 = getFragmentManager();
                        if (fragmentManager2 != null) {
                            Fragment fragment2 = new FragAddNewCase();
                            Bundle bundle = new Bundle();
                            bundle.putString("caseId", id);
                            fragment2.setArguments(bundle);
                            fragmentManager2.beginTransaction().replace(R.id.contain_layout, fragment2).commit();
                        }
                    }
                },
                new RecyclerViewClickListener3() {
                    @Override
                    public void ImageViewListClicked(String mobileNum) {
                        caseComplete(mobileNum);
                    }
                });
        mBinding.rvAllCase.setAdapter(mAdapter);

        mBinding.rvAllCase.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                if (!isLastPage) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadData(page);
                        }
                    }, 200);
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        loadData(page);
    }

    private void caseComplete(String linkId) {
        if (isInternetAvailable(getActivity())) {
            ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), true, true, false, null);
            WebApiClient.getInstance().ChangeCaseStatus(paramGetData(linkId)).enqueue(new Callback<VerifyModel>() {
                @Override
                public void onResponse(Call<VerifyModel> call, Response<VerifyModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        if (response.body().message.equalsIgnoreCase("success")) {
//                            onBackPressed();
                        } else {
//                            Utils.showDialog(getActivity(), response.body().message + "");
                        }
                    }
                }

                @Override
                public void onFailure(Call<VerifyModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(getActivity(), "Check Your Internet.");
        }
    }

    private Map<String, String> paramGetData(String linkId) {
        Map<String, String> params = new HashMap<>();
        params.put("case_id", linkId);
        params.put("status", "complete");
        return params;
    }


    private void loadData(int page) {
        if (isInternetAvailable(getActivity())) {
            final ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), true, true, false, null);
            WebApiClient.getInstance().FindCaseByDate(paramMyBooking(String.valueOf(page))).enqueue(new Callback<CaseListModel>() {
                @Override
                public void onResponse(Call<CaseListModel> call, Response<CaseListModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    assert response.body() != null;
                    if (response.code() == 200) {
                        if (response.body().message.equalsIgnoreCase("success")) {
                            mBinding.rvAllCase.setVisibility(View.VISIBLE);
                            mBinding.tvDataNotFound.setVisibility(View.GONE);
                            CaseListModel caseListModel = response.body();
                            resultAction(caseListModel);
                        }
                    }
                }

                @Override
                public void onFailure(Call<CaseListModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    mBinding.rvAllCase.setVisibility(View.GONE);
                    mBinding.tvDataNotFound.setVisibility(View.VISIBLE);
                    Log.e("error", t.getMessage());
                }
            });
        }
    }

    private void resultAction(CaseListModel myBookingModel) {
        isLoading = false;
        if (myBookingModel != null) {
            mAdapter.addItems(myBookingModel.collection_data);
            if (page == (Integer.parseInt(myBookingModel.total_pages) - 1)) {
                isLastPage = true;
            } else {
                page = page + 1;
            }
        }
    }

    private Map<String, String> paramMyBooking(String page) {
        Map<String, String> params = new HashMap<>();
        params.put("page_id", page);
        params.put("date", finalNectDate);
        params.put("limit", "5");
        Log.d("param", params.toString());
        return params;
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @SuppressLint("SetTextI18n")
    public void onDayClick(String date) {
        mBinding.tvSelectedDate.setText("Date : " + date);
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date1;
        try {
            date1 = inputFormat.parse(date);
            finalNectDate = outputFormat.format(date1);
            Log.d("finalNectDate", finalNectDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        loadDetail();
    }

    @Override
    public void onDayLongClick(Date date) {

    }

    @Override
    public void onRightButtonClick() {
        mBinding.tvSelectedDate.setText("");
        currentSelectedMonth = currentSelectedMonth + 1;
        if (currentSelectedMonth > 12) {
            currentSelectedMonth = 1;
            currentSelectedYear = currentSelectedYear + 1;
        }
        Log.d("currentSelectedMonth", currentSelectedMonth + "");
        Log.d("currentSelectedYear", currentSelectedYear + "");
        getMonthCaseCount(currentSelectedYear, currentSelectedMonth);
    }

    @Override
    public void onLeftButtonClick() {
        mBinding.tvSelectedDate.setText("");
        currentSelectedMonth = currentSelectedMonth - 1;
        if (currentSelectedMonth < 1) {
            currentSelectedMonth = 12;
            currentSelectedYear = currentSelectedYear - 1;
        }
        Log.d("currentSelectedMonth", currentSelectedMonth + "");
        Log.d("currentSelectedYear", currentSelectedYear + "");
//        Toast.makeText(getActivity(), "onLeftButtonClick!", Toast.LENGTH_SHORT).show();
        getMonthCaseCount(currentSelectedYear, currentSelectedMonth);
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
