package net.z0kai.kkrefreshlayout.vertical;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.z0kai.kkrefreshlayout.R;
import net.z0kai.kkrefreshlayout.IHeaderView;

/**
 * Created by Z_0Kai on 16/10/27.
 */

public class DefaultHeaderView extends RelativeLayout implements IHeaderView {

    private ProgressBar loadingPb;
    private TextView infoTv;

    private boolean isRefreshing;

    public DefaultHeaderView(Context context) {
        this(context, null);
    }

    public DefaultHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.kk_rl_default_header_view, this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);

        loadingPb = (ProgressBar) findViewById(R.id.loadingPb);
        infoTv = (TextView) findViewById(R.id.infoTv);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public int getMinRefreshSize() {
        return getHeight();
    }

    @Override
    public int getRefreshingSize() {
        return infoTv.getHeight();
    }

    @Override
    public void onScroll(float offset) {
        if (isRefreshing) {
            return;
        }
        if (offset >= getHeight()) {
            infoTv.setText(R.string.kk_rl_release_to_refresh);
        } else {
            infoTv.setText(R.string.kk_rl_pull_to_refresh);
        }
    }

    @Override
    public void startRefresh() {
        isRefreshing = true;
        loadingPb.setVisibility(VISIBLE);
        infoTv.setText(R.string.kk_rl_refreshing);
        loadingPb.getParent().requestLayout();
    }

    @Override
    public void stopRefresh() {
        isRefreshing = false;
        loadingPb.setVisibility(GONE);
    }
}
