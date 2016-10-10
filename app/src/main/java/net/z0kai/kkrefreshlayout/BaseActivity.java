package net.z0kai.kkrefreshlayout;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import net.z0kai.refreshlayout.KKRefreshLayout;

/**
 * Created by Z_0Kai on 16/10/9.
 */

public class BaseActivity extends AppCompatActivity {
    protected KKRefreshLayout refreshLayout;

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
