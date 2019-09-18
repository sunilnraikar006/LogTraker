package com.sgs.mylibrary.orm.inflater.field;

import android.database.Cursor;

import com.sgs.mylibrary.orm.util.ReflectionUtil;

import java.lang.reflect.Field;


public class DefaultFieldInflater extends FieldInflater {

    public DefaultFieldInflater(Field field, Cursor cursor, Object object, Class<?> fieldType) {
        super(field, cursor, object, fieldType);
    }

    @Override
    public void inflate() {
        ReflectionUtil.setFieldValueFromCursor(cursor, field, object);
    }
}
