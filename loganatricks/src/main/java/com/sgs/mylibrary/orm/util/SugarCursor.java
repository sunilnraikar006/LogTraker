package com.sgs.mylibrary.orm.util;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * SugarCursor class for supporting sugarRecord
 */
public class SugarCursor extends CursorWrapper {

    public SugarCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * method for getting the columnIndex
     * @param columnName
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
        try {
            return super.getColumnIndexOrThrow(columnName);
        } catch (IllegalArgumentException e) {
            if (columnName.equals("_id"))
                return super.getColumnIndexOrThrow("ID");
            else
                throw e;
        }
    }

    /**
     * method for getting the columnIndex
     * @param columnName
     * @return
     */
    @Override
    public int getColumnIndex(String columnName) {
        if (columnName.equals("_id"))
            columnName = "ID";
        return super.getColumnIndex(columnName);
    }
}
