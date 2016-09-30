package net.z0kai.refreshlayout.view;

import android.view.View;

/**
 * Created by Z_0Kai on 16/9/29.
 */

public interface IHeaderView {
    View getView();

    /**
     * the min size to start refresh
     */
    int getMinRefreshSize();

    /**
     * the size when refreshing
     */
    int getRefreshingSize();

    /**
     * scroll down the header
     * @param offset scroll offset
     */
    void onScroll(float offset);

    void startRefresh();

    void stopRefresh();
}
