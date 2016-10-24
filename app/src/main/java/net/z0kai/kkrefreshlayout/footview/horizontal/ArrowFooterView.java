package net.z0kai.kkrefreshlayout.footview.horizontal;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import net.z0kai.kkrefreshlayout.R;
import net.z0kai.kkrefreshlayout.utils.DensityUtil;
import net.z0kai.refreshlayout.view.IFooterView;

public class ArrowFooterView extends View implements IFooterView {
    private Path mPath;
    private Paint mPaint;
    private int mMinimumHeight;

    private float mSupX, mSupY;
    private int mFlyPercent = 0;

    private int mBitmapX;
    private int mBitmapY;

    private boolean isDrawBack = false;

    private int mArrowHeight;
    private Bitmap mArrowBitmap;

    private int mArrowMaxWidth;

    private int mVisibleWidth;

    public ArrowFooterView(Context context) {
        this(context, null, 0);
    }

    public ArrowFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArrowFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ArrowFooterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        if (isInEditMode()) {
            return;
        }
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(android.R.color.black));
        mPaint.setAntiAlias(true);

        mArrowBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_white);
        mArrowHeight = DensityUtil.dip2px(getContext(), 60);
        mArrowMaxWidth = DensityUtil.dip2px(getContext(), 100);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(mArrowMaxWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int measuredWidth = mVisibleWidth;
        int measuredHeight = getMeasuredHeight();
        mPath.reset();

        mSupY = measuredHeight / 2;
        Bitmap bitmap = mArrowBitmap;

        int top = (measuredHeight - mArrowHeight) / 2;

        if (isDrawBack) {

            mPath.moveTo(measuredWidth, top);
            mPath.quadTo(mSupX, mSupY, measuredWidth, measuredHeight - top);
            canvas.drawPath(mPath, mPaint);

            if (mFlyPercent < 100) {
                canvas.drawBitmap(bitmap, mBitmapX, mBitmapY, mPaint);
                mFlyPercent += 5;
                postInvalidateDelayed(10);
            } else {
                mFlyPercent = 0;
                isDrawBack = false;
            }

        } else {

            mPath.moveTo(measuredWidth, top);
            mPath.quadTo(mSupX, mSupY, measuredWidth, measuredHeight - top);
            canvas.drawPath(mPath, mPaint);

            mBitmapX = (int) (measuredWidth - (measuredWidth - mSupX) / 2 + 20);
            mBitmapY = (int) (mSupY - bitmap.getHeight() / 2);

            canvas.drawBitmap(bitmap, mBitmapX, mBitmapY, mPaint);

        }

    }

    @Override
    public void setMinimumHeight(int minimumHeight) {
        this.mMinimumHeight = minimumHeight;
    }

    @Override
    public int getMinimumHeight() {
        return mMinimumHeight;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public int getMaxSize() {
        return mArrowMaxWidth;
    }

    @Override
    public int getMinLoadMoreSize() {
        return mArrowMaxWidth / 2;
    }

    @Override
    public int getLoadingSize() {
        return 0;
    }

    @Override
    public void onScroll(float offset) {
        if (offset < 0) {
            mVisibleWidth = - (int) offset;
            invalidate();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showNoMore() {

    }
}
