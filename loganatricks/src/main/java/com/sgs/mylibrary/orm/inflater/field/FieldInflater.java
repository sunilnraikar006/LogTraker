package com.sgs.mylibrary.orm.inflater.field;

import android.database.Cursor;

import java.lang.reflect.Field;

public abstract class FieldInflater {
    protected Field field;
    protected Cursor cursor;
    protected Object object;
    protected Class<?> fieldType;

    public FieldInflater(Field field, Cursor cursor, Object object, Class<?> fieldType) {
        this.field = field;
        this.cursor = cursor;
        this.object = object;
        this.fieldType = fieldType;
    }

    public abstract void inflate();
}
