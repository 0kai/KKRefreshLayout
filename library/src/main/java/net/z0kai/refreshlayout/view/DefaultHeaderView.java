package net.z0kai.refreshlayout.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.z0kai.refreshlayout.R;

import static android.animation.ValueAnimator.INFINITE;

/**
 * Created by Z_0Kai on 16/9/29.
 */

public class DefaultHeaderView extends RelativeLayout implements IHeaderView {

    private final static int STATUS_NORMAL = 0;
    private final static int STATUS_MOVE = 1;
    private final static int STATUS_FLASH = 2;

    private int mStatus = STATUS_NORMAL;

    private ImageView mRefreshIv;
    private LayoutParams mRefreshLp;

    private ValueAnimator mMoveAnimator;
    private AnimatorSet mFlashAnimatorSet;

    public DefaultHeaderView(Context context) {
        this(context, null);
    }

    public DefaultHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.kk_rl_default_header_view, this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);

        mRefreshIv = (ImageView) findViewById(R.id.refreshIv);
        mRefreshLp = (LayoutParams) mRefreshIv.getLayoutParams();
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public int getMinRefreshSize() {
        return getHeight();
    }

    @Override
    public int getRefreshingSize() {
        return getHeight() / 2;
    }

    @Override
    public void onScroll(float offset) {
        if (offset >= getHeight() && mStatus == STATUS_NORMAL) {
            animationMove();
        }
    }

    @Override
    public void startRefresh() {
        if (mMoveAnimator != null) {
            mMoveAnimator.cancel();
        }
        mRefreshLp.height = mRefreshIv.getWidth();
        mRefreshIv.requestLayout();
        animationFlash();
    }

    @Override
    public void stopRefresh() {
        if (mFlashAnimatorSet != null) {
            mFlashAnimatorSet.cancel();
        }
        reset();
    }

    private void animationMove() {
        mStatus = STATUS_MOVE;
        if (mMoveAnimator == null) {
            mMoveAnimator = ObjectAnimator.ofInt(getHeight(), mRefreshIv.getWidth());
            mMoveAnimator.setInterpolator(new DecelerateInterpolator());
            mMoveAnimator.setDuration(300);
            mMoveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mRefreshLp.height = (int) animation.getAnimatedValue();
                    mRefreshIv.requestLayout();
                }
            });
        }
        mMoveAnimator.start();
    }

    private void animationFlash() {
        mStatus = STATUS_FLASH;
        if (mFlashAnimatorSet == null) {
            ValueAnimator animatorX = ObjectAnimator.ofFloat(mRefreshIv, "scaleX", 0.5f, 2.0f, 0.5f);
            ValueAnimator animatorY = ObjectAnimator.ofFloat(mRefreshIv, "scaleY", 0.5f, 2.0f, 0.5f);
            animatorX.setRepeatCount(INFINITE);
            animatorY.setRepeatCount(INFINITE);
            mFlashAnimatorSet = new AnimatorSet();
            mFlashAnimatorSet.playTogether(animatorX, animatorY);
            mFlashAnimatorSet.setDuration(500);
        }
        mFlashAnimatorSet.setStartDelay(300);
        if (!mFlashAnimatorSet.isStarted()) {
            mFlashAnimatorSet.start();
        }
    }

    private void reset() {
        if (mStatus == STATUS_NORMAL) {
            return;
        }
        mStatus = STATUS_NORMAL;
        mRefreshIv.setScaleX(1);
        mRefreshIv.setScaleY(1);
        mRefreshLp.height = getHeight();
        mRefreshIv.getParent().requestLayout();
    }
}
