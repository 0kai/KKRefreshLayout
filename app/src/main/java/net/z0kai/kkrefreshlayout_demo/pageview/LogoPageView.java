package net.z0kai.kkrefreshlayout_demo.pageview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.z0kai.kkrefreshlayout.IPageView;
import net.z0kai.kkrefreshlayout_demo.R;

/**
 * Created by Z_0Kai on 17/3/17.
 */

public class LogoPageView extends LinearLayout implements IPageView {

    ImageView logo;

    public LogoPageView(Context context) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.kk_rl_logo_page, this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(lp);

        logo = (ImageView) findViewById(R.id.logo);
        setVisibility(GONE);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void show() {
        setVisibility(VISIBLE);
        ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 2.0f, 1.0f).setDuration(1000).start();
    }

    @Override
    public void hide() {
        setVisibility(GONE);
    }
}
