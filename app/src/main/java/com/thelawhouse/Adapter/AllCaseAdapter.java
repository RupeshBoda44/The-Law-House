package com.thelawhouse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thelawhouse.ClickListener.RecyclerViewClickListener2;
import com.thelawhouse.Model.CaseListModel;
import com.thelawhouse.R;
import com.thelawhouse.ClickListener.RecyclerViewClickListener;
import com.thelawhouse.databinding.AdapterAllCaseBinding;

import java.util.ArrayList;
import java.util.List;

public class AllCaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CaseListModel.Collection_data> caseList_data = new ArrayList<>();
    private Context mContext;
    private RecyclerViewClickListener mClickListener;
    private RecyclerViewClickListener2 mClickListener2;

    public AllCaseAdapter(Context context, RecyclerViewClickListener mClickListeneR,RecyclerViewClickListener2 mClickListeneR2) {
        this.mContext = context;
        caseList_data = new ArrayList<>();
        mClickListener = mClickListeneR;
        mClickListener2 = mClickListeneR2;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterAllCaseBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.adapter_all_case, parent, false);
        return new AllCaseAdapter.ItemViewHolder(mBinding.getRoot(), mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final AllCaseAdapter.ItemViewHolder mViewHolder = (AllCaseAdapter.ItemViewHolder) holder;
        final CaseListModel.Collection_data mdata = caseList_data.get(position);
        mViewHolder.mBinding.tvNextDate.setText(mdata.case_next_date);
        mViewHolder.mBinding.tvLastDate.setText(mdata.case_last_date);
        mViewHolder.mBinding.tvCaseType.setText(mdata.case_type);
        mViewHolder.mBinding.tvCourtNo.setText(mdata.court_room_no);
        mViewHolder.mBinding.tvPartyName1.setText(mdata.party_name_1);
        mViewHolder.mBinding.tvPartyName2.setText(mdata.party_name_2);
        mViewHolder.mBinding.tvCourt.setText(mdata.court);
        mViewHolder.mBinding.tvStages.setText(mdata.stages);
        mViewHolder.mBinding.tvAdvocateParty1.setText(mdata.advocate_party_1);
        mViewHolder.mBinding.tvAdvocateParty2.setText(mdata.advocate_party_2);
        mViewHolder.mBinding.tvMobileNo.setText(mdata.m_country_code + mdata.mobile_no);
        mViewHolder.mBinding.tvWhatsappNumber.setText(mdata.w_country_code + mdata.whatsapp_number);
        mViewHolder.mBinding.tvEmail.setText(mdata.email);
        mViewHolder.mBinding.llWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent = new Intent("android.intent.action.VIEW",
                        Uri.parse("https://api.whatsapp.com/send?phone=" + mViewHolder.mBinding.tvWhatsappNumber.getText().toString()));
                mContext.startActivity(viewIntent);
            }
        });
        mViewHolder.mBinding.llCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.ImageViewListClicked(mViewHolder.mBinding.tvMobileNo.getText().toString());
            }
        });
        mViewHolder.mBinding.llEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener2.ImageViewListClicked(mdata.case_id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return caseList_data.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private AdapterAllCaseBinding mBinding;

        ItemViewHolder(View itemView, AdapterAllCaseBinding mBinding) {
            super(itemView);
            this.mBinding = mBinding;
        }
    }

    public void addItems(List<CaseListModel.Collection_data> items) {
        caseList_data.addAll(items);
        notifyDataSetChanged();
    }


}
