package com.openclassrooms.realestatemanager.entities;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.openclassrooms.realestatemanager.models.Photo;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.RealEstateAgent;

import java.util.List;

@Entity(
        tableName = "property",
        ignoredColumns = {"photoList", "pointOfInterestNearby", "agent"})
public class PropertyEntity extends Property {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "property_id")
    public int id;

    @ColumnInfo(name = "agent_id")
    public int agentID;

    @Embedded
    private Address address;

    public PropertyEntity(
            Type type,
            double price,
            double surface,
            int numberOfRooms,
            String description,
            Address address,
            boolean isAvailable,
            long availableSince,
            long saleDate) {
        super(
                type,
                price,
                surface,
                numberOfRooms,
                description,
               null,
                address,
                null,
                isAvailable,
                availableSince,
                saleDate,
                null);
    }
    public PropertyEntity(Property parent) {
        super(
                parent.getType(),
                parent.getPrice(),
                parent.getSurface(),
                parent.getNumberOfRooms(),
                parent.getDescription(),
                parent.getPhotoList(),
                parent.getAddress(),
                parent.getPointOfInterestNearby(),
                parent.isAvailable(),
                parent.getAvailableSince(),
                parent.getSaleDate(),
                parent.getAgent());
        id = parent.getId();
    }
}
