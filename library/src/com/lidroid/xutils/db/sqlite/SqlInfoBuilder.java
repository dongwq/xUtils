/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lidroid.xutils.db.sqlite;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.table.*;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Build "insert", "replace",，"update", "delete" and "create" sql.
 */
public class SqlInfoBuilder {

    private SqlInfoBuilder() {
    }

    //*********************************************** insert sql ***********************************************

    public static SqlInfo buildInsertSqlInfo(DbUtils db, Object entity) throws DbException {

        List<KeyValue> keyValueList = entity2KeyValueList(db, entity);
        if (keyValueList.size() == 0) return null;

        SqlInfo result = new SqlInfo();
        StringBuffer sqlBuffer = new StringBuffer();

        sqlBuffer.append("INSERT INTO ");
        sqlBuffer.append(Table.get(entity.getClass()).getTableName());
        sqlBuffer.append(" (");
        for (KeyValue kv : keyValueList) {
            sqlBuffer.append(kv.getKey()).append(",");
            result.addBindArg(kv.getValue());
        }
        sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
        sqlBuffer.append(") VALUES (");

        int length = keyValueList.size();
        for (int i = 0; i < length; i++) {
            sqlBuffer.append("?,");
        }
        sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
        sqlBuffer.append(")");

        result.setSql(sqlBuffer.toString());

        return result;
    }

    //*********************************************** replace sql ***********************************************

    public static SqlInfo buildReplaceSqlInfo(DbUtils db, Object entity) throws DbException {

        List<KeyValue> keyValueList = entity2KeyValueList(db, entity);
        if (keyValueList.size() == 0) return null;

        SqlInfo result = new SqlInfo();
        StringBuffer sqlBuffer = new StringBuffer();

        sqlBuffer.append("REPLACE INTO ");
        sqlBuffer.append(Table.get(entity.getClass()).getTableName());
        sqlBuffer.append(" (");
        for (KeyValue kv : keyValueList) {
            sqlBuffer.append(kv.getKey()).append(",");
            result.addBindArg(kv.getValue());
        }
        sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
        sqlBuffer.append(") VALUES (");

        int length = keyValueList.size();
        for (int i = 0; i < length; i++) {
            sqlBuffer.append("?,");
        }
        sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
        sqlBuffer.append(")");

        result.setSql(sqlBuffer.toString());

        return result;
    }

    //*********************************************** delete sql ***********************************************

    private static String buildDeleteSqlByTableName(String tableName) {
        return "DELETE FROM " + tableName;
    }

    public static SqlInfo buildDeleteSqlInfo(Object entity) throws DbException {
        SqlInfo result = new SqlInfo();

        Table table = Table.get(entity.getClass());
        Id id = table.getId();
        Object idValue = id.getColumnValue(entity);

        if (idValue == null) {
            throw new DbException("this entity[" + entity.getClass() + "]'s id value is null");
        }
        StringBuilder sb = new StringBuilder(buildDeleteSqlByTableName(table.getTableName()));
        sb.append(" WHERE ").append(WhereBuilder.b(id.getColumnName(), "=", idValue));

        result.setSql(sb.toString());

        return result;
    }

    public static SqlInfo buildDeleteSqlInfo(Class<?> entityType, Object idValue) throws DbException {
        SqlInfo result = new SqlInfo();

        Table table = Table.get(entityType);
        Id id = table.getId();

        if (null == idValue) {
            throw new DbException("this entity[" + entityType + "]'s id value is null");
        }
        StringBuilder sb = new StringBuilder(buildDeleteSqlByTableName(table.getTableName()));
        sb.append(" WHERE ").append(WhereBuilder.b(id.getColumnName(), "=", idValue));

        result.setSql(sb.toString());

        return result;
    }

    public static SqlInfo buildDeleteSqlInfo(Class<?> entityType, WhereBuilder whereBuilder) throws DbException {
        Table table = Table.get(entityType);
        StringBuilder sb = new StringBuilder(buildDeleteSqlByTableName(table.getTableName()));

        if (whereBuilder != null && whereBuilder.getWhereItemSize() > 0) {
            sb.append(" WHERE ").append(whereBuilder.toString());
        }

        return new SqlInfo(sb.toString());
    }

    //*********************************************** update sql ***********************************************

    public static SqlInfo buildUpdateSqlInfo(DbUtils db, Object entity) throws DbException {

        List<KeyValue> keyValueList = entity2KeyValueList(db, entity);
        if (keyValueList.size() == 0) return null;

        Table table = Table.get(entity.getClass());
        Id id = table.getId();
        Object idValue = id.getColumnValue(entity);

        if (null == idValue) {
            throw new DbException("this entity[" + entity.getClass() + "]'s id value is null");
        }

        SqlInfo result = new SqlInfo();
        StringBuffer sqlBuffer = new StringBuffer("UPDATE ");
        sqlBuffer.append(table.getTableName());
        sqlBuffer.append(" SET ");
        for (KeyValue kv : keyValueList) {
            sqlBuffer.append(kv.getKey()).append("=?,");
            result.addBindArg(kv.getValue());
        }
        sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
        sqlBuffer.append(" WHERE ").append(WhereBuilder.b(id.getColumnName(), "=", idValue));

        result.setSql(sqlBuffer.toString());
        return result;
    }

    public static SqlInfo buildUpdateSqlInfo(DbUtils db, Object entity, WhereBuilder whereBuilder) throws DbException {

        List<KeyValue> keyValueList = entity2KeyValueList(db, entity);
        if (keyValueList.size() == 0) return null;

        Table table = Table.get(entity.getClass());

        SqlInfo result = new SqlInfo();
        StringBuffer sqlBuffer = new StringBuffer("UPDATE ");
        sqlBuffer.append(table.getTableName());
        sqlBuffer.append(" SET ");
        for (KeyValue kv : keyValueList) {
            sqlBuffer.append(kv.getKey()).append("=?,");
            result.addBindArg(kv.getValue());
        }
        sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
        if (whereBuilder != null && whereBuilder.getWhereItemSize() > 0) {
            sqlBuffer.append(" WHERE ").append(whereBuilder.toString());
        }

        result.setSql(sqlBuffer.toString());
        return result;
    }

    //*********************************************** others ***********************************************

    public static SqlInfo buildCreateTableSqlInfo(Class<?> entityType) throws DbException {
        Table table = Table.get(entityType);

        Id id = table.getId();
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("CREATE TABLE IF NOT EXISTS ");
        sqlBuffer.append(table.getTableName());
        sqlBuffer.append(" ( ");

        if (id.isAutoIncrement()) {
            sqlBuffer.append("\"").append(id.getColumnName()).append("\"  ").append("INTEGER PRIMARY KEY AUTOINCREMENT,");
        } else {
            sqlBuffer.append("\"").append(id.getColumnName()).append("\"  ").append(id.getColumnDbType()).append(" PRIMARY KEY,");
        }

        Collection<Column> columns = table.columnMap.values();
        for (Column column : columns) {
            if (column instanceof Finder) {
                continue;
            }
            sqlBuffer.append("\"").append(column.getColumnName()).append("\"  ");
            sqlBuffer.append(column.getColumnDbType());
            if (ColumnUtils.isUnique(column.getColumnField())) {
                sqlBuffer.append(" UNIQUE");
            }
            if (ColumnUtils.isNotNull(column.getColumnField())) {
                sqlBuffer.append(" NOT NULL");
            }
            String check = ColumnUtils.getCheck(column.getColumnField());
            if (check != null) {
                sqlBuffer.append(" CHECK(").append(check).append(")");
            }
            sqlBuffer.append(",");
        }

        sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
        sqlBuffer.append(" )");
        return new SqlInfo(sqlBuffer.toString());
    }

    private static KeyValue column2KeyValue(Object entity, Column column) {
        KeyValue kv = null;
        String key = column.getColumnName();
        Object value = column.getColumnValue(entity);
        value = value == null ? column.getDefaultValue() : value;
        if (key != null && value != null) {
            kv = new KeyValue(key, value);
        }
        return kv;
    }

    public static List<KeyValue> entity2KeyValueList(DbUtils db, Object entity) {

        List<KeyValue> keyValueList = new ArrayList<KeyValue>();

        Table table = Table.get(entity.getClass());
        Id id = table.getId();
        Object idValue = TableUtils.getIdValue(entity);

        if (id != null && idValue != null) {
            KeyValue kv = new KeyValue(id.getColumnName(), idValue);
            keyValueList.add(kv);
        }

        Collection<Column> columns = table.columnMap.values();
        for (Column column : columns) {
            if (column instanceof Finder) {
                ((Finder) column).db = db;
            } else if (column instanceof Foreign) {
                ((Foreign) column).db = db;
            }
            KeyValue kv = column2KeyValue(entity, column);
            if (kv != null) {
                keyValueList.add(kv);
            }
        }

        return keyValueList;
    }
}
