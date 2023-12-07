package com.openclassrooms.realestatemanager.data_provider;

import com.openclassrooms.realestatemanager.models.Property;

import java.util.List;

public interface PropertyProvider {
    Property getById(int id);
    List<Property> getAll();
    void update(Property property);
    void delete(Property property);
    void create(Property property);

}
