package net.z0kai.refreshlayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Created by Z_0Kai on 16/9/29.
 */

public class DefaultHeaderView extends ProgressBar implements IHeaderView {

    public DefaultHeaderView(Context context) {
        this(context, null);
    }

    public DefaultHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        setLayoutParams(lp);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public int getMinRefreshSize() {
        return 200;
    }

    @Override
    public int getRefreshingSize() {
        return 100;
    }

    @Override
    public void onScroll(float offset) {

    }

    @Override
    public void startRefresh() {
    }

    @Override
    public void stopRefresh() {
    }
}
