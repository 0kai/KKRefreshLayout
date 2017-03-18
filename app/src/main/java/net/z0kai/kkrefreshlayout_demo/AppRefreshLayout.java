package net.z0kai.kkrefreshlayout_demo;

import android.content.Context;
import android.util.AttributeSet;

import net.z0kai.kkrefreshlayout.IFooterView;
import net.z0kai.kkrefreshlayout.IHeaderView;
import net.z0kai.kkrefreshlayout.IPageView;
import net.z0kai.kkrefreshlayout.KKRefreshLayout;
import net.z0kai.kkrefreshlayout.KKRefreshListener;
import net.z0kai.kkrefreshlayout_demo.config.KKRefreshLayoutConfig;
import net.z0kai.kkrefreshlayout_demo.pageview.LoadingPageView;

/**
 * Created by Z_0Kai on 16/9/30.
 * APP封装新控件,请尽量不要使用到原来刷新控件的接口,封装后方便替换
 */
public class AppRefreshLayout extends KKRefreshLayout {

    private AppRefreshLayoutListener mListener;

    public AppRefreshLayout(Context context) {
        this(context, null);
    }

    public AppRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void startRefresh() {
        super.startRefresh();
    }

    public void startLoadMore() {
        super.startLoadMore();
    }

    public void finishRefresh() {
        super.finishRefresh();
    }

    public void finishLoadMore() {
        super.finishLoadMore();
    }

    public void setRefreshEnable(boolean enable) {
        super.setRefreshEnable(enable);
    }

    public void setLoadMoreEnable(boolean enable) {
        super.setLoadMoreEnable(enable);
    }

    public interface AppRefreshLayoutListener {
        void onRefresh();

        void onLoadMore();
    }

    public static class AppRefreshLayoutAdapter implements AppRefreshLayoutListener {
        public void onRefresh() {
        }

        public void onLoadMore() {
        }
    }

    public void setRefreshLayoutListener(AppRefreshLayoutListener listener) {
        mListener = listener;

        super.setRefreshListener(new KKRefreshListener() {
            @Override
            public void onRefresh() {
                if (mListener != null) {
                    mListener.onRefresh();
                }
            }

            @Override
            public void onLoadMore() {
                if (mListener != null) {
                    mListener.onLoadMore();
                }
            }
        });
    }

    /**
     * @hide
     * @deprecated
     */
    @Override
    public void setRefreshListener(KKRefreshListener listener) {
        super.setRefreshListener(listener);
    }

    @Override
    protected IHeaderView obtainHeaderView() {
        return KKRefreshLayoutConfig.getHeaderViewProvider().get(getContext());
    }

    @Override
    protected IFooterView obtainFooterView() {
        return KKRefreshLayoutConfig.getFooterViewProvider().get(getContext());
    }

    @Override
    protected IPageView obtainPageView() {
        return new LoadingPageView(getContext());
    }
}
