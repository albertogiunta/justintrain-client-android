package com.jaus.albertogiunta.justintrain_oraritreni.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = Station.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract StationDaoRoom stationDao();

}
