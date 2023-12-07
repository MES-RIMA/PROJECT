package com.openclassrooms.realestatemanager.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.openclassrooms.realestatemanager.models.Photo;

@Entity(tableName = "photo_entity")
public class PhotoEntity extends Photo {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "photo_id")
    public int id;

    @ColumnInfo(name = "property_id")
    public int propertyId;

    public PhotoEntity(String url, String description) {
        super(url, description);
    }
    public Photo toModel(){
        return this;
    }
}
