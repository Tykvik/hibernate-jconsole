package net.sf.hibernate.jconsole.ui;

/**
 * Implements the common statistic page.
 *
 * @author Helloween
 * @version 1.0
 */
public class CommonStatisticTab extends AbstractTableTab {
    public static final String NAME = "Common";

    /**
     * ctor
     */
    public CommonStatisticTab() {
        init(new CommonStatisticDetails(new CommonStatisticTable()));
    }
}
