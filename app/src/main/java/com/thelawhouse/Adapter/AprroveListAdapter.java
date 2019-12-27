package com.thelawhouse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thelawhouse.ClickListener.RecyclerViewClickListener;
import com.thelawhouse.ClickListener.RecyclerViewClickListener4;
import com.thelawhouse.Model.AdminListModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.databinding.AdapterApproveListBinding;

import java.util.ArrayList;
import java.util.List;

public class AprroveListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AdminListModel.Users_data> caseList_data = new ArrayList<>();
    private Context mContext;
    RecyclerViewClickListener clickListener;
    RecyclerViewClickListener4 clickListener4;

    public AprroveListAdapter(Context context, RecyclerViewClickListener clickListener, RecyclerViewClickListener4 clickListener4) {
        this.mContext = context;
        caseList_data = new ArrayList<>();
        this.clickListener = clickListener;
        this.clickListener4 = clickListener4;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterApproveListBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.adapter_approve_list, parent, false);
        return new AprroveListAdapter.ItemViewHolder(mBinding.getRoot(), mBinding);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final AprroveListAdapter.ItemViewHolder mViewHolder = (AprroveListAdapter.ItemViewHolder) holder;
        final AdminListModel.Users_data mdata = caseList_data.get(position);
        mViewHolder.mBinding.tvName.setText(mdata.name);
        if (mdata.email != null)
            mViewHolder.mBinding.tvEmail.setText(mdata.email);
        mViewHolder.mBinding.tvMobileNo.setText(mdata.mobile_number);
        if (mdata.status.equalsIgnoreCase("active")) {
            mViewHolder.mBinding.tvStatus.setText("Deactivate");
            mViewHolder.mBinding.tvStatus.setTextColor(mContext.getResources().getColor(R.color.red));
        } else {
            mViewHolder.mBinding.tvStatus.setText("Activate");
            mViewHolder.mBinding.tvStatus.setTextColor(mContext.getResources().getColor(R.color.green));
        }
        mViewHolder.mBinding.tvMobileNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.ImageViewListClicked(mdata.mobile_number);
            }
        });
        if (PreferenceHelper.getString(Constants.MOBILE_NUMBER, "").equalsIgnoreCase(mdata.mobile_number)) {
//            caseList_data.remove(position);
//            notifyItemRemoved(position);
//            notifyItemRangeRemoved(position, caseList_data.size());
            mViewHolder.mBinding.llMain.setVisibility(View.GONE);
        }
        mViewHolder.mBinding.tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{mdata.email});
                i.putExtra(Intent.EXTRA_SUBJECT, "");
                i.putExtra(Intent.EXTRA_TEXT, "");
                try {
                    mContext.startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mViewHolder.mBinding.tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewHolder.mBinding.tvStatus.getText().toString().equalsIgnoreCase("Activate")) {
                    mViewHolder.mBinding.tvStatus.setText("Deactivate");
                    mViewHolder.mBinding.tvStatus.setTextColor(mContext.getResources().getColor(R.color.red));
                    clickListener4.ImageViewListClicked(mdata.user_id, "active");
                } else {
                    mViewHolder.mBinding.tvStatus.setText("Activate");
                    mViewHolder.mBinding.tvStatus.setTextColor(mContext.getResources().getColor(R.color.green));
                    clickListener4.ImageViewListClicked(mdata.user_id, "deactive");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return caseList_data.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private AdapterApproveListBinding mBinding;

        ItemViewHolder(View itemView, AdapterApproveListBinding mBinding) {
            super(itemView);
            this.mBinding = mBinding;
        }
    }

    public void addItems(List<AdminListModel.Users_data> items) {
        caseList_data.addAll(items);
        notifyDataSetChanged();
    }


}
