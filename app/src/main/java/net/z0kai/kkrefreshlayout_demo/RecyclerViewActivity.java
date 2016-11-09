package net.z0kai.kkrefreshlayout_demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.z0kai.kkrefreshlayout.KKRefreshLayout;
import net.z0kai.kkrefreshlayout.KKRefreshListener;

public class RecyclerViewActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private TestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.z0kai.kkrefreshlayout_demo.R.layout.activity_recycler_view);

        refreshLayout = (KKRefreshLayout) findViewById(net.z0kai.kkrefreshlayout_demo.R.id.refreshLayout);
        recyclerView = (RecyclerView) findViewById(net.z0kai.kkrefreshlayout_demo.R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TestAdapter(this);
        recyclerView.setAdapter(adapter);

        refreshLayout.setRefreshListener(new KKRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        adapter.refresh();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addData();
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                    }
                }, 2000);
            }
        });
    }
}
