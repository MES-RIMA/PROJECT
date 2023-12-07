package com.openclassrooms.realestatemanager.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.entities.EntitiesRelations;
import com.openclassrooms.realestatemanager.entities.RealEstateAgentEntity;

import java.util.List;

@Dao
public interface RealEstateAgentDao {
    @Update
    void update(RealEstateAgentEntity property);

    @Delete
    void delete(RealEstateAgentEntity property);

    @Insert
    void create(RealEstateAgentEntity property);

    @Query("SELECT * FROM real_estate_agent")
    List<EntitiesRelations.RealEstateAgentWithPhotoAndProperties> getAllAgentProperties();
}
