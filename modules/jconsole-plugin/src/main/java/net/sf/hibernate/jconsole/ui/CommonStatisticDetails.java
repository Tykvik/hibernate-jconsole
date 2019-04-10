package net.sf.hibernate.jconsole.ui;

import net.sf.hibernate.jconsole.AbstractStatisticsContext;
import net.sf.hibernate.jconsole.util.DataTable;

/**
 * Implements the details panel below some statistic attributes.
 *
 * @author Helloween
 * @version 1.0
 */
public class CommonStatisticDetails extends AbstractChartViewDetails<String> {

    /**
     * Creates an instance of CommonStatisticDetails for the given table.
     *
     * @param table the table to create the details for.
     */
    public CommonStatisticDetails(CommonStatisticTable table) {
        super(table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTable getDataTableFor(AbstractStatisticsContext context, Object selection) {
        return null;
    }
}
