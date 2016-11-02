package net.z0kai.kkrefreshlayout_demo;

import net.z0kai.kkrefreshlayout_demo.horizontal.ArrowFooterView;
import net.z0kai.kkrefreshlayout_demo.headerview.EmptyHeaderView;
import net.z0kai.kkrefreshlayout_demo.headerview.vertical.BuDuoHeaderView;
import net.z0kai.kkrefreshlayout.vertical.DefaultFooterView;
import net.z0kai.kkrefreshlayout.vertical.DefaultHeaderView;

/**
 * Created by Z_0Kai on 16/10/31.
 */

public class AppConfigs {
    public final static Class[] HEADER_VIEW_CLASS_LIST = {
            DefaultHeaderView.class,
            EmptyHeaderView.class,
            BuDuoHeaderView.class,
    };

    public final static Class[] FOOTER_VIEW_CLASS_LIST = {
            DefaultFooterView.class,
            ArrowFooterView.class,
    };
}
