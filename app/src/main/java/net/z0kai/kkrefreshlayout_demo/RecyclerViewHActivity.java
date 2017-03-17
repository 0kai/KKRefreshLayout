package net.z0kai.kkrefreshlayout_demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

public class RecyclerViewHActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private TestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.z0kai.kkrefreshlayout_demo.R.layout.activity_recycler_view_h);

        refreshLayout = (AppRefreshLayout) findViewById(net.z0kai.kkrefreshlayout_demo.R.id.refreshLayout);
        recyclerView = (RecyclerView) findViewById(net.z0kai.kkrefreshlayout_demo.R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new TestAdapter(this);
        recyclerView.setAdapter(adapter);

        refreshLayout.setRefreshLayoutListener(new AppRefreshLayout.AppRefreshLayoutListener() {
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
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
//                        adapter.addData();
                        Toast.makeText(RecyclerViewHActivity.this, "on load more", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
            }
        });

        refreshLayout.setHeaderView(RefreshLayoutHConfig.getHeaderViewProvider().get(this));
        refreshLayout.setFooterView(RefreshLayoutHConfig.getFooterViewProvider().get(this));
    }

    @Override
    protected boolean isHorizontal() {
        return true;
    }
}
