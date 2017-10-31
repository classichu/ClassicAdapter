package com.classichu.classicadapter;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.classichu.adapter.helper.ClassicEmptyViewHelper;
import com.classichu.adapter.listener.OnRVItemTouchListener;
import com.classichu.adapter.recyclerview.ClassicRVHeaderFooterAdapter;
import com.classichu.adapter.recyclerview.ClassicRecyclerViewAdapter;
import com.classichu.adapter.widget.ClassicEmptyView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> mDatas = new ArrayList<>();
    RecyclerView id_recycler_view;
    RecyclerViewRVHFAdapter recyclerViewRVHFAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 15; i++) {
           //mDatas.add("dsdas" + i);
        }

        ListView id_list_view = (ListView) findViewById(R.id.id_list_view);
        id_list_view.setAdapter(new ListViewAdapter(mDatas,R.layout.item_classic_list));
        id_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "lv onItemClick" + position, Toast.LENGTH_SHORT).show();
            }
        });
        id_list_view.setVisibility(View.GONE);

        Button id_tb = (Button) findViewById(R.id.id_tb);
        id_tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_recycler_view.getLayoutManager() instanceof GridLayoutManager) {
                    id_recycler_view.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                }else  if (id_recycler_view.getLayoutManager() instanceof LinearLayoutManager) {
                    id_recycler_view.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                }else {
                    id_recycler_view.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                    recyclerViewRVHFAdapter.callAfterChangeGridLayoutManager(id_recycler_view);
                }
                //  id_recycler_view.setAdapter(recyclerViewRVHFAdapter);
               //  recyclerViewRVHFAdapter.notifyDataSetChanged();
            }
        });

         id_recycler_view = (RecyclerView) findViewById(R.id.id_recycler_view);
        id_recycler_view.setLayoutManager(new LinearLayoutManager(this));
       // id_recycler_view.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
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
        ClassicEmptyView classicEmptyView=ClassicEmptyViewHelper.getClassicEmptyView(this, new ClassicEmptyView.OnEmptyViewClickListener() {
            @Override
            public void onClickEmptyView(View view) {
                super.onClickEmptyView(view);
                recyclerViewRVHFAdapter.addDataAtEnd("dsadas111111");
            }
        });

        //
        //id_recycler_view.setVisibility(View.GONE);
         recyclerViewRVHFAdapter =
                new RecyclerViewRVHFAdapter(this,mDatas,R.layout.item_classic_list);
        recyclerViewRVHFAdapter.setEmptyView(classicEmptyView);
        recyclerViewRVHFAdapter.setOnItemClickListener(new ClassicRVHeaderFooterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                super.onItemClick(itemView, position);
                Toast.makeText(MainActivity.this, "recyclerViewRVHFAdapter:" + position, Toast.LENGTH_SHORT).show();
                recyclerViewRVHFAdapter.refreshDataList(new ArrayList<String>());
            }
        });

        TextView textView1 = new TextView(this);
        textView1.setBackgroundColor(ContextCompat.getColor(this,R.color.colorAccent));
        textView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView1.setText("head01");

        TextView textView2 = new TextView(this);
        textView2.setBackgroundColor(ContextCompat.getColor(this,R.color.colorAccent));
        textView2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView2.setText("head02");
        recyclerViewRVHFAdapter.addHeaderView(textView1);
       /* recyclerViewRVHFAdapter.addHeaderView(textView2);*/


        TextView textView3 = new TextView(this);
        textView3.setBackgroundColor(ContextCompat.getColor(this,R.color.colorAccent));
        textView3.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView3.setText("foot01");

        TextView textView4 = new TextView(this);
        textView4.setBackgroundColor(ContextCompat.getColor(this,R.color.colorAccent));
        textView4.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView4.setText("foot02");
        recyclerViewRVHFAdapter.addFooterView(textView4);
        /*recyclerViewRVHFAdapter.addFooterView(textView4);*/

        id_recycler_view.addOnItemTouchListener(new OnRVItemTouchListener(id_recycler_view) {

            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        id_recycler_view.setAdapter(recyclerViewRVHFAdapter);
    }
}
