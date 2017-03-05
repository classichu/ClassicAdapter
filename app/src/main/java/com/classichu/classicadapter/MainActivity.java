package com.classichu.classicadapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.classichu.adapter.recyclerview.ClassicRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 20; i++) {
            mDatas.add("dsdas" + i);
        }


        ListView id_list_view = (ListView) findViewById(R.id.id_list_view);
        id_list_view.setAdapter(new ListViewAdapter(mDatas, android.R.layout.simple_list_item_1));
        id_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "onItemClick"+position, Toast.LENGTH_SHORT).show();
            }
        });
        id_list_view.setVisibility(View.GONE);


        RecyclerView id_recycler_view= (RecyclerView) findViewById(R.id.id_recycler_view);
        id_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter recyclerViewAdapter=new RecyclerViewAdapter(mDatas,android.R.layout.simple_list_item_1);
        recyclerViewAdapter.setOnItemClickListener(new ClassicRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                super.onItemClick(itemView, position);
                Toast.makeText(MainActivity.this, "onItemClick"+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(View itemView, int position) {
                super.onItemLongClick(itemView, position);
                Toast.makeText(MainActivity.this, "onItemLongClick"+position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        id_recycler_view.setAdapter(recyclerViewAdapter);
    }
}
