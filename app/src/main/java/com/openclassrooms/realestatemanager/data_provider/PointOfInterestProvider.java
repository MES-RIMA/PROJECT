package com.openclassrooms.realestatemanager.data_provider;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.models.Property;

import java.util.List;

public interface PointOfInterestProvider {
    LiveData<List<Property.PointOfInterest>> getPointOfInterestByPropertyId(int property_id);

    LiveData<Integer> create(Property.PointOfInterest pointOfInterest);

    LiveData<List<Property.PointOfInterest>> getAll();

    void delete(Property.PointOfInterest pointOfInterest);

    void update(Property.PointOfInterest pointOfInterest);
}
