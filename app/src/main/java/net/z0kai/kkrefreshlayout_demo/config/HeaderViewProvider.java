package net.z0kai.kkrefreshlayout_demo.config;

import android.content.Context;

import net.z0kai.kkrefreshlayout.IHeaderView;

/**
 * Created by Z_0Kai on 16/9/29.
 */

public interface HeaderViewProvider {
    IHeaderView get(Context context);
}
