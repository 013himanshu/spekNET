package com.example.himanshu.speknet;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.ViewHolder> {
    HomeFragment mCtx;
    private List<NewsFeed> newsItems;

    public NewsFeedAdapter(HomeFragment mCtx, List<NewsFeed> newsItems) {
        this.mCtx = mCtx;
        this.newsItems = newsItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_feed, parent, false);
        ViewHolder ViewHolder=new ViewHolder(v);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsFeed listItem = newsItems.get(position);
        holder.textName.setText(listItem.getUserName());
        holder.textPost.setText(listItem.getPostText());
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName;
        public TextView textPost;

        public ViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textPost = itemView.findViewById(R.id.textPost);
        }
    }
}
