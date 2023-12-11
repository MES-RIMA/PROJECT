package com.openclassrooms.realestatemanager.data_provider;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.models.Property;

import java.util.List;

public interface PropertyProvider {
    Property getById(int id);
    LiveData<List<Property>> getAll();
    void update(Property property);
    void delete(Property property);
    LiveData<Integer> create(Property property);
    void associateWithPointOfInterest(int propertyId, int pointOfInterestId);
    void removePointOfInterestFromProperty(int propertyId, int pointOfInterestId);

}
