package net.z0kai.kkrefreshlayout_demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import net.z0kai.kkrefreshlayout.KKRefreshLayout;
import net.z0kai.kkrefreshlayout.KKRefreshListener;

public class MainActivity extends BaseActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.z0kai.kkrefreshlayout_demo.R.layout.activity_main);

        refreshLayout = (KKRefreshLayout) findViewById(net.z0kai.kkrefreshlayout_demo.R.id.refreshLayout);

        refreshLayout.setRefreshListener(new KKRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
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
                    }
                }, 2000);
            }
        });
        bindEvent();
    }

    private void bindEvent() {
        findViewById(net.z0kai.kkrefreshlayout_demo.R.id.rcvBtn).setOnClickListener(this);
        findViewById(net.z0kai.kkrefreshlayout_demo.R.id.rcvHBtn).setOnClickListener(this);
        findViewById(net.z0kai.kkrefreshlayout_demo.R.id.lvBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case net.z0kai.kkrefreshlayout_demo.R.id.rcvBtn:
                startActivity(new Intent(this, RecyclerViewActivity.class));
                break;
            case net.z0kai.kkrefreshlayout_demo.R.id.rcvHBtn:
                startActivity(new Intent(this, RecyclerViewHActivity.class));
                break;
            case net.z0kai.kkrefreshlayout_demo.R.id.lvBtn:
                startActivity(new Intent(this, ListViewActivity.class));
                break;
        }
    }
}
