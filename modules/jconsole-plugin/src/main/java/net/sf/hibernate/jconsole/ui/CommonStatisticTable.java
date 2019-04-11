package net.sf.hibernate.jconsole.ui;

import net.sf.hibernate.jconsole.AbstractStatisticsContext;
import net.sf.hibernate.jconsole.formatters.AbstractHighlighter;
import net.sf.hibernate.jconsole.formatters.QueryHighlighter;
import net.sf.hibernate.jconsole.formatters.SimpleHightlighter;
import net.sf.hibernate.jconsole.stats.Names;
import net.sf.hibernate.jconsole.ui.widgets.AbstractRefreshableJTable;

import java.util.*;

import static net.sf.hibernate.jconsole.formatters.AbstractHighlighter.Style.NUMBER;

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

    private static final Map<Names, AbstractHighlighter> ATTRIBUTES = new LinkedHashMap<Names, AbstractHighlighter>();

    static {
        ATTRIBUTES.put(Names.SessionOpenCount, new SimpleHightlighter(NUMBER));
        ATTRIBUTES.put(Names.SessionCloseCount, new SimpleHightlighter(NUMBER));
        ATTRIBUTES.put(Names.QueryExecutionMaxTime, new SimpleHightlighter(NUMBER));
        ATTRIBUTES.put(Names.QueryExecutionMaxTimeQueryString, new QueryHighlighter());
        ATTRIBUTES.put(Names.PrepareStatementCount, new SimpleHightlighter(NUMBER));
        ATTRIBUTES.put(Names.CloseStatementCount, new SimpleHightlighter(NUMBER));
        ATTRIBUTES.put(Names.ConnectCount, new SimpleHightlighter(NUMBER));
        ATTRIBUTES.put(Names.FlushCount, new SimpleHightlighter(NUMBER));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Map<String, String> toTableData(AbstractStatisticsContext context) {
        Map<String, String> values = new HashMap<String, String>();
        SimpleHightlighter highlighter = new SimpleHightlighter(AbstractHighlighter.Style.NAME);

        for (Map.Entry<Names, AbstractHighlighter> entry : ATTRIBUTES.entrySet()) {
            String value = String.valueOf(context.getAttributes().get(entry.getKey()));
            values.put(highlighter.highlight(entry.getKey().name()),
                    entry.getValue().highlight(value == null ? "" : value));
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

