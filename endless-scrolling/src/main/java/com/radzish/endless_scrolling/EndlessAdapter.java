package com.radzish.endless_scrolling;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class EndlessAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> list;

    private int VISIBLE_THRESHOLD = 10;

    private int lastVisibleItem, firstVisibleItem;
    private boolean loading;

    private int PAGE_SIZE = 30;
    private int TOTAL_SIZE = PAGE_SIZE * 3;

    private Handler handler;

    public EndlessAdapter(RecyclerView recyclerView, final OnLoadListener<T> onLoadMoreListener) {
        list = new ArrayList<>();
        handler = new Handler();

        list.add(onLoadMoreListener.onLoadFirst());

        for (int i = 0; i < PAGE_SIZE * 2 - 1; i++) {
            list.add(onLoadMoreListener.onLoadNext(list.get(list.size() - 1)));
        }

        for (int i = 0; i < PAGE_SIZE; i++) {
            list.add(0, onLoadMoreListener.onLoadPrev(list.get(0)));
        }

        recyclerView.scrollToPosition(PAGE_SIZE);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                int delta = linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL ? dy : dx;

                if (!loading && delta != 0) {

                    //scrolling up
                    if (delta > 0 && (firstVisibleItem >= TOTAL_SIZE - VISIBLE_THRESHOLD - (lastVisibleItem - firstVisibleItem))) {

                        loading = true;

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                int numberOfItemsToLoad = firstVisibleItem + 2 * PAGE_SIZE - TOTAL_SIZE;

                                for (int i = 0; i < numberOfItemsToLoad; i++) {
                                    list.add(onLoadMoreListener.onLoadNext(list.get(list.size() - 1)));
                                }

                                for (int i = 0; i < numberOfItemsToLoad; i++) {
                                    list.remove(0);
                                }

                                notifyItemRangeRemoved(0, numberOfItemsToLoad);
                                notifyItemRangeInserted(lastVisibleItem + 1, numberOfItemsToLoad);

                                loading = false;
                            }
                        });

                    }

                    //scrolling down
                    if (delta < 0 && firstVisibleItem <= VISIBLE_THRESHOLD + 1) {

                        loading = true;

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                int numberOfItemsToLoad = PAGE_SIZE - VISIBLE_THRESHOLD;

                                for (int i = 0; i < numberOfItemsToLoad; i++) {
                                    list.add(0, onLoadMoreListener.onLoadPrev(list.get(0)));
                                }

                                int listSize = list.size();

                                for (int i = 0; i < numberOfItemsToLoad; i++) {
                                    list.remove(listSize - numberOfItemsToLoad);
                                }

                                notifyItemRangeRemoved(listSize - numberOfItemsToLoad, numberOfItemsToLoad);
                                notifyItemRangeInserted(0, numberOfItemsToLoad);

                                loading = false;
                            }
                        });

                    }


                }
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindViewHolder(holder, list.get(position));
    }

    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, T item);

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnLoadListener<T> {

        T onLoadFirst();

        T onLoadPrev(T t);

        T onLoadNext(T t);

    }

}