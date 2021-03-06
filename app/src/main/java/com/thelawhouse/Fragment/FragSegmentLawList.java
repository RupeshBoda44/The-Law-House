package com.thelawhouse.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thelawhouse.Activity.AddSegmentLawAcitivity;
import com.thelawhouse.Activity.MainActivity;
import com.thelawhouse.Adapter.AllSegmentLawAdapter;
import com.thelawhouse.ClickListener.PaginationScrollListener;
import com.thelawhouse.ClickListener.RecyclerViewClickListener;
import com.thelawhouse.ClickListener.RecyclerViewClickListener2;
import com.thelawhouse.Model.SegmentLawListModel;
import com.thelawhouse.Model.VerifyModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.FragSagmentLawListBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;

public class FragSegmentLawList extends Fragment {
    private FragSagmentLawListBinding mBinding;
    private MainActivity mainActivity;
    private int page = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private AllSegmentLawAdapter mAdapter;
    private boolean isVisible = false;
    List<SegmentLawListModel.Segment_of_law_data> caseList_data = new ArrayList<>();
    private boolean one = false;

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
                one = false;
            }
        }
    }

    public static FragSegmentLawList newInstance() {
        return new FragSegmentLawList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frag_sagment_law_list, container, false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.ClickcableTrue();
        onClickListener();
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
        mBinding.rvSegementLaw.setLayoutManager(linearLayoutManager);
        mAdapter = new AllSegmentLawAdapter(caseList_data, getActivity(),
                new RecyclerViewClickListener() {
                    @Override
                    public void ImageViewListClicked(String mobileNum) {
                        deleteSagementLaw(mobileNum);
                    }
                }, new RecyclerViewClickListener2() {
            @Override
            public void ImageViewListClicked(String id) {
                Intent intent = new Intent(getActivity(), AddSegmentLawAcitivity.class);
                intent.putExtra("linkId", id);
                startActivityForResult(intent, 103);
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
        if (one == false) {
            one = true;
            loadData(page);
        }
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
                        } else {
                            mBinding.rvSegementLaw.setVisibility(View.GONE);
                            mBinding.tvDataNotFound.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SegmentLawListModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    mBinding.rvSegementLaw.setVisibility(View.GONE);
                    mBinding.tvDataNotFound.setVisibility(View.VISIBLE);
                    Log.e("error", Objects.requireNonNull(t.getMessage()));
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

    private void onClickListener() {
        mBinding.tvAddSegment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddSegmentLawAcitivity.class);
                startActivityForResult(intent, 102);
            }
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 102) {
            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .detach(this)
                        .attach(this)
                        .commit();
                isVisible = true;
                one = false;
            }
        } else if (resultCode == 103) {
            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .detach(this)
                        .attach(this)
                        .commit();
                isVisible = true;
                one = false;
            }
        }
    }

    public void onBackPressed() {
        mainActivity.selectFirstItemAsDefault();
    }
}
