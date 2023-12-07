package com.openclassrooms.realestatemanager.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.openclassrooms.realestatemanager.entities.Relationships;

@Dao
public interface PropertyPointOfInterestCrossRefDao {
    @Delete
    void delete(Relationships.PropertyPointOfInterestCrossRef associationClass);

    @Insert
    void create(Relationships.PropertyPointOfInterestCrossRef associationClass);
}
