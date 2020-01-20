package com.thelawhouse.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.thelawhouse.ClickListener.RecyclerViewClickListener2;
import com.thelawhouse.Model.NotificationModel;
import com.thelawhouse.R;
import com.thelawhouse.databinding.AdapterNotificationListBinding;

import java.util.ArrayList;
import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NotificationModel.Notification_data> mData = new ArrayList<>();
    private Context mContext;
    private RecyclerViewClickListener2 mClickListener;

    public NotificationListAdapter(Context context, RecyclerViewClickListener2 mClickListener) {
        this.mContext = context;
        this.mClickListener = mClickListener;
        mData = new ArrayList<>();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterNotificationListBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.adapter_notification_list, parent, false);
        return new NotificationListAdapter.ItemViewHolder(mBinding.getRoot(), mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        NotificationListAdapter.ItemViewHolder mViewHolder = (NotificationListAdapter.ItemViewHolder) holder;
        final NotificationModel.Notification_data mVData = mData.get(position);
        mViewHolder.mBinding.tvNotification.setText(Html.fromHtml(mVData.getNotification_content()));
        mViewHolder.mBinding.tvDate.setText(mVData.getCreated_date());
        if (mVData.getNotification_read().equalsIgnoreCase("0")) {
            mViewHolder.mBinding.llNotification.setBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
        } else {
            mViewHolder.mBinding.llNotification.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            mData.get(position).setFlag(true);
        }
        if (mData.get(position).getFlag()) {
            mViewHolder.mBinding.llNotification.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            mViewHolder.mBinding.llNotification.setBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
        }
        mViewHolder.mBinding.llNotification.setTag(position);
        mViewHolder.mBinding.llNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickPosition = (int) v.getTag();
                if (mData.get(clickPosition).getFlag()) {
                } else {
                    mData.get(position).setFlag(true);
                    mClickListener.ImageViewListClicked(mVData.notification_id);
                }
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private AdapterNotificationListBinding mBinding;

        ItemViewHolder(View itemView, AdapterNotificationListBinding mBinding) {
            super(itemView);
            this.mBinding = mBinding;
        }
    }

    public void addItems(List<NotificationModel.Notification_data> items) {
        mData.addAll(items);
        notifyDataSetChanged();
    }

}
