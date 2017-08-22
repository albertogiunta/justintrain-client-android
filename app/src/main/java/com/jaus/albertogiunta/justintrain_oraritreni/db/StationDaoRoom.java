package com.jaus.albertogiunta.justintrain_oraritreni.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StationDaoRoom {

    @Query("SELECT * FROM station")
    List<Station> getAll();

    @Query("SELECT * FROM station WHERE name_fancy LIKE :nameFancy OR name_fancy LIKE :nameFancy2")
    List<Station> getAllByNameFancy(String nameFancy, String nameFancy2);

    @Query("SELECT * FROM station WHERE name_long LIKE :nameLong1 OR name_long LIKE :nameLong2")
    List<Station> getAllByNameLong(String nameLong1, String nameLong2);

    @Query("SELECT * FROM station WHERE likelihood(name_long LIKE :nameLong, 0.05)")
    List<Station> getAllByNameLongWithLikelihood(String nameLong);

    @Query("SELECT * FROM station WHERE name_short LIKE :nameShort")
    List<Station> getAllByNameShort(String nameShort);

    @Query("SELECT * FROM station WHERE name_short LIKE :name OR name_long LIKE :name")
    Station getByWhateverName(String name);

    @Query("SELECT count(1) FROM station WHERE name_short LIKE :name OR name_long LIKE :name")
    boolean isStationNameValid(String name);
}
