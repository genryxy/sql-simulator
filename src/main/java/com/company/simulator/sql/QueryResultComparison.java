package com.company.simulator.sql;

import java.util.List;
import java.util.Map;

public class QueryResultComparison {

    private final List<Map<String, Object>> first;

    public QueryResultComparison(final List<Map<String, Object>> first) {
        this.first = first;
    }

    public boolean compareWith(final List<Map<String, Object>> second) {
        final boolean res;
        if (first == null || second == null) {
            res = false;
        } else if (first.size() != second.size()) {
            res = false;
        } else if (first.isEmpty()) {
            res = true;
        } else if (first.get(0).size() != second.get(0).size()) {
            res = false;
        } else {
            res = first.equals(second);
        }
        return res;
    }

    /**
     * Compare ignoring column names.
     * @param second Second result of query
     * @return True if received values are equals, false otherwise.
     *  It is possible when both columns have the same values. In this case,
     *  the results are not distinguishable.
     */
    private boolean compareAllRecords(final List<Map<String, Object>> second) {
        for (int i = 0; i < second.size(); i++) {
            if (!first.get(i).equals(second.get(i))) {
                return false;
            }
        }
        return true;
    }
}
