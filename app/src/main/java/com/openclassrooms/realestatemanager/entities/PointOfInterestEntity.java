package com.openclassrooms.realestatemanager.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.openclassrooms.realestatemanager.models.Property;
@Entity(tableName = "point_of_interest")
public class PointOfInterestEntity extends Property.PointOfInterest {
    @ColumnInfo(name = "point_of_interest_id")
    @PrimaryKey(autoGenerate = true)
    public int id;

    public PointOfInterestEntity(String name) {
        super(name);
    }
    public PointOfInterestEntity toModel(){
        return this;
    }
}
