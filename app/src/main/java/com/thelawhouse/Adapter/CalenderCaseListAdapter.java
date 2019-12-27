package com.thelawhouse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thelawhouse.ClickListener.RecyclerViewClickListener;
import com.thelawhouse.ClickListener.RecyclerViewClickListener2;
import com.thelawhouse.ClickListener.RecyclerViewClickListener3;
import com.thelawhouse.Model.CaseListModel;
import com.thelawhouse.R;
import com.thelawhouse.databinding.AdapterCalenderCaseListBinding;

import java.util.ArrayList;
import java.util.List;

public class CalenderCaseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CaseListModel.Collection_data> caseList_data = new ArrayList<>();
    private Context mContext;
    private RecyclerViewClickListener mClickListener;
    private RecyclerViewClickListener3 mClickListener3;
    private RecyclerViewClickListener2 mClickListener2;

    public CalenderCaseListAdapter(Context context, RecyclerViewClickListener mClickListeneR, RecyclerViewClickListener2 mClickListeneR2, RecyclerViewClickListener3 mClickListeneR3) {
        this.mContext = context;
        caseList_data = new ArrayList<>();
        mClickListener = mClickListeneR;
        mClickListener2 = mClickListeneR2;
        mClickListener3 = mClickListeneR3;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterCalenderCaseListBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.adapter_calender_case_list, parent, false);
        return new CalenderCaseListAdapter.ItemViewHolder(mBinding.getRoot(), mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CalenderCaseListAdapter.ItemViewHolder mViewHolder = (CalenderCaseListAdapter.ItemViewHolder) holder;
        final CaseListModel.Collection_data mdata = caseList_data.get(position);
        mViewHolder.mBinding.tvNextDate.setText(mdata.case_last_date + " / " + mdata.case_next_date);
        mViewHolder.mBinding.tvLastDate.setText(mdata.case_last_date);
        mViewHolder.mBinding.tvCaseType.setText(mdata.case_type + " / " + mdata.court + " - " + mdata.court_room_no);
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
        mViewHolder.mBinding.tvCaseId.setText("Case Id : " + mdata.case_id);

        mViewHolder.mBinding.tvViewMore.setVisibility(View.VISIBLE);
        mViewHolder.mBinding.llView.setVisibility(View.GONE);
        mViewHolder.mBinding.tvShowLess.setVisibility(View.GONE);
        mViewHolder.mBinding.tvViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewHolder.mBinding.tvViewMore.setVisibility(View.GONE);
                mViewHolder.mBinding.llView.setVisibility(View.VISIBLE);
                mViewHolder.mBinding.tvShowLess.setVisibility(View.VISIBLE);
            }
        });
        mViewHolder.mBinding.tvShowLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewHolder.mBinding.tvViewMore.setVisibility(View.VISIBLE);
                mViewHolder.mBinding.llView.setVisibility(View.GONE);
                mViewHolder.mBinding.tvShowLess.setVisibility(View.GONE);
            }
        });
        mViewHolder.mBinding.ivMenubar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, v);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
                popup.getMenu().add(0, 1, 1, menuIconWithText(mContext.getResources().getString(R.string.whatsapp)));
                popup.getMenu().add(0, 2, 2, menuIconWithText(mContext.getResources().getString(R.string.call)));
                popup.getMenu().add(0, 3, 3, menuIconWithText(mContext.getResources().getString(R.string.edit)));
                popup.getMenu().add(0, 4, 4, menuIconWithText(mContext.getResources().getString(R.string.comlete)));
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == 1) {
                            Intent viewIntent = new Intent("android.intent.action.VIEW",
                                    Uri.parse("https://api.whatsapp.com/send?phone=" + mViewHolder.mBinding.tvWhatsappNumber.getText().toString()));
                            mContext.startActivity(viewIntent);
                        } else if (item.getItemId() == 2) {
                            mClickListener.ImageViewListClicked(mViewHolder.mBinding.tvMobileNo.getText().toString());
                        } else if (item.getItemId() == 3) {
                            mClickListener2.ImageViewListClicked(mdata.case_id);
                        } else {
                            caseList_data.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeRemoved(position, caseList_data.size());
                            mClickListener3.ImageViewListClicked(mdata.case_id);
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    private CharSequence menuIconWithText(String title) {
        SpannableString sb = new SpannableString(title);
        return sb;
    }

    @Override
    public int getItemCount() {
        return caseList_data.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private AdapterCalenderCaseListBinding mBinding;

        ItemViewHolder(View itemView, AdapterCalenderCaseListBinding mBinding) {
            super(itemView);
            this.mBinding = mBinding;
        }
    }

    public void addItems(List<CaseListModel.Collection_data> items) {
        caseList_data.addAll(items);
        notifyDataSetChanged();
    }


}
