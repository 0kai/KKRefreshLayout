package net.z0kai.kkrefreshlayout_demo.pageview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import net.z0kai.kkrefreshlayout.IPageView;
import net.z0kai.kkrefreshlayout_demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingPageView extends LinearLayout implements IPageView {

    @BindView(R.id.animateIv)
    ImageView animateIv;

    public LoadingPageView(Context context) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.kk_rl_loading_page, this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(lp);
        setVisibility(GONE);
        ButterKnife.bind(this);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void show() {
        animateIv.setAlpha(1.0f);
        setVisibility(VISIBLE);
        Glide.with(getContext()).load(R.mipmap.page_loading).into(animateIv);
    }

    @Override
    public void hide() {
        animateIv.animate().alpha(0)
                .setDuration(1000)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

}
