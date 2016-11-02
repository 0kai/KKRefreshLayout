package net.z0kai.kkrefreshlayout_demo;

import android.content.Context;

import net.z0kai.kkrefreshlayout.FooterViewProvider;
import net.z0kai.kkrefreshlayout.HeaderViewProvider;
import net.z0kai.kkrefreshlayout.IFooterView;
import net.z0kai.kkrefreshlayout.IHeaderView;
import net.z0kai.kkrefreshlayout_demo.horizontal.ArrowFooterView;
import net.z0kai.kkrefreshlayout_demo.headerview.EmptyHeaderView;

/**
 * Created by Z_0Kai on 16/9/29.
 */

public class RefreshLayoutHConfig {
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
                    return new EmptyHeaderView(context);
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
                    return new ArrowFooterView(context);
                }
            };
        }
        return footerViewProvider;
    }


}
