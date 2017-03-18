package net.z0kai.kkrefreshlayout;

import android.view.View;

/**
 * Created by Z_0Kai on 17/3/17.
 */

public interface IPageView {
    View getView();

    /**
     * call when show (maybe add some animate)
     */
    void show();

    /**
     * call when hide (maybe release resource)
     * it will call after {@link KKRefreshLayout#finishRefresh}
     */
    void hide();
}
