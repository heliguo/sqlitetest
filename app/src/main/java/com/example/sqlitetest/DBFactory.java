package com.example.sqlitetest;

import android.content.Context;


public class DBFactory {
    public static CommonSqlHelper getConnection(Context c) {
        return new CommonSqlHelper(c);
    }
}
