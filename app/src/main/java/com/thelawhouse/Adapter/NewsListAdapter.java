package com.thelawhouse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.thelawhouse.Activity.NewsDetailActivity;
import com.thelawhouse.ClickListener.RecyclerViewClickListener;
import com.thelawhouse.ClickListener.RecyclerViewClickListener2;
import com.thelawhouse.Model.NewsListModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.databinding.AdapterNewsListBinding;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewsListModel.News_data> caseList_data = new ArrayList<>();
    private Context mContext;
    private RecyclerViewClickListener mClickListener;
    private RecyclerViewClickListener2 mClickListener2;

    public NewsListAdapter(List<NewsListModel.News_data> caseList_data, Context context, RecyclerViewClickListener mClickListeneR, RecyclerViewClickListener2 mClickListener2) {
        this.mContext = context;
        this.caseList_data = caseList_data;
        mClickListener = mClickListeneR;
        this.mClickListener2 = mClickListener2;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterNewsListBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.adapter_news_list, parent, false);
        return new NewsListAdapter.ItemViewHolder(mBinding.getRoot(), mBinding);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final NewsListAdapter.ItemViewHolder mViewHolder = (NewsListAdapter.ItemViewHolder) holder;
        final NewsListModel.News_data mdata = caseList_data.get(position);
        mViewHolder.mBinding.tvDetail.setText(mdata.contents);
        mViewHolder.mBinding.tvTitle.setText(mdata.title);
        Glide.with(mContext)
                .load(PreferenceHelper.getString(Constants.ImagePath, "") + mdata.news_image)
                .placeholder(R.drawable.img_news_demo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mViewHolder.mBinding.ivNewsImg);
        if (PreferenceHelper.getString(Constants.LOGINTYPE, "").equalsIgnoreCase("guest")) {
            mViewHolder.mBinding.ivMenubar.setVisibility(View.GONE);
        }
        mViewHolder.mBinding.tvTitle.setMovementMethod(LinkMovementMethod.getInstance());
        mViewHolder.mBinding.ivNewsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("linkId", mdata.news_id);
                mContext.startActivity(intent);
            }
        });
        mViewHolder.mBinding.llSubMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("linkId", mdata.news_id);
                mContext.startActivity(intent);
            }
        });
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
                            mClickListener2.ImageViewListClicked(mdata.news_id);
//                            Intent intent = new Intent(mContext, AddNewsActivity.class);
//                            intent.putExtra("linkId", mdata.news_id);
//                            mContext.startActivity(intent);
                        } else {
                            caseList_data.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeRemoved(position, caseList_data.size());
                            mClickListener.ImageViewListClicked(mdata.news_id);
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
        private AdapterNewsListBinding mBinding;

        ItemViewHolder(View itemView, AdapterNewsListBinding mBinding) {
            super(itemView);
            this.mBinding = mBinding;
        }
    }

    public void addItems(List<NewsListModel.News_data> items) {
        caseList_data.addAll(items);
        notifyDataSetChanged();
    }
}
