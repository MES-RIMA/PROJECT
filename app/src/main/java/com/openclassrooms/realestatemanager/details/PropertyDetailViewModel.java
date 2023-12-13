package com.openclassrooms.realestatemanager.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.repositories.PropertyRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PropertyDetailViewModel extends ViewModel {
    private final PropertyRepository propertyRepository;

    @Inject
    public PropertyDetailViewModel(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public LiveData<Property> getPropertyById(int propertyId) {
        return propertyRepository.getById(propertyId);
    }
}
