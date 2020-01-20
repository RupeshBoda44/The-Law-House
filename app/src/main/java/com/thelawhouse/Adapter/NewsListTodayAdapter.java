package com.thelawhouse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thelawhouse.Activity.NewsDetailActivity;
import com.thelawhouse.ClickListener.RecyclerViewClickListener;
import com.thelawhouse.Model.NewsListTodayModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.databinding.AdapterNewsListBinding;
import com.thelawhouse.databinding.AdapterNewsListTodayBinding;

import java.util.ArrayList;
import java.util.List;

public class NewsListTodayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewsListTodayModel.News_data> caseList_data = new ArrayList<>();
    private Context mContext;
    private RecyclerViewClickListener mClickListener;

    public NewsListTodayAdapter(Context context, RecyclerViewClickListener mClickListeneR) {
        this.mContext = context;
        caseList_data = new ArrayList<>();
        mClickListener = mClickListeneR;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterNewsListTodayBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.adapter_news_list_today, parent, false);
        return new NewsListTodayAdapter.ItemViewHolder(mBinding.getRoot(), mBinding);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final NewsListTodayAdapter.ItemViewHolder mViewHolder = (NewsListTodayAdapter.ItemViewHolder) holder;
        final NewsListTodayModel.News_data mdata = caseList_data.get(position);
        mViewHolder.mBinding.tvDetail.setText(mdata.contents);
        mViewHolder.mBinding.tvTitle.setText(mdata.title);
        Glide.with(mContext)
                .load(PreferenceHelper.getString(Constants.ImagePath, "") + mdata.news_image)
                .placeholder(R.drawable.img_news_demo)
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
        mViewHolder.mBinding.ivMenubar.setVisibility(View.GONE);
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
        private AdapterNewsListTodayBinding mBinding;

        ItemViewHolder(View itemView, AdapterNewsListTodayBinding mBinding) {
            super(itemView);
            this.mBinding = mBinding;
        }
    }

    public void addItems(List<NewsListTodayModel.News_data> items) {
        caseList_data.addAll(items);
        notifyDataSetChanged();
    }
}
