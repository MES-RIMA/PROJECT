package com.openclassrooms.realestatemanager.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.entities.PhotoEntity;

import java.util.List;

@Dao
public interface PhotoDao {
    @Query("SELECT * FROM photo_entity WHERE property_id=:propertyId")
    List<PhotoEntity> getByPropertyId(int propertyId);

    @Update
    void update(PhotoEntity photo);

    @Delete
    void delete(PhotoEntity photo);

    @Insert
    void create(PhotoEntity... photo);

}
