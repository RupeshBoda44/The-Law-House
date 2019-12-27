package com.thelawhouse.Fragment;

import android.Manifest;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.thelawhouse.Activity.MainActivity;
import com.thelawhouse.Adapter.PhoneBookListAdapter;
import com.thelawhouse.ClickListener.PaginationScrollListener;
import com.thelawhouse.ClickListener.RecyclerViewClickListener;
import com.thelawhouse.Model.PhonebookModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.FragPhonebookListBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;

public class FragPhoneBookList extends Fragment {
    private FragPhonebookListBinding mBinding;
    private MainActivity mainActivity;

    private int page = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private PhoneBookListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frag_phonebook_list, container, false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.ClickcableTrue();
        onClickListener();
//        setData();
        return mBinding.getRoot();
    }

    private void setData() {
        page = 0;
        isLoading = false;
        isLastPage = false;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.rvPhoneBookList.setLayoutManager(linearLayoutManager);
        mAdapter = new PhoneBookListAdapter(getActivity(), new RecyclerViewClickListener() {
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
                                    Utils.showSettingsDialog(getActivity());
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        mBinding.rvPhoneBookList.setAdapter(mAdapter);
        mBinding.rvPhoneBookList.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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



    private void loadData(int page) {

        if (isInternetAvailable(getActivity())) {
            final ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), true, true, false, null);
            WebApiClient.getInstance().PhoneBookDetail(paramMyBooking(String.valueOf(page))).enqueue(new Callback<PhonebookModel>() {
                @Override
                public void onResponse(Call<PhonebookModel> call, Response<PhonebookModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    assert response.body() != null;
                    if (response.code() == 200) {
                        if (response.body().message.equalsIgnoreCase("success")) {
                            mBinding.rvPhoneBookList.setVisibility(View.VISIBLE);
                            mBinding.tvDataNotFound.setVisibility(View.GONE);
                            PhonebookModel caseListModel = response.body();
                            resultAction(caseListModel);
                        }
                    }
                }

                @Override
                public void onFailure(Call<PhonebookModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    mBinding.rvPhoneBookList.setVisibility(View.GONE);
                    mBinding.tvDataNotFound.setVisibility(View.VISIBLE);
                    Log.e("error", t.getMessage());
                }
            });
        }
    }

    private void resultAction(PhonebookModel myBookingModel) {
        isLoading = false;
        if (myBookingModel != null) {
            mAdapter.addItems(myBookingModel.phone_book_data);
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
        params.put("limit", "15");
        Log.d("param", params.toString());
        return params;
    }

    private void onClickListener() {
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
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
        mainActivity.selectFirstItemAsDefault();
    }
}
