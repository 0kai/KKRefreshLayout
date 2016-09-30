package net.z0kai.refreshlayout;

import android.content.Context;

import net.z0kai.refreshlayout.view.DefaultFooterView;
import net.z0kai.refreshlayout.view.DefaultHeaderView;
import net.z0kai.refreshlayout.view.IFooterView;
import net.z0kai.refreshlayout.view.IHeaderView;

/**
 * Created by Z_0Kai on 16/9/29.
 */

public class RefreshLayoutConfig {
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
