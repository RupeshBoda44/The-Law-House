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
import com.thelawhouse.Model.PhonebookModel;
import com.thelawhouse.R;
import com.thelawhouse.databinding.AdapterPhonebookListBinding;

import java.util.ArrayList;
import java.util.List;

public class PhoneBookListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PhonebookModel.Phone_book_data> caseList_data = new ArrayList<>();
    private Context mContext;
    RecyclerViewClickListener clickListener;

    public PhoneBookListAdapter(Context context, RecyclerViewClickListener clickListener) {
        this.mContext = context;
        caseList_data = new ArrayList<>();
        this.clickListener = clickListener;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterPhonebookListBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.adapter_phonebook_list, parent, false);
        return new PhoneBookListAdapter.ItemViewHolder(mBinding.getRoot(), mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final PhoneBookListAdapter.ItemViewHolder mViewHolder = (PhoneBookListAdapter.ItemViewHolder) holder;
        final PhonebookModel.Phone_book_data mdata = caseList_data.get(position);
        mViewHolder.mBinding.tvName.setText(mdata.name);
        mViewHolder.mBinding.tvEmail.setText(mdata.email);
        mViewHolder.mBinding.tvMobileNo.setText(mdata.mobile_number);
        mViewHolder.mBinding.tvMobileNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.ImageViewListClicked(mdata.mobile_number);
            }
        });
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

    }

    @Override
    public int getItemCount() {
        return caseList_data.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private AdapterPhonebookListBinding mBinding;

        ItemViewHolder(View itemView, AdapterPhonebookListBinding mBinding) {
            super(itemView);
            this.mBinding = mBinding;
        }
    }

    public void addItems(List<PhonebookModel.Phone_book_data> items) {
        caseList_data.addAll(items);
        notifyDataSetChanged();
    }


}
