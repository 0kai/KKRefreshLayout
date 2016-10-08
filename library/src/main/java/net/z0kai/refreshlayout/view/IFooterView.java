package net.z0kai.refreshlayout.view;

import android.view.View;

/**
 * Created by Z_0Kai on 16/9/29.
 */

public interface IFooterView {
    View getView();

    int getSize();

    void showLoading();

    void showNoMore();
}
