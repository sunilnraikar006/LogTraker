package com.sgs.mylibrary.orm.inflater;

import android.database.Cursor;

import com.sgs.mylibrary.orm.SugarRecord;
import com.sgs.mylibrary.orm.inflater.field.DefaultFieldInflater;
import com.sgs.mylibrary.orm.inflater.field.EntityFieldInflater;
import com.sgs.mylibrary.orm.inflater.field.FieldInflater;
import com.sgs.mylibrary.orm.inflater.field.ListFieldInflater;
import com.sgs.mylibrary.orm.inflater.field.RelationEntityFieldInflater;
import com.sgs.mylibrary.orm.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


public class EntityInflater {
    private Cursor cursor;
    private Object object;
    private Object relationObject;
    private String relationFieldName;
    private Map<Object, Long> entitiesMap;

    public EntityInflater withCursor(Cursor cursor) {
        this.cursor = cursor;
        return this;
    }

    public EntityInflater withObject(Object object) {
        this.object = object;
        return this;
    }

    public EntityInflater withRelationObject(Object relationObject) {
        this.relationObject = relationObject;
        return this;
    }

    public EntityInflater withRelationFieldName(String relationFieldName) {
        this.relationFieldName = relationFieldName;
        return this;
    }

    public EntityInflater withEntitiesMap(Map<Object, Long> entitiesMap) {
        this.entitiesMap = entitiesMap;
        return this;
    }

    public void inflate() {
        List<Field> columns = ReflectionUtil.getTableFields(object.getClass());
        Long objectId = cursor.getLong(cursor.getColumnIndex(("ID")));
        if (!entitiesMap.containsKey(object)) {
            entitiesMap.put(object, objectId);
        }

        FieldInflater fieldInflater;

        for (Field field : columns) {
            field.setAccessible(true);
            Class<?> fieldType = field.getType();

            if (SugarRecord.isSugarEntity(fieldType)) {
                if (field.getName().equals(relationFieldName)) {
                    fieldInflater = new RelationEntityFieldInflater(field, cursor, object, fieldType, relationObject);
                } else {
                    fieldInflater = new EntityFieldInflater(field, cursor, object, fieldType);
                }
            } else if (fieldType.equals(List.class)) {
                fieldInflater = new ListFieldInflater(field, cursor, object, fieldType);
            } else {
                fieldInflater = new DefaultFieldInflater(field, cursor, object, fieldType);
            }

            fieldInflater.inflate();
        }
    }
}
