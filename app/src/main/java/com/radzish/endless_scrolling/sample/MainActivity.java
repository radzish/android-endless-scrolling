package com.radzish.endless_scrolling.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.radzish.endless_scrolling.EndlessAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        EndlessAdapter adapter = new MyAdapter(recyclerView, new EndlessAdapter.OnLoadListener<Integer>() {

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
