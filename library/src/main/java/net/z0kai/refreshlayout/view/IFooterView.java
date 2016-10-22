package net.z0kai.refreshlayout.view;

import android.view.View;

/**
 * Created by Z_0Kai on 16/9/29.
 */

public interface IFooterView {
    View getView();

    int getMaxSize();

    /**
     * the min size to start load more
     */
    int getMinLoadMoreSize();

    /**
     * the size when loading
     * if value < 0, mean it don't need to scroll back
     */
    int getLoadingSize();

    void onScroll(float offset);

    void showLoading();

    void showNoMore();
}
