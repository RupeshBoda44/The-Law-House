package com.thelawhouse.Fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.thelawhouse.Activity.MainActivity;
import com.thelawhouse.Adapter.AllCaseAdapter;
import com.thelawhouse.ClickListener.PaginationScrollListener;
import com.thelawhouse.ClickListener.RecyclerViewClickListener;
import com.thelawhouse.ClickListener.RecyclerViewClickListener2;
import com.thelawhouse.Model.CaseListModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.FragFindCaseByMobileBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;

public class FragFindCaseByMobile extends Fragment implements View.OnClickListener {
    private FragFindCaseByMobileBinding mBinding;
    private int page = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private AllCaseAdapter mAdapter;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frag_find_case_by_mobile, container, false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.ClickcableTrue();
        mBinding.tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.edtMobileNo.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(getActivity(), "Please Enter Mobile Number");
                } else if (mBinding.edtMobileNo.getText().toString().length() < 10) {
                    Utils.showDialog(getActivity(), "Please Enter Valid Mobile Number");
                } else {
                    page = 0;
                    isLoading = false;
                    isLastPage = false;
                    setData();
                }
            }
        });
//        setData();
        return mBinding.getRoot();
    }

    private void setData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.rvAllCase.setLayoutManager(linearLayoutManager);
        mAdapter = new AllCaseAdapter(getActivity(),
                new RecyclerViewClickListener() {
                    @Override
                    public void ImageViewListClicked(String mobileNum) {
                        Dexter.withActivity(getActivity())
                                .withPermissions(Manifest.permission.CALL_PHONE)
                                .withListener(new MultiplePermissionsListener() {
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

    private void loadData(int page) {
        if (isInternetAvailable(getActivity())) {
            final ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), true, true, false, null);
            WebApiClient.getInstance().FindCaseByMobile(paramMyBooking(String.valueOf(page))).enqueue(new Callback<CaseListModel>() {
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
        params.put("mobile_no", mBinding.edtMobileNo.getText().toString());
        params.put("limit", "5");
        Log.d("param", params.toString());
        return params;
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
                    onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

    public void onBackPressed() {
        mainActivity.selectFirstItemAsDefault();
//        Utils.onBackPressed(getActivity(), getResources().getString(R.string.exit_dialog_text));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }
}
