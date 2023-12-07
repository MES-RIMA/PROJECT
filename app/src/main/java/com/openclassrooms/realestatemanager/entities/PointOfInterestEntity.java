package com.openclassrooms.realestatemanager.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.openclassrooms.realestatemanager.models.Property;
@Entity(tableName = "point_of_interest")
public class PointOfInterestEntity extends Property.PointOrInterest {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public PointOfInterestEntity(String name) {
        super(name);
    }
}
