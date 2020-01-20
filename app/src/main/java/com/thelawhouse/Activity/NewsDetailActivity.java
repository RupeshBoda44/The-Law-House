package com.thelawhouse.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.thelawhouse.Model.NewsListModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.ActivityNewsDetailBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;

public class NewsDetailActivity extends AppCompatActivity {
    NewsDetailActivity mContext = NewsDetailActivity.this;
    ActivityNewsDetailBinding mBinding;
    String linkId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.activity_news_detail);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            linkId = bundle.getString("linkId");
            getData(linkId);
        }
        onClickListener();
    }

    private void onClickListener() {
        mBinding.toolbar.ivNavigation.setColorFilter(getResources().getColor(R.color.colorPrimary));
        mBinding.toolbar.ivNavigation.setEnabled(false);
        mBinding.toolbar.tvTitle.setText("Show News");
        mBinding.toolbar.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getData(String linkId) {
        if (isInternetAvailable(mContext)) {
            ProgressHUD mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().NewsDetail(paramUpdate(linkId)).enqueue(new Callback<NewsListModel>() {
                @Override
                public void onResponse(Call<NewsListModel> call, Response<NewsListModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        if (response.body().message.equalsIgnoreCase("success")) {
                            mBinding.edtNewsDetail.setText(response.body().news_data.get(0).contents);
                            mBinding.edtTitle.setText(response.body().news_data.get(0).title);
                            Glide.with(mContext)
                                    .load(PreferenceHelper.getString(Constants.ImagePath, "") + response.body().news_data.get(0).news_image)
                                    .placeholder(R.drawable.img_news_demo)
                                    .into(mBinding.ivNewsImg);
                        } else {
                            Utils.showDialog(mContext, response.body().message + "");
                        }
                    }
                }

                @Override
                public void onFailure(Call<NewsListModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(mContext, "Check Your Internet.");
        }
    }

    private Map<String, String> paramUpdate(String linkId) {
        Map<String, String> params = new HashMap<>();
        params.put("news_id", linkId);
        return params;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
