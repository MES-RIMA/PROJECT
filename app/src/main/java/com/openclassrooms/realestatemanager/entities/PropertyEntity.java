package com.openclassrooms.realestatemanager.entities;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
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
   public AddressEntity address;

    public PropertyEntity(
            int id,
            Type type,
            double price,
            double surface,
            int numberOfRooms,
            String description,
            AddressEntity address,
            boolean isSold,
            long publicationDate,
            long saleDate,
            String mainPhotoUrl) {
        super(
                type,
                price,
                surface,
                numberOfRooms,
                description,
               null,
                address,
                null,
                isSold,
                publicationDate,
                saleDate,
                null);
                setMainPhotoUrl(mainPhotoUrl);
        setId(id);
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
                parent.isSold(),
                parent.getPublicationDate(),
                parent.getSaleDate(),
                parent.getAgent());
        id = parent.getId();
        agentID = parent.getAgent().getId();
        setMainPhotoUrl(parent.getMainPhotoUrl());
    }
    public static class AddressEntity extends Address {

        @Ignore
        public AddressEntity(){}

        public AddressEntity(String locality, String postalCode, String formattedAddress){
            super(locality, postalCode, formattedAddress);
        }
        public AddressEntity(Address address){
            super(address.getLocality(), address.getPostalCode(), address.getFormattedAddress());
        }
    }
}
