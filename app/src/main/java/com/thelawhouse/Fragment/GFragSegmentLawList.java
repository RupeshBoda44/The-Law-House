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
import com.thelawhouse.Adapter.AllSegmentLawAdapter;
import com.thelawhouse.ClickListener.PaginationScrollListener;
import com.thelawhouse.ClickListener.RecyclerViewClickListener;
import com.thelawhouse.Model.SegmentLawListModel;
import com.thelawhouse.Model.VerifyModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.FragSagmentLawListBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;

public class GFragSegmentLawList extends Fragment {
    private FragSagmentLawListBinding mBinding;
    private GMainActivity mainActivity;
    private int page = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private AllSegmentLawAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frag_sagment_law_list, container, false);
        mainActivity = (GMainActivity) getActivity();
        mainActivity.ClickcableFalse();
        mBinding.tvAddSegment.setVisibility(View.GONE);
        return mBinding.getRoot();
    }

    private void setData() {
        page = 0;
        isLoading = false;
        isLastPage = false;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.rvSegementLaw.setLayoutManager(linearLayoutManager);
        mAdapter = new AllSegmentLawAdapter(getActivity(),
                new RecyclerViewClickListener() {
                    @Override
                    public void ImageViewListClicked(String mobileNum) {
                        deleteSagementLaw(mobileNum);
                    }
                });
        mBinding.rvSegementLaw.setAdapter(mAdapter);

        mBinding.rvSegementLaw.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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

    private void deleteSagementLaw(String linkId) {
        if (isInternetAvailable(getActivity())) {
            ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), true, true, false, null);
            WebApiClient.getInstance().DeleteSegmentLaw(paramGetData(linkId)).enqueue(new Callback<VerifyModel>() {
                @Override
                public void onResponse(Call<VerifyModel> call, Response<VerifyModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        if (response.body().message.equalsIgnoreCase("success")) {
                        } else {
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
        params.put("segment_id", linkId);
        return params;
    }

    private void loadData(int page) {

        if (isInternetAvailable(getActivity())) {
            final ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), true, true, false, null);
            WebApiClient.getInstance().SegmentLawList(paramMyBooking(String.valueOf(page))).enqueue(new Callback<SegmentLawListModel>() {
                @Override
                public void onResponse(Call<SegmentLawListModel> call, Response<SegmentLawListModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    assert response.body() != null;
                    if (response.code() == 200) {
                        if (response.body().message.equalsIgnoreCase("success")) {
                            mBinding.rvSegementLaw.setVisibility(View.VISIBLE);
                            mBinding.tvDataNotFound.setVisibility(View.GONE);
                            SegmentLawListModel caseListModel = response.body();
                            resultAction(caseListModel);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SegmentLawListModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    mBinding.rvSegementLaw.setVisibility(View.GONE);
                    mBinding.tvDataNotFound.setVisibility(View.VISIBLE);
                    Log.e("error", t.getMessage());
                }
            });
        }
    }

    private void resultAction(SegmentLawListModel myBookingModel) {
        isLoading = false;
        if (myBookingModel != null) {
            mAdapter.addItems(myBookingModel.segment_of_law_data);
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
        Utils.onBackPressed(getActivity(), getResources().getString(R.string.exit_dialog_text));
    }
}
