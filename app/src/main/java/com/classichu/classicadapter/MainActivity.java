package com.classichu.classicadapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.classichu.adapter.recyclerview.ClassicRVHeaderFooterAdapter;
import com.classichu.adapter.recyclerview.ClassicRecyclerViewAdapter;
import com.classichu.adapter.widget.ClassicEmptyView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 5; i++) {
            mDatas.add("dsdas" + i);
        }

        ListView id_list_view = (ListView) findViewById(R.id.id_list_view);
        id_list_view.setAdapter(new ListViewAdapter(mDatas, R.layout.item_classic_list));
        id_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "lv onItemClick" + position, Toast.LENGTH_SHORT).show();
            }
        });
        id_list_view.setVisibility(View.GONE);


        RecyclerView id_recycler_view = (RecyclerView) findViewById(R.id.id_recycler_view);
        id_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        id_recycler_view.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mDatas, R.layout.item_classic_list);
        recyclerViewAdapter.setOnItemClickListener(new ClassicRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                super.onItemClick(itemView, position);
                Toast.makeText(MainActivity.this, "rv onItemClick" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(View itemView, int position) {
                super.onItemLongClick(itemView, position);
                Toast.makeText(MainActivity.this, "rv onItemLongClick" + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        //## id_recycler_view.setAdapter(recyclerViewAdapter);

        //ClassicEmptyView cev= (ClassicEmptyView) findViewById(R.id.id_cev);
        //cev.setEmptyImage(null);

        ClassicEmptyView classicEmptyView = new ClassicEmptyView(this);
        classicEmptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        classicEmptyView.setOnEmptyViewClickListener(new ClassicEmptyView.OnEmptyViewClickListener() {
            @Override
            public void onClickTextView(View view) {
                super.onClickTextView(view);
                Toast.makeText(MainActivity.this, "onClickTextView", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickImageView(View view) {
                super.onClickImageView(view);
                Toast.makeText(MainActivity.this, "onClickImageView", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClickEmptyView(View view) {
                super.onClickEmptyView(view);
                Toast.makeText(MainActivity.this, "onClickEmptyView", Toast.LENGTH_SHORT).show();
            }
        });
        //
        //id_recycler_view.setVisibility(View.GONE);
        ///// mDatas.clear();
        RecyclerViewRVHFAdapter recyclerViewRVHFAdapter =
                new RecyclerViewRVHFAdapter(R.layout.item_classic_list, mDatas);
        recyclerViewRVHFAdapter.setEmptyView(classicEmptyView);
        recyclerViewRVHFAdapter.setOnItemClickListener(new ClassicRVHeaderFooterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                super.onItemClick(itemView, position);
                Toast.makeText(MainActivity.this, "recyclerViewRVHFAdapter:" + position, Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView1 = new TextView(this);
        textView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView1.setText("head01");

        TextView textView2 = new TextView(this);
        textView2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView2.setText("head02");
        recyclerViewRVHFAdapter.addHeaderView(textView1);
       /* recyclerViewRVHFAdapter.addHeaderView(textView2);*/


        TextView textView3 = new TextView(this);
        textView3.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView3.setText("foot01");

        TextView textView4 = new TextView(this);
        textView4.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView4.setText("foot02");
        recyclerViewRVHFAdapter.addFooterView(textView3);
        /*recyclerViewRVHFAdapter.addFooterView(textView4);*/

        id_recycler_view.setAdapter(recyclerViewRVHFAdapter);
    }
}
