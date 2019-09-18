package com.sgs.mylibrary.orm.inflater.field;

import android.database.Cursor;
import android.util.Log;

import com.sgs.mylibrary.orm.SugarRecord;
import com.sgs.mylibrary.orm.helper.NamingHelper;

import java.lang.reflect.Field;

public class EntityFieldInflater extends FieldInflater {
    private static final String LOG_TAG = "EntityFieldInflater";

    public EntityFieldInflater(Field field, Cursor cursor, Object object, Class<?> fieldType) {
        super(field, cursor, object, fieldType);
    }

    @Override
    public void inflate() {
        try {
            long id = cursor.getLong(cursor.getColumnIndex(NamingHelper.toColumnName(field)));
            field.set(object, (id > 0) ? SugarRecord.findById(fieldType, id) : null);
        } catch (IllegalAccessException e) {
            Log.e(LOG_TAG, String.format("Error while inflating entity field %s", field), e);
        }
    }
}
