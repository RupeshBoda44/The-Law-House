package com.thelawhouse.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thelawhouse.Activity.AddSegmentLawAcitivity;
import com.thelawhouse.ClickListener.RecyclerViewClickListener;
import com.thelawhouse.ClickListener.RecyclerViewClickListener2;
import com.thelawhouse.Model.SegmentLawListModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.databinding.AdapterSegmentLawBinding;

import java.util.ArrayList;
import java.util.List;

public class AllSegmentLawAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SegmentLawListModel.Segment_of_law_data> caseList_data = new ArrayList<>();
    private Context mContext;
    private RecyclerViewClickListener mClickListener;
    private RecyclerViewClickListener2 mClickListener2;

    public AllSegmentLawAdapter(List<SegmentLawListModel.Segment_of_law_data> caseList_data,Context context, RecyclerViewClickListener mClickListeneR,
                                RecyclerViewClickListener2 mClickListener2) {
        this.mContext = context;
        this.caseList_data = new ArrayList<>();
        mClickListener = mClickListeneR;
        this.mClickListener2 = mClickListener2;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterSegmentLawBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.adapter_segment_law, parent, false);
        return new AllSegmentLawAdapter.ItemViewHolder(mBinding.getRoot(), mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final AllSegmentLawAdapter.ItemViewHolder mViewHolder = (AllSegmentLawAdapter.ItemViewHolder) holder;
        final SegmentLawListModel.Segment_of_law_data mdata = caseList_data.get(position);
        mViewHolder.mBinding.tvContent.setText(mdata.content);
        mViewHolder.mBinding.tvTitle.setText(mdata.title);
        if (PreferenceHelper.getString(Constants.LOGINTYPE, "").equalsIgnoreCase("guest")) {
            mViewHolder.mBinding.ivMenubar.setVisibility(View.GONE);
        }
        mViewHolder.mBinding.ivMenubar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, v);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
                popup.getMenu().add(0, 1, 1, menuIconWithText(mContext.getResources().getString(R.string.edit)));
                popup.getMenu().add(0, 2, 2, menuIconWithText(mContext.getResources().getString(R.string.delete)));
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == 1) {
                            mClickListener2.ImageViewListClicked(mdata.segment_id);
                        } else {
                            caseList_data.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeRemoved(position, caseList_data.size());
                            mClickListener.ImageViewListClicked(mdata.segment_id);
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
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return caseList_data.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private AdapterSegmentLawBinding mBinding;

        ItemViewHolder(View itemView, AdapterSegmentLawBinding mBinding) {
            super(itemView);
            this.mBinding = mBinding;
        }
    }

    public void addItems(List<SegmentLawListModel.Segment_of_law_data> items) {
        caseList_data.addAll(items);
        notifyDataSetChanged();
    }


}
