package com.thelawhouse.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thelawhouse.Activity.GMainActivity;
import com.thelawhouse.Adapter.AllUsefullLinkAdapter;
import com.thelawhouse.ClickListener.PaginationScrollListener;
import com.thelawhouse.ClickListener.RecyclerViewClickListener;
import com.thelawhouse.ClickListener.RecyclerViewClickListener2;
import com.thelawhouse.Model.UsefullLinkListModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.FragUsefullLinkListBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;

public class GFragUsefullLinkList extends Fragment {
    private FragUsefullLinkListBinding mBinding;
    private GMainActivity mainActivity;
    private int page = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private AllUsefullLinkAdapter mAdapter;
    List<UsefullLinkListModel.Use_full_link_data> caseList_data = new ArrayList<>();
    private boolean isVisible = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            if (getFragmentManager() != null) {

                getFragmentManager()
                        .beginTransaction()
                        .detach(this)
                        .attach(this)
                        .commit();
                isVisible = true;
            }
        }
    }

    public static GFragUsefullLinkList newInstance() {
        return new GFragUsefullLinkList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frag_usefull_link_list, container, false);
        mainActivity = (GMainActivity) getActivity();
        mainActivity.ClickcableTrue();
        mBinding.tvAddLink.setVisibility(View.GONE);
//        setData();
        if (isVisible) {
            setData();
        }
        return mBinding.getRoot();
    }

    private void setData() {
        caseList_data = new ArrayList<>();
        page = 0;
        isLoading = false;
        isLastPage = false;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.rvUsefullLink.setLayoutManager(linearLayoutManager);
        mAdapter = new AllUsefullLinkAdapter(caseList_data, getActivity(),
                new RecyclerViewClickListener() {
                    @Override
                    public void ImageViewListClicked(String mobileNum) {

                    }
                }, new RecyclerViewClickListener2() {
            @Override
            public void ImageViewListClicked(String id) {

            }
        });
        mBinding.rvUsefullLink.setAdapter(mAdapter);

        mBinding.rvUsefullLink.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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
            WebApiClient.getInstance().UsefullLinkList(paramMyBooking(String.valueOf(page))).enqueue(new Callback<UsefullLinkListModel>() {
                @Override
                public void onResponse(Call<UsefullLinkListModel> call, Response<UsefullLinkListModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    assert response.body() != null;
                    if (response.code() == 200) {
                        if (response.body().message.equalsIgnoreCase("success")) {
                            mBinding.rvUsefullLink.setVisibility(View.VISIBLE);
                            mBinding.tvDataNotFound.setVisibility(View.GONE);
                            UsefullLinkListModel caseListModel = response.body();
                            resultAction(caseListModel);
                        } else {
                            mBinding.rvUsefullLink.setVisibility(View.GONE);
                            mBinding.tvDataNotFound.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<UsefullLinkListModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    mBinding.rvUsefullLink.setVisibility(View.GONE);
                    mBinding.tvDataNotFound.setVisibility(View.VISIBLE);
                    Log.e("error", t.getMessage());
                }
            });
        }
    }

    private void resultAction(UsefullLinkListModel myBookingModel) {
        isLoading = false;
        if (myBookingModel != null) {
            mAdapter.addItems(myBookingModel.use_full_link_data);
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

    @Override
    public void onResume() {
        super.onResume();
//        setData();
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
