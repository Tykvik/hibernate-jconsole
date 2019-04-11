package net.sf.hibernate.jconsole.formatters;

/**
 * Implements highlighting for concrete style
 *
 * @author Helloween
 * @version 1.0
 */
public class SimpleHightlighter extends AbstractHighlighter {
    private final TokenHighlighter tokenHighlighter;

    /**
     * ctor
     *
     * @param tokenHighlighter style
     */
    public SimpleHightlighter(TokenHighlighter tokenHighlighter) {
        this.tokenHighlighter = tokenHighlighter;
    }

    @Override
    protected void reset() {
    }

    @Override
    protected TokenHighlighter getHighlighterForToken(Token token, Token nextToken) {
        return tokenHighlighter;
    }
}
