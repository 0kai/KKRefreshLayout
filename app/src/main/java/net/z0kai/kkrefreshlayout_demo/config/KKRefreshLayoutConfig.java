package net.z0kai.kkrefreshlayout_demo.config;

import android.content.Context;

import net.z0kai.kkrefreshlayout.IFooterView;
import net.z0kai.kkrefreshlayout.IHeaderView;
import net.z0kai.kkrefreshlayout.vertical.DefaultFooterView;
import net.z0kai.kkrefreshlayout.vertical.DefaultHeaderView;

/**
 * Created by Z_0Kai on 16/9/29.
 */

public class KKRefreshLayoutConfig {
    private static HeaderViewProvider headerViewProvider;
    private static FooterViewProvider footerViewProvider;

    public static void setHeaderViewProvider(HeaderViewProvider provider) {
        headerViewProvider = provider;
    }

    public static HeaderViewProvider getHeaderViewProvider() {
        if (headerViewProvider == null) {
            headerViewProvider = new HeaderViewProvider() {
                @Override
                public IHeaderView get(Context context) {
                    return new DefaultHeaderView(context);
                }
            };
        }
        return headerViewProvider;
    }

    public static void setFooterViewProvider(FooterViewProvider provider) {
        footerViewProvider = provider;
    }

    public static FooterViewProvider getFooterViewProvider() {
        if (footerViewProvider == null) {
            footerViewProvider = new FooterViewProvider() {
                @Override
                public IFooterView get(Context context) {
                    return new DefaultFooterView(context);
                }
            };
        }
        return footerViewProvider;
    }


}
