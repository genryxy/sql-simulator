package com.company.simulator.processing;

import java.util.Locale;
import java.util.regex.Pattern;

public class QueryProcess {
    private static final Pattern PTRN = Pattern.compile(
        ".*drop[\\r\\n\\s]+database[\\r\\n\\s]+simulator.*"
    );

    private final String query;

    public QueryProcess(final String query) {
        this.query = processedQuery(query);
    }

    public boolean malformed() {
        return PTRN.matcher(query).matches();
    }

    private String processedQuery(final String query) {
        if (query == null) {
            return "";
        }
        return query.toLowerCase(Locale.ROOT);
    }
}
