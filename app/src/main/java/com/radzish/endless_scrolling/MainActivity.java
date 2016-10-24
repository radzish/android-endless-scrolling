package com.radzish.endless_scrolling;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    EndlessAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(recyclerView, new EndlessAdapter.OnLoadListener<Integer>() {

            @Override
            public Integer onLoadFirst() {
                return 0;
            }

            @Override
            public Integer onLoadPrev(Integer value) {
                return value - 1;
            }

            @Override
            public Integer onLoadNext(Integer value) {
                return value + 1;
            }

        });

        recyclerView.setAdapter(adapter);

    }

}
