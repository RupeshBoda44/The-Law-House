package com.thelawhouse.Adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.thelawhouse.ClickListener.RecyclerViewClickListener;
import com.thelawhouse.ClickListener.RecyclerViewClickListener2;
import com.thelawhouse.Model.UsefullLinkListModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.databinding.AdapterAllUsefullLinkBinding;

import java.util.ArrayList;
import java.util.List;

public class AllUsefullLinkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UsefullLinkListModel.Use_full_link_data> caseList_data = new ArrayList<>();
    private Context mContext;
    private RecyclerViewClickListener mClickListener;
    private RecyclerViewClickListener2 mClickListener2;

    public AllUsefullLinkAdapter(List<UsefullLinkListModel.Use_full_link_data> caseList_data, Context context, RecyclerViewClickListener mClickListeneR,
                                 RecyclerViewClickListener2 mClickListener2) {
        this.mContext = context;
        this.caseList_data = caseList_data;
        mClickListener = mClickListeneR;
        this.mClickListener2 = mClickListener2;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterAllUsefullLinkBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.adapter_all_usefull_link, parent, false);
        return new AllUsefullLinkAdapter.ItemViewHolder(mBinding.getRoot(), mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final AllUsefullLinkAdapter.ItemViewHolder mViewHolder = (AllUsefullLinkAdapter.ItemViewHolder) holder;
        final UsefullLinkListModel.Use_full_link_data mdata = caseList_data.get(position);
        mViewHolder.mBinding.tvUsefulLink.setText(mdata.content);
        mViewHolder.mBinding.tvStatus.setText("Status : " + mdata.status);
        if (PreferenceHelper.getString(Constants.LOGINTYPE, "").equalsIgnoreCase("guest")) {
            mViewHolder.mBinding.ivMenubar.setVisibility(View.GONE);
        }
        mViewHolder.mBinding.tvUsefulLink.setMovementMethod(LinkMovementMethod.getInstance());
        mViewHolder.mBinding.ivMenubar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, v);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
                popup.getMenu().add(0, 1, 1, menuIconWithText(mContext.getResources().getString(R.string.edit)));
                popup.getMenu().add(0, 2, 2, menuIconWithText("Delete"));
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == 1) {
//                            Intent viewIntent = new Intent("android.intent.action.VIEW",
//                                    Uri.parse("https://api.whatsapp.com/send?phone=" + mViewHolder.mBinding.tvWhatsappNumber.getText().toString()));
//                            mContext.startActivity(viewIntent);
                            mClickListener2.ImageViewListClicked(mdata.use_full_link_id);

                        } else {
                            caseList_data.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeRemoved(position, caseList_data.size());
                            mClickListener.ImageViewListClicked(mdata.use_full_link_id);
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
        private AdapterAllUsefullLinkBinding mBinding;

        ItemViewHolder(View itemView, AdapterAllUsefullLinkBinding mBinding) {
            super(itemView);
            this.mBinding = mBinding;
        }
    }

    public void addItems(List<UsefullLinkListModel.Use_full_link_data> items) {
        caseList_data.addAll(items);
        notifyDataSetChanged();
    }


}
