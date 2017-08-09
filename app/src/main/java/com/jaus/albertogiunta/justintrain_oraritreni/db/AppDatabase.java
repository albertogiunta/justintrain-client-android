package com.jaus.albertogiunta.justintrain_oraritreni.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.jaus.albertogiunta.justintrain_oraritreni.db.sqliteasset.AssetSQLiteOpenHelperFactory;

@Database(entities = {Station.class}, version = 1)
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
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


}
