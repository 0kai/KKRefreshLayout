package net.z0kai.kkrefreshlayout;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import net.z0kai.refreshlayout.KKRefreshLayout;
import net.z0kai.refreshlayout.KKRefreshListener;

public class MainActivity extends AppCompatActivity {

    private KKRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private TestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshLayout = (KKRefreshLayout) findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TestAdapter(this);
        recyclerView.setAdapter(adapter);

        refreshLayout.setLoadMoreEnable(true);
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
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        adapter.addData();
                    }
                }, 2000);
            }
        });
        refreshLayout.startRefresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_start_refresh:
                refreshLayout.startRefresh();
                break;
            case R.id.menu_start_load_more:
                refreshLayout.startLoadMore();
                break;

            case R.id.menu_refresh_enable:
                refreshLayout.setRefreshEnable(true);
                break;
            case R.id.menu_refresh_disable:
                refreshLayout.setRefreshEnable(false);
                break;

            case R.id.menu_load_more_enable:
                refreshLayout.setLoadMoreEnable(true);
                break;
            case R.id.menu_load_more_disable:
                refreshLayout.setLoadMoreEnable(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
