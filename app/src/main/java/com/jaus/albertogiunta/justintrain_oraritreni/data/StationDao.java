package com.jaus.albertogiunta.justintrain_oraritreni.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface StationDao {

    @Query("SELECT * FROM station")
    List<Station> getAllByNameLong();

    @Query("SELECT * FROM station WHERE name_long LIKE :nameLong")
    List<Station> getAllByNameLong(String nameLong);

    @Query("SELECT * FROM station WHERE name_short LIKE :nameShort")
    List<Station> getAllByNameShort(String nameShort);

    @Query("SELECT * FROM station WHERE name_short LIKE :name || name_long LIKE :name")
    Station getByWhateverName(String name);

    @Query("SELECT count(1) FROM station WHERE name_short LIKE :name || name_long LIKE :name")
    boolean isStationNameValid(String name);
}
