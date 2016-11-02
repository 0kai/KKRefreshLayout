package net.z0kai.kkrefreshlayout_demo.headerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import net.z0kai.kkrefreshlayout.IHeaderView;

/**
 * Created by Z_0Kai on 16/10/23.
 * An empty header view, can move down but would not call refresh
 */

public class EmptyHeaderView extends View implements IHeaderView {

    public EmptyHeaderView(Context context) {
        super(context);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public int getMinRefreshSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getRefreshingSize() {
        return 0;
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
