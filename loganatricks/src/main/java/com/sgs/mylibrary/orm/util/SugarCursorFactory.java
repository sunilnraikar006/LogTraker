package com.sgs.mylibrary.orm.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

/**
 *SugarCursorFactory supporting for SugarRecord implemting SQLiteDatabase.CursorFactory
 */
public class SugarCursorFactory implements SQLiteDatabase.CursorFactory {

    private boolean debugEnabled;

    public SugarCursorFactory() {
        this.debugEnabled = false;
    }

    public SugarCursorFactory(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    /**
     * method for creating new Cursor
     * @param sqLiteDatabase
     * @param sqLiteCursorDriver
     * @param editTable
     * @param sqLiteQuery
     * @return
     */
    @SuppressWarnings("deprecation")
    public Cursor newCursor(SQLiteDatabase sqLiteDatabase,
                            SQLiteCursorDriver sqLiteCursorDriver,
                            String editTable,
                            SQLiteQuery sqLiteQuery) {

        if (debugEnabled) {
            Log.d("SQL Log", sqLiteQuery.toString());
        }

        return new SQLiteCursor(sqLiteDatabase, sqLiteCursorDriver, editTable, sqLiteQuery);
    }

}
