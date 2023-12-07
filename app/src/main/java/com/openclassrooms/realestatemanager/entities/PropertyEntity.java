package com.openclassrooms.realestatemanager.entities;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.openclassrooms.realestatemanager.models.Photo;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.RealEstateAgent;

import java.util.List;

@Entity(tableName = "property")
public class PropertyEntity extends Property {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "agent_id")
    private int agentID;

    @Embedded
    private Address address;

    public PropertyEntity(
            Type type,
            double price,
            double surface,
            int numberOfRooms,
            String description,
            List<Photo> photoList,
            Address address,
            List<PointOrInterest> pointOfInterestNearby,
            boolean isAvailable,
            long availableSince,
            long saleDate,
            RealEstateAgent agent) {
        super(
                type,
                price,
                surface,
                numberOfRooms,
                description,
                photoList,
                address,
                pointOfInterestNearby,
                isAvailable,
                availableSince,
                saleDate,
                agent);
    }

}
