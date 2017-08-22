package com.jaus.albertogiunta.justintrain_oraritreni.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.jaus.albertogiunta.justintrain_oraritreni.db.sqliteAsset.AssetSQLiteOpenHelperFactory;

@Database(entities = {Station.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

//    public abstract StationDaoRoom stationDao();

    private static AppDatabase INSTANCE;

    public abstract StationDaoRoom stationDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {

            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "justintrain.db")
                            // allow queries on the main thread.
                            // Don't do\ this on a real app! See PersistenceBasicSample for an example.
                            .openHelperFactory(new AssetSQLiteOpenHelperFactory())
                            .addMigrations(MIGRATION_1_2)
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE station "
                    + " ADD COLUMN name_fancy TEXT");
        }
    };

}
