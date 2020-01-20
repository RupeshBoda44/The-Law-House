package com.thelawhouse.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.thelawhouse.Adapter.NotificationListAdapter;
import com.thelawhouse.ClickListener.PaginationScrollListener;
import com.thelawhouse.ClickListener.RecyclerViewClickListener2;
import com.thelawhouse.Model.NotificationModel;
import com.thelawhouse.Model.VerifyModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.ActivityNotificationBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;

public class NotificationActivity extends AppCompatActivity {
    ActivityNotificationBinding mBinding;
    NotificationActivity mContext = NotificationActivity.this;
    NotificationListAdapter mAdapter;
    int page = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.activity_notification);
        onClickListener();
        onCreateMethod();
    }

    private void onCreateMethod() {
        page = 0;
        isLoading = false;
        isLastPage = false;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mBinding.rvNotification.setLayoutManager(linearLayoutManager);
        mAdapter = new NotificationListAdapter(mContext, new RecyclerViewClickListener2() {
            @Override
            public void ImageViewListClicked(String id) {
                notificationStatus(id);
            }
        });
        mBinding.rvNotification.setAdapter(mAdapter);
        mBinding.rvNotification.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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

        if (isInternetAvailable(mContext)) {
            ProgressHUD mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().NotificationList(paramNotifications(String.valueOf(page))).enqueue(new Callback<NotificationModel>() {
                @Override
                public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    assert response.body() != null;
                    if (response.code() == 200) {
                        if (response.body().message.equalsIgnoreCase("success")) {
                            mBinding.rvNotification.setVisibility(View.VISIBLE);
                            mBinding.tvDataNotFound.setVisibility(View.GONE);
                            NotificationModel myBookingModel = response.body();
                            resultAction(myBookingModel);
                        }
                    }
                }

                @Override
                public void onFailure(Call<NotificationModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    mBinding.rvNotification.setVisibility(View.GONE);
                    mBinding.tvDataNotFound.setVisibility(View.VISIBLE);
                    Log.e("error", t.getMessage());
                }
            });
        }
    }

    private void resultAction(NotificationModel myBookingModel) {
        isLoading = false;
        if (myBookingModel != null) {
            mAdapter.addItems(myBookingModel.notification_data);
            if (page == (myBookingModel.total_pages - 1)) {
                isLastPage = true;
            } else {
                page = page + 1;
            }
        }
    }

    private Map<String, String> paramNotifications(String page) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", PreferenceHelper.getString(Constants.USER_ID, ""));
        params.put("mobile_number", PreferenceHelper.getString(Constants.MOBILE_NUMBER, ""));
        params.put("unique_code", PreferenceHelper.getString(Constants.UNIQUE_CODE, ""));
        params.put("page_id", page);
        params.put("limit", "10");
        Log.d("param", params.toString());
        return params;
    }

    private void onClickListener() {
        mBinding.toolbar.tvTitle.setText("Notifications");
        mBinding.toolbar.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeRefresh.setRefreshing(false);

                onCreateMethod();
            }
        });
    }

    private void notificationStatus(String id) {
        if (isInternetAvailable(mContext)) {
            ProgressHUD mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().NotificationStatus(paramNotificationStatus(id)).enqueue(new Callback<VerifyModel>() {
                @Override
                public void onResponse(Call<VerifyModel> call, Response<VerifyModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                    }
                }

                @Override
                public void onFailure(Call<VerifyModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(mContext, "Check Your Internet.");
        }
    }

    private Map<String, String> paramNotificationStatus(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", PreferenceHelper.getString(Constants.USER_ID, ""));
        params.put("mobile_number", PreferenceHelper.getString(Constants.MOBILE_NUMBER, ""));
        params.put("unique_code", PreferenceHelper.getString(Constants.UNIQUE_CODE, ""));
        params.put("notification_id", id);
        Log.d("param", params.toString());
        return params;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
