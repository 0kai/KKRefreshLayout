package net.z0kai.refreshlayout;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import net.z0kai.refreshlayout.view.IFooterView;
import net.z0kai.refreshlayout.view.IHeaderView;

/**
 * Created by Z_0Kai on 16/9/29.
 */

public class KKRefreshLayout extends FrameLayout implements NestedScrollingParent,
        NestedScrollingChild {

    private final static String TAG = KKRefreshLayout.class.getSimpleName();
    private final static long MIN_REFRESH_TIME = 500;
    private static final float DRAG_RATE = .5f;

    private boolean isRefreshing = false;
    private boolean isLoadingMore = false;
    private boolean isRefreshEnable = true;
    private boolean isLoadMoreEnable = false;

    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];
    private boolean mNestedScrollInProgress;

    private int mTouchSlop;
    private float mTouchY = -1;
    private float mCurrentY;

    private View mTarget; // the target of the gesture
    private IHeaderView mHeaderView;
    private IFooterView mFooterView;

    private KKRefreshListener mListener;

    private float mOffset;
    private long mStartRefreshTime;
    private boolean isRefreshBeforeLayout;
    private Runnable mMoveBackRunnable = new Runnable() {
        @Override
        public void run() {
            smoothScrollBack(mOffset, 0);
        }
    };

    public KKRefreshLayout(Context context) {
        this(context, null);
    }

    public KKRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);

        mHeaderView = KKRefreshLayoutConfig.getHeaderViewProvider().get(context);
        addView(mHeaderView.getView());

        mFooterView = KKRefreshLayoutConfig.getFooterViewProvider().get(context);
        addView(mFooterView.getView());

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (getChildCount() == 0) {
            return;
        }
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        layoutChildren();
        if (isRefreshBeforeLayout) {
            isRefreshBeforeLayout = false;
            startRefresh();
        }
    }

    public void startRefresh() {
        if (isRefreshing || !isRefreshEnable) {
            return;
        }
        if (getMeasuredWidth() == 0) {
            isRefreshBeforeLayout = true;
            return;
        }
        mStartRefreshTime = System.currentTimeMillis();
        isRefreshing = true;
        mHeaderView.startRefresh();

        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, mHeaderView.getRefreshingSize()).setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mOffset = value;
                layoutChildren();
                if (value == mHeaderView.getRefreshingSize() && mListener != null) {
                    mListener.onRefresh();
                }
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setStartDelay(100);
        valueAnimator.start();
    }

    public void startLoadMore() {
        if (isLoadingMore || !isLoadMoreEnable) {
            return;
        }
        isLoadingMore = true;
        if (mListener != null) {
            mListener.onLoadMore();
        }
    }

    public void setRefreshEnable(boolean enable) {
        isRefreshEnable = enable;
    }

    public void setLoadMoreEnable(boolean enable) {
        isLoadMoreEnable = enable;
    }

    public void finishRefresh() {
        if (isRefreshing) {
            long refreshTime = System.currentTimeMillis() - mStartRefreshTime;
            if (refreshTime < MIN_REFRESH_TIME) {
                postDelayed(mMoveBackRunnable, MIN_REFRESH_TIME - refreshTime);
            } else {
                mMoveBackRunnable.run();
            }
        }
    }

    public void finishLoadMore() {
        if(isLoadingMore) {
            isLoadingMore = false;
            mOffset = 0;
//            layoutChildren();
            if (mTarget != null) {
                mTarget.requestLayout();
            }
        }
    }

    public void setRefreshListener(KKRefreshListener listener) {
        mListener = listener;
    }

    private void layoutChildren() {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int offset = (int) mOffset;
        int left, right, top, bottom;

        if (mHeaderView != null) {
            View view = mHeaderView.getView();
            left = paddingLeft;
            top = paddingTop - view.getMeasuredHeight() + offset;
            right = left + view.getMeasuredWidth();
            bottom = top + view.getMeasuredHeight();
            view.layout(left, top, right, bottom);
            mHeaderView.onScroll(offset);
        }

        if (mFooterView != null) {
            View view = mFooterView.getView();
            left = paddingLeft;
            top = paddingTop + getHeight() + offset;
            right = left + view.getMeasuredWidth();
            bottom = top + view.getMeasuredHeight();
            view.layout(left, top, right, bottom);
        }

        if (mTarget != null) {
            View view = mTarget;
            left = paddingLeft;
            right = left + getWidth();
            if (offset > 0) {
                top = paddingTop + offset;
                bottom = top + getHeight() - offset;
            } else {
                top = paddingTop;
                bottom = top + getHeight() + offset;
            }
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

    private void finishSpinner(float overScrollTop) {
        if (overScrollTop >= mHeaderView.getMinRefreshSize()) {
            if (mListener != null) {
                mListener.onRefresh();
            }
            mStartRefreshTime = System.currentTimeMillis();
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
                mOffset = value;
                layoutChildren();
                if (value == 0) {
                    isRefreshing = false;
                    isLoadingMore = false;
                    mHeaderView.stopRefresh();
                }
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setStartDelay(100);
        valueAnimator.start();
    }

    private void offsetUp(int dy) {
        if (mOffset < getHeight() / 3) {
            dy /= 2;
        } else if (mOffset < getHeight() / 2){
            dy /= 3;
        } else {
            dy /= 4;
        }
        mOffset += dy;
    }

    private void moveChild(int dy, int dyConsumed) {
        // pull down
        if (dy < 0 && isRefreshEnable && !isLoadingMore && !canChildScrollUp()) {
            offsetUp(-dy);
            layoutChildren();
        }

        // pull up
        if (dy > 0 && isLoadMoreEnable && !isRefreshing && !canChildScrollDown()) {
            if (!isLoadingMore) {
                mFooterView.showLoading();
                isLoadingMore = true;
                if (mListener != null) {
                    mListener.onLoadMore();
                }
            }
            mOffset -= dy;
            if (-mOffset > mFooterView.getSize()) {
                mOffset = -mFooterView.getSize();
            }
            layoutChildren();
        }

        // pull up, show no more
        if (dy > 0 && canChildScrollUp() && !isLoadMoreEnable) {
            mFooterView.showNoMore();
            mOffset -= dy;
            if (-mOffset > mFooterView.getSize()) {
                mOffset = -mFooterView.getSize();
            }
            layoutChildren();
        }

        // hide pull up item
        if (dyConsumed < 0 && mOffset < 0) {
            mOffset -= dyConsumed;
            if (mOffset > 0) {
                mOffset = 0;
            }
            layoutChildren();
        }

    }

    private void actionMove(int dy) {
        moveChild(dy, dy);

        // pull down, back
        if (dy > 0 && mOffset > 0) {
            if (dy > mOffset) {
                mOffset = 0;
            } else {
                mOffset -= dy;
            }
            layoutChildren();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (isRefreshing || mNestedScrollInProgress) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = ev.getY();
                mCurrentY = mTouchY;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mTouchY == -1) {
                    break;
                }
                float currentY = ev.getY();
                int dy = (int) (currentY - mTouchY);
                if (mOffset != 0) {

                } else if (Math.abs(dy) < mTouchSlop) {
                    break;
                } else if (dy > 0 && !canChildScrollUp() && isRefreshEnable) {
//                    return true;
                } else if (dy < 0 && !canChildScrollDown()) {
//                    return true;
                } else if (mOffset != 0) {
//                    return true;
                } else {
                    mTouchY = currentY;
                    break;
                }
                mCurrentY = mTouchY = currentY;
                dy = -dy;
                actionMove(dy);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                onStopNestedScroll(mTarget);
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isRefreshing || mNestedScrollInProgress || canChildScrollUp()) {
            // Fail fast if we're not in a state where a swipe is possible
            return super.onTouchEvent(ev);
        }

        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = ev.getY();
                mCurrentY = mTouchY;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mTouchY == -1) {
                    break;
                }
                mCurrentY = ev.getY();
                int dy = - (int) (mCurrentY - mTouchY);
                mTouchY = mCurrentY;
                actionMove(dy);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                onStopNestedScroll(mTarget);
                break;
        }

        return true;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        // if this is a List < L or another view that doesn't support nested
        // scrolling, ignore this request so that the vertical scroll event
        // isn't stolen
        if ((android.os.Build.VERSION.SDK_INT < 21 && mTarget instanceof AbsListView)
                || (mTarget != null && !ViewCompat.isNestedScrollingEnabled(mTarget))
                || !isRefreshEnable) {
            // Nope.
        } else {
            super.requestDisallowInterceptTouchEvent(b);
        }
    }

    // NestedScrollingParent
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
//        return super.onStartNestedScroll(child, target, nestedScrollAxes);
        return isEnabled()
                && !isRefreshing
                && (isRefreshEnable) // and then load more will run on touch
                && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        // Dispatch up to the nested parent
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mNestedScrollInProgress = true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
        // before allowing the list to scroll
        if (dy > 0 && mOffset > 0) {
            if (dy > mOffset) {
                consumed[1] = dy - (int) mOffset;
                mOffset = 0;
            } else {
                mOffset -= dy;
                consumed[1] = dy;
            }
            layoutChildren();
        }

        // Now let our nested parent consume the leftovers
        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mNestedScrollInProgress = false;
        // Finish the spinner for nested scrolling if we ever consumed any
        // unconsumed nested scroll
        if (mOffset > 0) {
            finishSpinner(mOffset);
//            mTotalUnconsumed = 0;
        } else {
            mHeaderView.stopRefresh();
        }
        // Dispatch up our nested parent
        stopNestedScroll();
        mTouchY = -1;
    }

    @Override
    public void onNestedScroll(final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        // Dispatch up to the nested parent first
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, mParentOffsetInWindow);

        // This is a bit of a hack. Nested scrolling works from the bottom up, and as we are
        // sometimes between two nested scrolling views, we need a way to be able to know when any
        // nested scrolling parent has stopped handling events. We do that by using the
        // 'offset in window 'functionality to see if we have been moved from the event.
        // This is a decent indication of whether we should take over the event stream or not.
        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
        moveChild(dy, dyConsumed);
    }

    // NestedScrollingChild
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(
                dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX,
                                    float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY,
                                 boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

}
