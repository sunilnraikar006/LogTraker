package com.sgs.mylibrary.orm;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import com.sgs.mylibrary.orm.annotation.Table;
import com.sgs.mylibrary.orm.annotation.Unique;
import com.sgs.mylibrary.orm.helper.ManifestHelper;
import com.sgs.mylibrary.orm.helper.NamingHelper;
import com.sgs.mylibrary.orm.inflater.EntityInflater;
import com.sgs.mylibrary.orm.util.QueryBuilder;
import com.sgs.mylibrary.orm.util.ReflectionUtil;
import com.sgs.mylibrary.orm.util.SugarCursor;
import com.sgs.mylibrary.ormmodel.LAErrorVideoAsset;
import com.sgs.mylibrary.ormmodel.LASession;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.sgs.mylibrary.orm.SugarContext.getSugarContext;


/**
 *  SugarRecord is orm class
 */
public class SugarRecord {
    public static final String SUGAR = "Sugar";
    private static final String TAG = SugarRecord.class.getSimpleName();

    private Long id = null;

    /**
     * method will return object of sugarRecord
     * @return
     */
    private static SQLiteDatabase getSugarDataBase ( ) {
        return getSugarContext().getSugarDb().getDB();
    }

    private static SQLiteDatabase getReadleDb(){
        return getSugarContext().getSugarDb().getReadableDatabase();
    }

    /**
     * method will delete all the records based on the where condition
     * @param type
     * @param <T>
     * @return
     */
    public static <T> int deleteAll ( Class<T> type ) {
        return deleteAll(type, null);
    }

    /**
     * method will delete all the records
     * @param type
     * @param whereClause
     * @param whereArgs
     * @param <T>
     * @return
     */
    public static <T> int deleteAll ( Class<T> type, String whereClause, String... whereArgs ) {
        return getSugarDataBase().delete(NamingHelper.toTableName(type), whereClause, whereArgs);
    }

    /**
     * method will return cursor
     * @param type
     * @param whereClause
     * @param whereArgs
     * @param groupBy
     * @param orderBy
     * @param limit
     * @param <T>
     * @return
     */
    public static <T> Cursor getCursor ( Class<T> type, String whereClause, String[] whereArgs, String groupBy, String orderBy, String limit ) {
        Cursor raw = getSugarDataBase().query(NamingHelper.toTableName(type), null, whereClause, whereArgs,
                groupBy, null, orderBy, limit);
        return new SugarCursor(raw);
    }

    /**
     * method will save the transcation object
     * @param objects
     * @param <T>
     */
    @SuppressWarnings("deprecation")
    public static <T> void saveInTx ( T... objects ) {
        saveInTx(Arrays.asList(objects));
    }

    /**
     * method will save the collection data
     * @param objects
     * @param <T>
     */
    @SuppressWarnings("deprecation")
    public static <T> void saveInTx ( Collection<T> objects ) {
        SQLiteDatabase sqLiteDatabase = getSugarDataBase();
        try {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.setLockingEnabled(false);
            for (T object : objects) {
                save(object);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            if (ManifestHelper.isDebugEnabled()) {
                Log.i(SUGAR, "Error in saving in transaction " + e.getMessage());
            }
        } finally {
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.setLockingEnabled(true);
        }
    }

    /**
     * method for update db
     * @param objects
     * @param <T>
     */
    @SuppressWarnings("deprecation")
    public static <T> void updateInTx ( T... objects ) {
        updateInTx(Arrays.asList(objects));
    }

    /**
     * method for updating db
     * @param objects
     * @param <T>
     */
    @SuppressWarnings("deprecation")
    public static <T> void updateInTx ( Collection<T> objects ) {
        SQLiteDatabase sqLiteDatabase = getSugarDataBase();
        try {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.setLockingEnabled(false);
            for (T object : objects) {
                update(object);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            if (ManifestHelper.isDebugEnabled()) {
                Log.i(SUGAR, "Error in saving in transaction " + e.getMessage());
            }
        } finally {
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.setLockingEnabled(true);
        }
    }

    /**
     * method for deleting db records
     * @param objects
     * @param <T>
     * @return
     */
    @SuppressWarnings("deprecation")
    public static <T> int deleteInTx ( T... objects ) {
        return deleteInTx(Arrays.asList(objects));
    }

    @SuppressWarnings("deprecation")
    public static <T> int deleteInTx ( Collection<T> objects ) {
        SQLiteDatabase sqLiteDatabase = getSugarDataBase();
        int deletedRows = 0;
        try {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.setLockingEnabled(false);
            for (T object : objects) {
                if (delete(object)) {
                    ++deletedRows;
                }
            }
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            deletedRows = 0;
            if (ManifestHelper.isDebugEnabled()) {
                Log.i(SUGAR, "Error in deleting in transaction " + e.getMessage());
            }
        } finally {
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.setLockingEnabled(true);
        }
        return deletedRows;
    }

    /**
     * method for getting the list of objects
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> listAll ( Class<T> type ) {
        return find(type, null, null, null, null, null);
    }

    /**
     * method for getting the list of objts
     * @param type
     * @param orderBy
     * @param <T>
     * @return
     */
    public static <T> List<T> listAll ( Class<T> type, String orderBy ) {
        return find(type, null, null, null, orderBy, null);
    }

    /**
     * method for finding object using id
     * @param type
     * @param id
     * @param <T>
     * @return
     */
    public static <T> T findById ( Class<T> type, Long id ) {
        List<T> list = find(type, "id=?", new String[]{String.valueOf(id)}, null, null, "1");
        if (list.isEmpty()) return null;
        return list.get(0);
    }

    /**
     * method to object using id
     * @param type
     * @param id
     * @param <T>
     * @return
     */
    public static <T> T findById ( Class<T> type, Integer id ) {
        return findById(type, Long.valueOf(id));
    }

    /**
     * method to find object using below parameters
     * @param type
     * @param ids
     * @param <T>
     * @return
     */
    public static <T> List<T> findById ( Class<T> type, String... ids ) {
        String whereClause = "id IN (" + QueryBuilder.generatePlaceholders(ids.length) + ")";
        return find(type, whereClause, ids);
    }

    /**
     * method to find the sessionId using below parameters
     * @param type
     * @param ids
     * @param <T>
     * @return
     */
    public static <T> List<T> findSessionId ( Class<T> type, String... ids ) {
        String whereClause = "id IN (" + QueryBuilder.generatePlaceholders(ids.length) + ")";
        return findLaSession(type, whereClause, ids);
    }


    /**
     * method to sort by query
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T first ( Class<T> type ) {
        List<T> list = findWithQuery(type,
                "SELECT * FROM " + NamingHelper.toTableName(type) + " ORDER BY ID ASC LIMIT 1");
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * method to find the last record usng limit keyword
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T last ( Class<T> type ) {
        List<T> list = findWithQuery(type,
                "SELECT * FROM " + NamingHelper.toTableName(type) + " ORDER BY ID DESC LIMIT 1");
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * method to find the last 3 rows
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> lastRows ( Class<T> type ) {
        List<T> list = findWithQuery(type,
                "SELECT * FROM " + NamingHelper.toTableName(type) + " ORDER BY ID DESC LIMIT 3");
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }

    /**
     * method to find all the records from the table
     * @param type
     * @param <T>
     * @return
     */
    public static <T> Iterator<T> findAll ( Class<T> type ) {
        return findAsIterator(type, null, null, null, null, null);
    }

    /**
     * method is used to findAsIterator
     * @param type
     * @param whereClause
     * @param whereArgs
     * @param <T>
     * @return
     */
    public static <T> Iterator<T> findAsIterator ( Class<T> type, String whereClause, String... whereArgs ) {
        return findAsIterator(type, whereClause, whereArgs, null, null, null);
    }

    /** method is used  for finding the query iterator
     * @param type
     * @param query
     * @param arguments
     * @param <T>
     * @return
     */
    public static <T> Iterator<T> findWithQueryAsIterator ( Class<T> type, String query, String... arguments ) {
        Cursor cursor = getSugarDataBase().rawQuery(query, arguments);
        return new CursorIterator<>(type, cursor);
    }

    /**
     * @param type
     * @param whereClause
     * @param whereArgs
     * @param groupBy
     * @param orderBy
     * @param limit
     * @param <T>
     * @return
     */
    public static <T> Iterator<T> findAsIterator ( Class<T> type, String whereClause, String[] whereArgs, String groupBy, String orderBy, String limit ) {
        Cursor cursor = getSugarDataBase().query(NamingHelper.toTableName(type), null, whereClause, whereArgs,
                groupBy, null, orderBy, limit);
        return new CursorIterator<>(type, cursor);
    }

    /**
     * method is used to find recofd
     * @param type
     * @param whereClause
     * @param whereArgs
     * @param <T>
     * @return
     */
    public static <T> List<T> find ( Class<T> type, String whereClause, String... whereArgs ) {
        return find(type, whereClause, whereArgs, null, null, null);
    }

    /**
     * method is used  for finding LAsession
     * @param type
     * @param whereClause
     * @param whereArgs
     * @param <T>
     * @return
     */
    public static <T> List<T> findLaSession ( Class<T> type, String whereClause, String... whereArgs ) {
        return findLa(type, whereClause, whereArgs, null, null, null);
    }


    /**
     * method is used  for finding the limit
     * @param type
     * @param whereClause
     * @param limit
     * @param orderBy
     * @param whereArgs
     * @param <T>
     * @return
     */
    public static <T> List<T> findLimit ( Class<T> type, String whereClause, String limit, String orderBy, String... whereArgs ) {
        return find(type, whereClause, whereArgs, null, orderBy, limit);
    }

    /**
     * method is used to find the order
     * @param type
     * @param whereClause
     * @param whereArgs
     * @param orderBy
     * @param limit
     * @param <T>
     * @return
     */
    public static <T> List<T> findByOrder ( Class<T> type, String whereClause, String[] whereArgs, String orderBy, String limit ) {
        return find(type, whereClause, whereArgs, null, orderBy, limit);
    }

    /**
     * method is for finding the query
     * @param type
     * @param query
     * @param arguments
     * @param <T>
     * @return
     */
    public static <T> List<T> findWithQuery ( Class<T> type, String query, String... arguments ) {
        Cursor cursor = getSugarDataBase().rawQuery(query, arguments);

        return getEntitiesFromCursor(cursor, type);
    }

    /**
     * method is for executing the query
     * @param query
     * @param arguments
     */
    public static void executeQuery ( String query, String... arguments ) {
        getSugarDataBase().execSQL(query, arguments);
    }

    /**
     * method is used for finding the LaSession
     * @param type
     * @param whereClause
     * @param whereArgs
     * @param groupBy
     * @param orderBy
     * @param limit
     * @param <T>
     * @return
     */
    public static <T> List<T> findLa ( Class<T> type, String whereClause, String[] whereArgs, String groupBy, String orderBy, String limit ) {

        String args[];
        args = (whereArgs == null) ? null : replaceArgs(whereArgs);

        Cursor cursor = getSugarDataBase().query(NamingHelper.toTableName(type), null, whereClause, args,
                groupBy, null, orderBy, limit);

        return getEntitiesFromCursor(cursor, type);
    }


    /**
     * method is used for generating query
     * @param type
     * @param whereClause
     * @param whereArgs
     * @param groupBy
     * @param orderBy
     * @param limit
     * @param <T>
     * @return
     */
    public static <T> List<T> find ( Class<T> type, String whereClause, String[] whereArgs, String groupBy, String orderBy, String limit ) {

        String args[];
        args = (whereArgs == null) ? null : replaceArgs(whereArgs);

        Cursor cursor = getSugarDataBase().query(NamingHelper.toTableName(type), null, whereClause, args,
                groupBy, null, orderBy, limit);

        return getEntitiesFromCursor(cursor, type);
    }

    /**
     * method is used to find the relationship mapping
     * @param type
     * @param relationFieldName
     * @param relationObject
     * @param relationObjectId
     * @param <T>
     * @return
     */
    public static <T> List<T> findOneToMany ( Class<T> type, String relationFieldName, Object relationObject, Long relationObjectId ) {
        String args[] = {String.valueOf(relationObjectId)};
        String whereClause = NamingHelper.toSQLNameDefault(relationFieldName) + " = ?";

        Cursor cursor = getSugarDataBase().query(NamingHelper.toTableName(type), null, whereClause, args,
                null, null, null, null);

        return getEntitiesFromCursor(cursor, type, relationFieldName, relationObject);
    }

    /**
     * method is used for getting the list of Entity from the cursor
     * @param cursor
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> getEntitiesFromCursor ( Cursor cursor, Class<T> type ) {
        return getEntitiesFromCursor(cursor, type, null, null);
    }

    /**
     * method is used for getting the list of Entity from the cursor
     * @param cursor
     * @param type
     * @param relationFieldName
     * @param relationObject
     * @param <T>
     * @return
     */
    public static <T> List<T> getEntitiesFromCursor ( Cursor cursor, Class<T> type, String relationFieldName, Object relationObject ) {
        T entity;
        List<T> result = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                entity = type.getDeclaredConstructor().newInstance();
                new EntityInflater()
                        .withCursor(cursor)
                        .withObject(entity)
                        .withEntitiesMap(getSugarContext().getEntitiesMap())
                        .withRelationFieldName(relationFieldName)
                        .withRelationObject(relationObject)
                        .inflate();
                result.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return result;
    }

 /* public boolean update(String errorId, String sessionId) {
        SQLiteDatabase db = getCursor()
  }*/

    /**
     * method is used for calculating the total count
     * @param type
     * @param <T>
     * @return
     */
    public static <T> long count ( Class<T> type ) {
        return count(type, null, null, null, null, null);
    }

    /**
     * method is used for calculating the total count
     * @param type
     * @param whereClause
     * @param whereArgs
     * @param <T>
     * @return
     */
    public static <T> long count ( Class<T> type, String whereClause, String... whereArgs ) {
        return count(type, whereClause, whereArgs, null, null, null);
    }

    /**
     * method is for calcualting the total count of particular table
     * @param type
     * @param whereClause
     * @param whereArgs
     * @param groupBy
     * @param orderBy
     * @param limit
     * @param <T>
     * @return
     */
    public static <T> long count ( Class<T> type, String whereClause, String[] whereArgs, String groupBy, String orderBy, String limit ) {
        long result = -1;
        String filter = (!TextUtils.isEmpty(whereClause)) ? " where " + whereClause : "";
        SQLiteStatement sqliteStatement;
        try {
            sqliteStatement = getSugarDataBase().compileStatement("SELECT count(*) FROM " + NamingHelper.toTableName(type) + filter);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return result;
        }

        if (whereArgs != null) {
            for (int i = whereArgs.length; i != 0; i--) {
                sqliteStatement.bindString(i, whereArgs[i - 1]);
            }
        }

        try {
            result = sqliteStatement.simpleQueryForLong();
        } finally {
            sqliteStatement.close();
        }

        return result;
    }

    /**
     * method is used for calculating the aggregation values
     * @param type
     * @param field
     * @param <T>
     * @return
     */
    public static <T> long sum ( Class<T> type, String field ) {
        return sum(type, field, null, null);
    }

    /**
     * method is for calculating the aggregation values
     * @param type
     * @param field
     * @param whereClause
     * @param whereArgs
     * @param <T>
     * @return
     */
    public static <T> long sum ( Class<T> type, String field, String whereClause, String... whereArgs ) {
        long result = -1;
        String filter = (!TextUtils.isEmpty(whereClause)) ? " where " + whereClause : "";
        SQLiteStatement sqLiteStatement;
        try {
            sqLiteStatement = getSugarDataBase().compileStatement("SELECT sum(" + field + ") FROM " + NamingHelper.toTableName(type) + filter);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return result;
        }

        if (whereArgs != null) {
            for (int i = whereArgs.length; i != 0; i--) {
                sqLiteStatement.bindString(i, whereArgs[i - 1]);
            }
        }

        try {
            result = sqLiteStatement.simpleQueryForLong();
        } finally {
            sqLiteStatement.close();
        }

        return result;
    }

    public static long save ( Object object ) {
        return save(getSugarDataBase(), object);
    }

    /**
     * method is used for saving the data
     * @param db
     * @param object
     * @return
     */
    static long save ( SQLiteDatabase db, Object object ) {
        try {
            Log.d(TAG, "save: " + object.toString());
            Map<Object, Long> entitiesMap = getSugarContext().getEntitiesMap();
            List<Field> columns = ReflectionUtil.getTableFields(object.getClass());
            ContentValues values = new ContentValues(columns.size());
            Field idField = null;
            for (Field column : columns) {
                ReflectionUtil.addFieldValueToColumn(values, column, object, entitiesMap);
                if (column.getName().equals("id")) {
                    idField = column;
                }
            }

            boolean isSugarEntity = isSugarEntity(object.getClass());
            if (isSugarEntity && entitiesMap.containsKey(object)) {
                values.put("id", entitiesMap.get(object));
            }

            long id = db.insertWithOnConflict(NamingHelper.toTableName(object.getClass()), null, values,
                    SQLiteDatabase.CONFLICT_REPLACE);

            if (object.getClass().isAnnotationPresent(Table.class)) {
                if (idField != null) {
                    idField.setAccessible(true);
                    try {
                        idField.set(object, id);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else {
                    entitiesMap.put(object, id);
                }
            } else if (SugarRecord.class.isAssignableFrom(object.getClass())) {
                ((SugarRecord) object).setId(id);
            }

            if (ManifestHelper.isDebugEnabled()) {
                Log.i(SUGAR, object.getClass().getSimpleName() + " saved : " + id);
            }

            return id;
        } catch (Exception e) {
            Log.e(TAG, "save: " + e.getMessage());
        }
        return -1;
    }

    /**
     * method is for updating ErrorVideoAsset using below parameters
     * @param errID
     * @param object
     * @return
     */
    public static boolean updEvaErrId ( String errID, Object object) {
        SQLiteDatabase db = getSugarDataBase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("IS_CREATED", 1);


        int update = db.update(NamingHelper.toTableName(object.getClass()), contentValues, "IDENTIFIER =? ", new String[]{errID});

        Log.d(TAG, "updEvaErrId: " + update);
        return update > 0;
    }

    /**
     * method to update isUploaded by using errorId
     * @param errID
     * @param object
     * @return
     */
    public static boolean updEVAisUploaded ( String errID, Object object, int status) {
        SQLiteDatabase db = getSugarDataBase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("IS_UPLOADED", status);

        int update = db.update(NamingHelper.toTableName(object.getClass()), contentValues, "IDENTIFIER =? ", new String[]{errID});

        Log.d(TAG, "updEvaErrId: " + update);
        return update > 0;
    }

    /**
     * method is used for updating the records
     * @param object
     * @return
     */
    public static long update ( Object object ) {
        return update(getSugarDataBase(), object);
    }

    /**
     * method is used for updating the records
     * @param db
     * @param object
     * @return
     */
    static long update ( SQLiteDatabase db, Object object ) {

        Map<Object, Long> entitiesMap = getSugarContext().getEntitiesMap();
        List<Field> columns = ReflectionUtil.getTableFields(object.getClass());
        ContentValues values = new ContentValues(columns.size());

        StringBuilder whereClause = new StringBuilder();
        List<String> whereArgs = new ArrayList<>();

        for (Field column : columns) {
            if (column.isAnnotationPresent(Unique.class)) {
                try {
                    column.setAccessible(true);
                    String columnName = NamingHelper.toColumnName(column);
                    Object columnValue = column.get(object);

                    whereClause.append(columnName).append(" = ?");
                    whereArgs.add(String.valueOf(columnValue));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                if (!column.getName().equals("id")) {
                    ReflectionUtil.addFieldValueToColumn(values, column, object, entitiesMap);
                }
            }
        }

        String[] whereArgsArray = whereArgs.toArray(new String[whereArgs.size()]);
        // Get SugarRecord based on Unique values
        long rowsEffected = db.update(NamingHelper.toTableName(object.getClass()), values, whereClause.toString(), whereArgsArray);

        if (rowsEffected == 0) {
            return save(db, object);
        } else {
            return rowsEffected;
        }
    }

    /**
     * method is used to check wheter db is sugarEntity or not
     * @param objectClass
     * @return
     */
    public static boolean isSugarEntity ( Class<?> objectClass ) {
        return objectClass.isAnnotationPresent(Table.class) || SugarRecord.class.isAssignableFrom(objectClass);
    }

    /**
     * method is used for deleting the particular table
     * @param object
     * @return
     */
    public static boolean delete ( Object object ) {
        Class<?> type = object.getClass();
        if (type.isAnnotationPresent(Table.class)) {
            try {
                Field field = type.getDeclaredField("id");
                field.setAccessible(true);
                Long id = (Long) field.get(object);
                if (id != null && id > 0L) {
                    boolean deleted = getSugarDataBase().delete(NamingHelper.toTableName(type), "Id=?", new String[]{id.toString()}) == 1;
                    if (ManifestHelper.isDebugEnabled()) {
                        Log.i(SUGAR, type.getSimpleName() + " deleted : " + id);
                    }
                    return deleted;
                } else {
                    if (ManifestHelper.isDebugEnabled()) {
                        Log.i(SUGAR, "Cannot delete object: " + object.getClass().getSimpleName() + " - object has not been saved");
                    }
                    return false;
                }
            } catch (NoSuchFieldException e) {
                if (ManifestHelper.isDebugEnabled()) {
                    Log.i(SUGAR, "Cannot delete object: " + object.getClass().getSimpleName() + " - annotated object has no id");
                }
                return false;
            } catch (IllegalAccessException e) {
                if (ManifestHelper.isDebugEnabled()) {
                    Log.i(SUGAR, "Cannot delete object: " + object.getClass().getSimpleName() + " - can't access id");
                }
                return false;
            }
        } else if (SugarRecord.class.isAssignableFrom(type)) {
            return ((SugarRecord) object).delete();
        } else {
            if (ManifestHelper.isDebugEnabled()) {
                Log.i(SUGAR, "Cannot delete object: " + object.getClass().getSimpleName() + " - not persisted");
            }
            return false;
        }
    }

    /**
     * method is used for replacing the argument array
     * @param args
     * @return
     */
    public static String[] replaceArgs ( String[] args ) {
        String[] replace = new String[args.length];
        for (int i = 0; i < args.length; i++) {

            replace[i] = (args[i].equals("true")) ? replace[i] = "1" : (args[i].equals("false")) ? replace[i] = "0" : args[i];

        }
        return replace;
    }

    /**
     * method used for getting the lsit of sessionRecords
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> lstSessionRecord ( Class<T> type ) {
        List<T> list = findWithQuery(type,
                "SELECT * FROM " + NamingHelper.toTableName(type) + " ORDER BY ID DESC LIMIT 1");
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }

/*
    public static void getEvaNotCreated ( Class<LAErrorVideoAsset> laErrorVideoAssetClass ) {
        SQLiteDatabase db = getReadleDb();
        Cursor cursor = db.query(NamingHelper.toTableName(laErrorVideoAssetClass.getClass(),))
    }
*/

    /**
     * method used for deleting the records
     * @return
     */
    public boolean delete ( ) {
        Long id = getId();
        Class<?> type = getClass();
        if (id != null && id > 0L) {
            if (ManifestHelper.isDebugEnabled()) {
                Log.i(SUGAR, type.getSimpleName() + " deleted : " + id);
            }
            return getSugarDataBase().delete(NamingHelper.toTableName(type), "Id=?", new String[]{id.toString()}) == 1;
        } else {
            if (ManifestHelper.isDebugEnabled()) {
                Log.i(SUGAR, "Cannot delete object: " + type.getSimpleName() + " - object has not been saved");
            }
            return false;
        }
    }

    /**
     * method used for saving the data to db
     * @return
     */
    public long save ( ) {
        return save(getSugarDataBase(), this);
    }

    /**
     * method used for updating the db
     * @return
     */
    public long update ( ) {
        return update(getSugarDataBase(), this);
    }

    @SuppressWarnings("unchecked")
    void inflate ( Cursor cursor ) {
        new EntityInflater()
                .withCursor(cursor)
                .withObject(this)
                .withEntitiesMap(getSugarContext().getEntitiesMap())
                .inflate();
    }

    /**
     * method returs the id
     * @return
     */
    public Long getId ( ) {
        return id;
    }

    /**
     * method is used for setting the id
     * @param id
     */
    public void setId ( Long id ) {
        this.id = id;
    }

    /**
     * CursorIterator class for database
     * @param <E>
     */
    static class CursorIterator<E> implements Iterator<E> {
        Class<E> type;
        Cursor cursor;

        public CursorIterator ( Class<E> type, Cursor cursor ) {
            this.type = type;
            this.cursor = cursor;
        }

        @Override
        public boolean hasNext ( ) {
            return cursor != null && !cursor.isClosed() && !cursor.isAfterLast();
        }

        @Override
        public E next ( ) {
            E entity = null;
            if (cursor == null || cursor.isAfterLast()) {
                throw new NoSuchElementException();
            }

            if (cursor.isBeforeFirst()) {
                cursor.moveToFirst();
            }

            try {
                entity = type.getDeclaredConstructor().newInstance();
                new EntityInflater()
                        .withCursor(cursor)
                        .withObject(entity)
                        .withEntitiesMap(getSugarContext().getEntitiesMap())
                        .inflate();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.moveToNext();
                if (cursor.isAfterLast()) {
                    cursor.close();
                }
            }

            return entity;
        }

        /**
         * method will remove all data if exits
         */
        @Override
        public void remove ( ) {
            throw new UnsupportedOperationException();
        }
    }
}



