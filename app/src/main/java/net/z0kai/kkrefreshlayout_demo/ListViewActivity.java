package net.z0kai.kkrefreshlayout_demo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import net.z0kai.kkrefreshlayout.KKRefreshLayout;
import net.z0kai.kkrefreshlayout.KKRefreshListener;

public class ListViewActivity extends BaseActivity {

    private ListView listView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.z0kai.kkrefreshlayout_demo.R.layout.activity_list_view);

        refreshLayout = (KKRefreshLayout) findViewById(net.z0kai.kkrefreshlayout_demo.R.id.refreshLayout);
        listView = (ListView) findViewById(net.z0kai.kkrefreshlayout_demo.R.id.listView);
        adapter = new MyAdapter(this);
        listView.setAdapter(adapter);

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

    private class MyAdapter extends SimpleAdapter {
        private Context context;
        private int count = 40;

        public MyAdapter(Context context) {
            super(context, null, 0, null, null);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView;
            if (convertView != null) {
                textView = (TextView) convertView;
            } else {
                textView = new TextView(context);
            }
            textView.setText("Test: " + position);
            return textView;
        }

        @Override
        public int getCount() {
            return count;
        }

        public void refresh() {
            count = 20;
            notifyDataSetChanged();
        }

        public void addData() {
            count += 20;
            notifyDataSetChanged();
        }
    }
}
