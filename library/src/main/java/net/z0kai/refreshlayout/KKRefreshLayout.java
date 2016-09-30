package net.z0kai.refreshlayout;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import net.z0kai.refreshlayout.view.IFooterView;
import net.z0kai.refreshlayout.view.IHeaderView;

/**
 * Created by Z_0Kai on 16/9/29.
 */

public class KKRefreshLayout extends FrameLayout implements NestedScrollingParent,
        NestedScrollingChild {

    private final static String TAG = KKRefreshLayout.class.getSimpleName();

    private boolean isRefreshing = false;
    private boolean isLoadingMore = false;

    private float mTotalUnconsumed;
    private float mTotalUnconsumedBottom;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];

    private View mTarget; // the target of the gesture
    private IHeaderView mHeaderView;
    private IFooterView mFooterView;

    private KKRefreshListener mListener;
    private DecelerateInterpolator mDecelerateInterpolator;

    public KKRefreshLayout(Context context) {
        this(context, null);
    }

    public KKRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);

        mHeaderView = RefreshLayoutConfig.getHeaderViewProvider().get(context);
        addView(mHeaderView.getView());

        mFooterView = RefreshLayoutConfig.getFooterViewProvider().get(context);
        addView(mFooterView.getView());

        mDecelerateInterpolator = new DecelerateInterpolator();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        final View child = mTarget;
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

        layoutChildren();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        mTarget.measure(MeasureSpec.makeMeasureSpec(
                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
    }

    public void finishRefresh() {
        if (isRefreshing) {
            smoothScrollBack(mTotalUnconsumed, 0);
        }
    }

    public void finishLoadMore() {
        if(isLoadingMore) {
            isLoadingMore = false;
            mTotalUnconsumedBottom = 0;
        }
    }

    public void setRefreshListener(KKRefreshListener listener) {
        mListener = listener;
    }

    private void layoutChildren() {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        if (mHeaderView != null) {
            View view = mHeaderView.getView();
            MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int top = paddingTop + lp.topMargin;
            final int right = left + view.getMeasuredWidth();
            final int bottom = top + view.getMeasuredHeight();
            view.layout(left, top, right, bottom);
        }

        if (mFooterView != null) {
            View view = mFooterView.getView();
            MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
            final int left = paddingLeft + lp.leftMargin;
            final int top = paddingTop + lp.topMargin + getHeight() - view.getMeasuredHeight();
            final int right = left + view.getMeasuredWidth();
            final int bottom = top + view.getMeasuredHeight();
            view.layout(left, top, right, bottom);
        }
    }

    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!(child instanceof IHeaderView) && !(child instanceof IFooterView)) {
                    mTarget = child;
                    break;
                }
            }
        }
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     *         scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp() {
        return ViewCompat.canScrollVertically(mTarget, -1);
    }

    public boolean canChildScrollDown() {
        return ViewCompat.canScrollVertically(mTarget, 1);
    }

    private void moveSpinner(float overscrollTop) {
        Log.e(TAG, "moveSpinner, " + "overscrollTop: " + overscrollTop);
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        if (mTarget != null) {
//            MarginLayoutParams lp = (MarginLayoutParams) mTarget.getLayoutParams();
//            final int left = paddingLeft + lp.leftMargin;
//            final int top = paddingTop + lp.topMargin + (int)overscrollTop;
//            final int right = left + mTarget.getMeasuredWidth();
//            final int bottom = top + mTarget.getMeasuredHeight();
//            mTarget.layout(left, top, right, bottom);
            mTarget.setTranslationY(overscrollTop);
        }

//        if (mHeaderView != null) {
//            View view = mHeaderView.getView();
//            MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
//            final int left = paddingLeft + lp.leftMargin;
//            final int top = paddingTop + lp.topMargin;
//            final int right = left + view.getMeasuredWidth();
//            final int bottom = top + view.getMeasuredHeight();
//            view.layout(left, top, right, bottom);
//        }
    }

    private void finishSpinner(float overScrollTop) {
        if (overScrollTop >= mHeaderView.getMinRefreshSize()) {
            if (mListener != null) {
                mListener.onRefresh();
            }
            mHeaderView.startRefresh();
            isRefreshing = true;
            smoothScrollBack(overScrollTop, mHeaderView.getRefreshingSize());
        } else {
            smoothScrollBack(overScrollTop, 0);
        }
    }

    private void smoothScrollBack(final float fromOffset, float toOffset) {
        if (fromOffset <= toOffset) {
            return;
        }
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(fromOffset, toOffset).setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                moveSpinner(value);
                if (value == 0) {
                    isRefreshing = false;
                    isLoadingMore = false;
                    mHeaderView.stopRefresh();
                }
                mTotalUnconsumed = value;
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.start();
    }

    // NestedScrollingParent
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onStartNestedScroll, " + nestedScrollAxes);
//        return super.onStartNestedScroll(child, target, nestedScrollAxes);
        return isEnabled()
//                && !isRefreshing && !isLoadingMore
                && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        // Dispatch up to the nested parent
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
//        mTotalUnconsumed = 0;
        Log.e(TAG, "onNestedScrollAccepted, " + axes);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.e(TAG, "onNestedPreScroll, " + "dx:" + dx + ", dy:" + dy + ", consumed:" + consumed);
        // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
        // before allowing the list to scroll
        if (dy > 0 && mTotalUnconsumed > 0) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = dy - (int) mTotalUnconsumed;
                mTotalUnconsumed = 0;
            } else {
                mTotalUnconsumed -= dy;
                consumed[1] = dy;
            }
            moveSpinner(mTotalUnconsumed);
        }

        if (dy < 0 && mTotalUnconsumedBottom > 0) {
            if (- dy > mTotalUnconsumedBottom) {
                mTotalUnconsumedBottom = 0;
            } else {
                mTotalUnconsumedBottom += dy;
            }
        }

        // If a client layout is using a custom start position for the circle
        // view, they mean to hide it again before scrolling the child view
        // If we get back to mTotalUnconsumed == 0 and there is more to go, hide
        // the circle so it isn't exposed if its blocking content is moved
//        if (mUsingCustomStart && dy > 0 && mTotalUnconsumed == 0
//                && Math.abs(dy - consumed[1]) > 0) {
//            mCircleView.setVisibility(View.GONE);
//        }
//
        // Now let our nested parent consume the leftovers
        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    @Override
    public int getNestedScrollAxes() {
        Log.e(TAG, "getNestedScrollAxes");
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.e(TAG, "onStopNestedScroll");
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        // Finish the spinner for nested scrolling if we ever consumed any
        // unconsumed nested scroll
        if (mTotalUnconsumed > 0) {
            finishSpinner(mTotalUnconsumed);
//            mTotalUnconsumed = 0;
        }
        // Dispatch up our nested parent
        stopNestedScroll();
    }

    @Override
    public void onNestedScroll(final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        Log.e(TAG, "onNestedScroll, " + "dxConsumed:" + dxConsumed + ", dyConsumed:" + dyConsumed
                + ", dxUnconsumed:" + dxUnconsumed + ", dyUnconsumed:" + dyUnconsumed);
        // Dispatch up to the nested parent first
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                mParentOffsetInWindow);

        // This is a bit of a hack. Nested scrolling works from the bottom up, and as we are
        // sometimes between two nested scrolling views, we need a way to be able to know when any
        // nested scrolling parent has stopped handling events. We do that by using the
        // 'offset in window 'functionality to see if we have been moved from the event.
        // This is a decent indication of whether we should take over the event stream or not.
        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
        if (dy < 0 && !canChildScrollUp()) {
            mTotalUnconsumed += Math.abs(dy);
            moveSpinner(mTotalUnconsumed);
        }

        if (dy > 0 && !canChildScrollDown()) {
            if (!isLoadingMore) {
                isLoadingMore = true;
                if (mListener != null) {
                    mListener.onLoadMore();
                }
            }
            mTotalUnconsumedBottom += Math.abs(dy);
            if (mTotalUnconsumedBottom > mFooterView.getSize()) {
                mTotalUnconsumedBottom = mFooterView.getSize();
            }
            MarginLayoutParams lp = (MarginLayoutParams) mTarget.getLayoutParams();
            final int left = getPaddingLeft() + lp.leftMargin;
            final int top = getPaddingTop() + lp.topMargin;
            final int right = left + mTarget.getMeasuredWidth();
            final int bottom = top + mTarget.getMeasuredHeight() - (int) mTotalUnconsumedBottom;
            mTarget.layout(left, top, right, bottom);
        }
    }

    // NestedScrollingChild

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        Log.e(TAG, "setNestedScrollingEnabled");
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        Log.e(TAG, "isNestedScrollingEnabled");
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        Log.e(TAG, "startNestedScroll, " + axes);
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        Log.e(TAG, "stopNestedScroll");
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        Log.e(TAG, "hasNestedScrollingParent");
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        Log.e(TAG, "dispatchNestedScroll, " + "dxConsumed:" + dxConsumed + ", dyConsumed:" + dyConsumed
                + ", dxUnconsumed:" + dxUnconsumed + ", dyUnconsumed:" + dyUnconsumed
                + ", offsetInWindow:" + offsetInWindow);
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        Log.e(TAG, "dispatchNestedPreScroll," + "dx:" + dx + ", dy:" + dy);
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(
                dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX,
                                    float velocityY) {
        Log.e(TAG, "onNestedPreFling," + "velocityX:" + velocityX + ", velocityY:" + velocityY);
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY,
                                 boolean consumed) {
        Log.e(TAG, "onNestedFling," + "consumed:" + consumed);
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        Log.e(TAG, "dispatchNestedFling, " + "consumed:" + consumed);
        return super.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

}
