package net.z0kai.kkrefreshlayout_demo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import net.z0kai.kkrefreshlayout.FooterViewProvider;
import net.z0kai.kkrefreshlayout.HeaderViewProvider;
import net.z0kai.kkrefreshlayout.IFooterView;
import net.z0kai.kkrefreshlayout.IHeaderView;
import net.z0kai.kkrefreshlayout.KKRefreshLayout;
import net.z0kai.kkrefreshlayout.KKRefreshLayoutConfig;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Z_0Kai on 16/10/9.
 */

public class BaseActivity extends AppCompatActivity {
    protected KKRefreshLayout refreshLayout;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(net.z0kai.kkrefreshlayout_demo.R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case net.z0kai.kkrefreshlayout_demo.R.id.menu_set_header:
                showSetHeaderDialog();
                break;
            case net.z0kai.kkrefreshlayout_demo.R.id.menu_set_footer:
                showSetFooterDialog();
                break;
            case net.z0kai.kkrefreshlayout_demo.R.id.menu_start_refresh:
                refreshLayout.startRefresh();
                break;
            case net.z0kai.kkrefreshlayout_demo.R.id.menu_start_load_more:
                refreshLayout.startLoadMore();
                break;

            case net.z0kai.kkrefreshlayout_demo.R.id.menu_refresh_enable:
                refreshLayout.setRefreshEnable(true);
                break;
            case net.z0kai.kkrefreshlayout_demo.R.id.menu_refresh_disable:
                refreshLayout.setRefreshEnable(false);
                break;

            case net.z0kai.kkrefreshlayout_demo.R.id.menu_load_more_enable:
                refreshLayout.setLoadMoreEnable(true);
                break;
            case net.z0kai.kkrefreshlayout_demo.R.id.menu_load_more_disable:
                refreshLayout.setLoadMoreEnable(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSetHeaderDialog() {
        final Class[] list = AppConfigs.HEADER_VIEW_CLASS_LIST;
        CharSequence[] items = getDialogItems(list);
        new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isHorizontal()) {
                            RefreshLayoutHConfig.setHeaderViewProvider(getHeaderViewProvider(list[which]));
                        } else {
                            KKRefreshLayoutConfig.setHeaderViewProvider(getHeaderViewProvider(list[which]));
                        }
                        reStartSelf();
                    }
                })
                .show();
    }

    private void showSetFooterDialog() {
        final Class[] list = AppConfigs.FOOTER_VIEW_CLASS_LIST;
        CharSequence[] items = getDialogItems(list);
        new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isHorizontal()) {
                            RefreshLayoutHConfig.setFooterViewProvider(getFooterViewProvider(list[which]));
                        } else {
                            KKRefreshLayoutConfig.setFooterViewProvider(getFooterViewProvider(list[which]));
                        }
                        reStartSelf();
                    }
                })
                .show();
    }

    private HeaderViewProvider getHeaderViewProvider(final Class clazz) {
        return new HeaderViewProvider() {
            @Override
            public IHeaderView get(Context context) {
                try {
                    Constructor constructor = clazz.getDeclaredConstructor(Context.class);
                    return (IHeaderView) constructor.newInstance(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    private FooterViewProvider getFooterViewProvider(final Class clazz) {
        return new FooterViewProvider() {
            @Override
            public IFooterView get(Context context) {
                try {
                    Constructor constructor = clazz.getDeclaredConstructor(Context.class);
                    return (IFooterView) constructor.newInstance(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    private void reStartSelf() {
        startActivity(new Intent(this, getClass()));
        finish();
    }

    private CharSequence[] getDialogItems(Class[] list) {
        List<CharSequence> items = new ArrayList<>();
        for (Class clazz : list) {
            if (!isHorizontal() && clazz.getPackage().getName().contains("horizontal")) {
                continue;
            } else if (isHorizontal() && clazz.getPackage().getName().contains("vertical")) {
                continue;
            }
            items.add(clazz.getSimpleName());
        }
        return items.toArray(new CharSequence[items.size()]);
    }

    protected boolean isHorizontal() {
        return false;
    }

}
