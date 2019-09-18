package com.sgs.mylibrary.orm;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sgs.mylibrary.orm.helper.ManifestHelper;
import com.sgs.mylibrary.orm.util.SugarCursorFactory;

import static com.sgs.mylibrary.orm.SugarContext.getDbConfiguration;
import static com.sgs.mylibrary.orm.helper.ManifestHelper.getDatabaseVersion;
import static com.sgs.mylibrary.orm.helper.ManifestHelper.getDbName;
import static com.sgs.mylibrary.orm.util.ContextUtil.getContext;

/**
 * Class used to create the db for the app
 */
public class SugarDb extends SQLiteOpenHelper {
    private static final String LOG_TAG = "LogAnalytics.db";

    private final SchemaGenerator schemaGenerator;
    private SQLiteDatabase sqLiteDatabase;
    private int openedConnections = 0;

    //Prevent instantiation
    private SugarDb() {
        super(getContext(), getDbName(), new SugarCursorFactory(ManifestHelper.isDebugEnabled()),
                getDatabaseVersion());
        schemaGenerator = SchemaGenerator.getInstance();
    }

    public static SugarDb getInstance() {
        return new SugarDb();
    }

    /**
     * oncrete method to intialise the schema generator
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        schemaGenerator.createDatabase(sqLiteDatabase);
    }

    /**
     * method to configure the instance of the SqliteDatabase object
     * @param db
     */
    @Override
    public void onConfigure(SQLiteDatabase db) {
        final SugarDbConfiguration configuration = getDbConfiguration();

        if (null != configuration) {
            db.setLocale(configuration.getDatabaseLocale());
            db.setMaximumSize(configuration.getMaxSize());
            db.setPageSize(configuration.getPageSize());
        }

        super.onConfigure(db);
    }

    /**
     * method is called only when the db version is changed to new version
     * @param sqLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        schemaGenerator.doUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }

    /**
     *
     * @return SqliteDatabase object
     */
    public synchronized SQLiteDatabase getDB() {
        if (this.sqLiteDatabase == null) {
            this.sqLiteDatabase = getWritableDatabase();
        }

        return this.sqLiteDatabase;
    }

    /**
     * method to get the Sqlite database instance
     * @return SqliteDatabase
     */
    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        if(ManifestHelper.isDebugEnabled()) {
            Log.d(LOG_TAG, "getReadableDatabase");
        }
        openedConnections++;
        return super.getReadableDatabase();
    }

    /**
     * method to close the SqliteDatabase after all transaction
     */
    @Override
    public synchronized void close() {
        if(ManifestHelper.isDebugEnabled()) {
            Log.d(LOG_TAG, "getReadableDatabase");
        }
        openedConnections--;
        if(openedConnections == 0) {
            if(ManifestHelper.isDebugEnabled()) {
                Log.d(LOG_TAG, "closing");
            }
            super.close();
        }
    }
}
