package net.z0kai.refreshlayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.z0kai.refreshlayout.R;

/**
 * Created by Z_0Kai on 16/9/29.
 */

public class DefaultFooterView extends RelativeLayout implements IFooterView {

    private ProgressBar loadingPb;
    private TextView infoTv;

    public DefaultFooterView(Context context) {
        this(context, null);
    }

    public DefaultFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.kk_rl_default_footer_view, this);
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
    public int getMaxSize() {
        return getHeight();
    }

    @Override
    public int getMinLoadMoreSize() {
        return 0;
    }

    @Override
    public int getLoadingSize() {
        return -1;
    }

    @Override
    public void onScroll(float offset) {

    }

    @Override
    public void showLoading() {
        if (loadingPb.getVisibility() != VISIBLE) {
            loadingPb.setVisibility(VISIBLE);
            infoTv.setText(R.string.kk_rl_loading_more);
            measure(getMeasuredWidth(), getMeasuredHeight());
        }
    }

    @Override
    public void showNoMore() {
        if (loadingPb.getVisibility() != GONE) {
            loadingPb.setVisibility(GONE);
            infoTv.setText(R.string.kk_rl_no_more_data);
            measure(getMeasuredWidth(), getMeasuredHeight());
        }
    }
}
