package com.thelawhouse.Adapter;

import android.content.Context;
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
import com.thelawhouse.Model.TodayAppointmentListModel;
import com.thelawhouse.R;
import com.thelawhouse.databinding.AdapterTodayAppointmentListBinding;

import java.util.ArrayList;
import java.util.List;

public class TodayAppointmentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TodayAppointmentListModel.Appointment_data> caseList_data = new ArrayList<>();
    private Context mContext;
    private RecyclerViewClickListener2 mClickListener2;
    private RecyclerViewClickListener mClickListener;

    public TodayAppointmentListAdapter(Context context, RecyclerViewClickListener mClickListeneR, RecyclerViewClickListener2 mClickListeneR2) {
        this.mContext = context;
        caseList_data = new ArrayList<>();
        mClickListener = mClickListeneR;
        mClickListener2 = mClickListeneR2;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterTodayAppointmentListBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.adapter_today_appointment_list, parent, false);
        return new TodayAppointmentListAdapter.ItemViewHolder(mBinding.getRoot(), mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final TodayAppointmentListAdapter.ItemViewHolder mViewHolder = (TodayAppointmentListAdapter.ItemViewHolder) holder;
        final TodayAppointmentListModel.Appointment_data mdata = caseList_data.get(position);
        mViewHolder.mBinding.tvName.setText(mdata.name);
        mViewHolder.mBinding.tvEmail.setText(mdata.email);
        mViewHolder.mBinding.tvMobileNo.setText(mdata.c_code + mdata.mobile);
        mViewHolder.mBinding.tvAppointmentReason.setText(mdata.appointment_reason);
        mViewHolder.mBinding.tvDate.setText(mdata.appointment_date);
        mViewHolder.mBinding.tvTime.setText(mdata.appointment_time);

        mViewHolder.mBinding.tvAppointmentId.setText("Appointment Id : " + mdata.appointment_id);
        if (mdata.appointment_status.equalsIgnoreCase("active")) {
            mViewHolder.mBinding.tvStatus.setTextColor(mContext.getResources().getColor(R.color.red));
            mViewHolder.mBinding.tvStatus.setText("Remaining");
        } else {
            mViewHolder.mBinding.tvStatus.setTextColor(mContext.getResources().getColor(R.color.green));
            mViewHolder.mBinding.tvStatus.setText("Completed");
        }
        mViewHolder.mBinding.ivMenubar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, v);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
                popup.getMenu().add(0, 1, 1, menuIconWithText(mContext.getResources().getString(R.string.edit)));
                popup.getMenu().add(0, 2, 2, menuIconWithText("Complete"));
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == 1) {
//                            Intent viewIntent = new Intent("android.intent.action.VIEW",
//                                    Uri.parse("https://api.whatsapp.com/send?phone=" + mViewHolder.mBinding.tvWhatsappNumber.getText().toString()));
//                            mContext.startActivity(viewIntent);
                            mClickListener.ImageViewListClicked(mdata.appointment_id);
                        } else {
                            mViewHolder.mBinding.llMain.setVisibility(View.GONE);
                            mClickListener2.ImageViewListClicked(mdata.appointment_id);
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
        private AdapterTodayAppointmentListBinding mBinding;

        ItemViewHolder(View itemView, AdapterTodayAppointmentListBinding mBinding) {
            super(itemView);
            this.mBinding = mBinding;
        }
    }

    public void addItems(List<TodayAppointmentListModel.Appointment_data> items) {
        caseList_data.addAll(items);
        notifyDataSetChanged();
    }


}
