package com.openclassrooms.realestatemanager.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.openclassrooms.realestatemanager.models.Photo;
import com.openclassrooms.realestatemanager.models.RealEstateAgent;
@Entity(tableName = "real_estate_agent")
public class RealEstateAgentEntity extends RealEstateAgent {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public RealEstateAgentEntity(String name, Photo photo) {
        super(name, photo);
    }
}
