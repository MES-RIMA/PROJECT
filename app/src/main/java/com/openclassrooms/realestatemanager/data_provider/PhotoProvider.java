package com.openclassrooms.realestatemanager.data_provider;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.models.Photo;

import java.util.List;

public interface PhotoProvider {
    LiveData<List<Photo>> getByPropertyId(int propertyId);

    void update(Photo photo);

    void delete(Photo photo);

    void create(Photo... photo);
}
