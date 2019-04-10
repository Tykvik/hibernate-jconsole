package net.sf.hibernate.jconsole.ui;

import net.sf.hibernate.jconsole.AbstractStatisticsContext;
import net.sf.hibernate.jconsole.stats.Names;
import net.sf.hibernate.jconsole.ui.widgets.AbstractRefreshableJTable;

import java.util.*;

/**
 * Implements a JTable containing some statistics attributes.
 *
 * @author Helloween
 * @version 1.0
 */
public class CommonStatisticTable extends AbstractRefreshableJTable<String> {
    private static final Column[] COLUMNS = {
        new Column("Attribute name", "Attribute name", Comparable.class),
        new Column("Attribute value", "Attribute value", Comparable.class)
    };

    private static final List<Names> ATTRIBUTES = Arrays.asList(
            Names.SessionOpenCount,
            Names.SessionCloseCount,
            Names.QueryExecutionMaxTime,
            Names.QueryExecutionMaxTimeQueryString,
            Names.PrepareStatementCount,
            Names.CloseStatementCount,
            Names.ConnectCount,
            Names.FlushCount);

    /**
     * {@inheritDoc}
     */
    @Override
    protected Map<String, String> toTableData(AbstractStatisticsContext context) {
        Map<String, String> values = new HashMap<String, String>();
        for (Names names : ATTRIBUTES) {
            values.put(names.name(), String.valueOf(context.getAttributes().get(names)));
        }
        return values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Column[] getColumns() {
        return COLUMNS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Vector toTableRow(String key, String dataElement) {
        Vector<Object> v = new Vector<Object>(COLUMNS.length);
        v.add(key);
        v.add(dataElement);
        return v;
    }
}

