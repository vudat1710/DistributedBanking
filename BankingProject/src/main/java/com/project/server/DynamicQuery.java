package com.project.server;

import java.util.ArrayList;
import java.util.List;

public class DynamicQuery {
    Director d = new Director();

    public class Select {
        private List<String> columns = new ArrayList<String>();

        private List<String> tables = new ArrayList<String>();

        private List<String> joins = new ArrayList<String>();

        private List<String> leftJoins = new ArrayList<String>();

        private List<String> wheres = new ArrayList<String>();

        private List<String> orderBys = new ArrayList<String>();

        private List<String> groupBys = new ArrayList<String>();

        private List<String> havings = new ArrayList<String>();

        public Select() {

        }

        public Select(String table) {
            tables.add(table);
        }

        public Select column(String name) {
            if (name != null) columns.add(name);
            return this;
        }

        public Select column(String name, boolean groupBy) {
            columns.add(name);
            if (groupBy) {
                groupBys.add(name);
            }
            return this;
        }

        public Select from(String table) {
            tables.add(table);
            return this;
        }

        public Select groupBy(String expr) {
            groupBys.add(expr);
            return this;
        }

        public Select having(String expr) {
            havings.add(expr);
            return this;
        }

        public Select join(String join) {
            joins.add(join);
            return this;
        }

        public Select leftJoin(String join) {
            leftJoins.add(join);
            return this;
        }

        public Select orderBy(String name) {
            orderBys.add(name);
            return this;
        }

        @Override
        public String toString() {

            StringBuilder sql = new StringBuilder("select ");

            if (columns.size() == 0) {
                sql.append("*");
            } else {
                d.appendList(sql, columns, "", ", ", "");
            }

            d.appendList(sql, tables, " from ", ", ", "");
            d.appendList(sql, joins, " join ", " join ", "");
            d.appendList(sql, leftJoins, " left join ", " left join ", "");
            d.appendList(sql, wheres, " where ", " and ", "");
            d.appendList(sql, groupBys, " group by ", ", ", "");
            d.appendList(sql, havings, " having ", " and ", "");
            d.appendList(sql, orderBys, " order by ", ", ", "");

            return sql.toString();
        }

        public Select where(String expr) {
            if (expr != null) wheres.add(expr);
            return this;
        }
    }

    public class Insert {
        private List<String> tables = new ArrayList<String>();
        private List<String> values = new ArrayList<String>();

        public Insert() {
        }

        public Insert(List<String> tables) {
            this.tables = tables;
        }

        public Insert table(String table) {
            tables.add(table);
            return this;
        }

        public Insert value(String value) {
            values.add(value);
            return this;
        }

        @Override
        public String toString() {
            StringBuilder sql = new StringBuilder("insert ");
            d.appendList(sql, tables, "into ", ", ", "");
            d.appendList(sql, values, " values (", ",", ")");

            return sql.toString();
        }
    }

    public class Update {
        private List<String> tables = new ArrayList<String>();
        private List<String> sets = new ArrayList<String>();
        private List<String> orderBys = new ArrayList<String>();
        private List<String> whereAnds = new ArrayList<String>();
        private List<String> whereOrs = new ArrayList<String>();

        public Update() {
        }

        public Update(List<String> tables) {
            this.tables = tables;
        }

        public Update table(String table) {
            tables.add(table);
            return this;
        }

        public Update set(String set) {
            sets.add(set);
            return this;
        }

        public Update orderBy(String orderBy) {
            orderBys.add(orderBy);
            return this;
        }

        public Update whereAnd(String whereAnd) {
            if (whereAnd != null) whereAnds.add(whereAnd);
            return this;
        }

        public Update whereOr(String whereOr) {
            if (whereOr != null) whereOrs.add(whereOr);
            return this;
        }

        @Override
        public String toString() {
            StringBuilder sql = new StringBuilder("update ");
            d.appendList(sql, tables, "", ", ", "");
            d.appendList(sql, sets, " set ", ", ", "");
            d.appendList(sql, whereAnds, " where ", " and ", "");
            d.appendList(sql, whereOrs, " where ", " or ", "");

            return sql.toString();
        }
    }

    public class Delete {
        private List<String> tables = new ArrayList<String>();
        private List<String> wheres = new ArrayList<String>();
        private List<String> orderBys = new ArrayList<String>();

        public Delete(List<String> tables) {
            this.tables = tables;
        }

        public Delete() {
        }

        public Delete table(String table) {
            tables.add(table);
            return this;
        }

        public Delete where(String where) {
            if (where != null) wheres.add(where);
            return this;
        }

        public Delete orderBy(String orderBy) {
            orderBys.add(orderBy);
            return this;
        }

        @Override
        public String toString() {
            StringBuilder sql = new StringBuilder("delete");
            d.appendList(sql, tables, " from ", ", ", "");
            d.appendList(sql, wheres, " where ", " and ", "");

            return sql.toString();
        }
    }
}
