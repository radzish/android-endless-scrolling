package com.radzish.endless_scrolling.sample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.radzish.endless_scrolling.EndlessAdapter;

public class MyAdapter extends EndlessAdapter<Integer> {

    public MyAdapter(RecyclerView recyclerView, OnLoadListener onLoadMoreListener) {
        super(recyclerView, onLoadMoreListener);
    }


    public static class TextViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public TextViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(android.R.id.text1);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new TextViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Integer item) {
        ((TextViewHolder) holder).mTextView.setText(item.toString());
    }
}
