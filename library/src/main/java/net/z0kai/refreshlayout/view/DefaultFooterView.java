package net.z0kai.refreshlayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Created by Z_0Kai on 16/9/29.
 */

public class DefaultFooterView extends ProgressBar implements IFooterView {
    public DefaultFooterView(Context context) {
        this(context, null);
    }

    public DefaultFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        setLayoutParams(lp);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public int getSize() {
        return 200;
    }
}
