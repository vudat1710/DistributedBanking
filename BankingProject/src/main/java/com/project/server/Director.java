package com.project.server;

import java.util.List;

public class Director {
    public String equals(String column_name, String b) {
        if (b == null) {
            return null;
        } else
            return "" + column_name + "= " + b + "";
    }

    public String like(String column_name, String b) {
        return "" + column_name + " like '%" + b + "%'";
    }

    public void appendList(StringBuilder sql, List<String> list, String init, String sep, String end) {
        boolean first = true;
        for (String s : list) {
            if (first) {
                sql.append(init);
            } else {
                sql.append(sep);
            }
            sql.append(s);
            first = false;
        }
        sql.append(end);
    }
}

