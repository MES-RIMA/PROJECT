package com.openclassrooms.realestatemanager.activities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.repositories.PropertyRepository;

import java.util.List;

import javax.inject.Inject;

public class MainActivityViewModel extends ViewModel {
    private final PropertyRepository propertyRepository;
    private LiveData<List<Property>> allProperties;

    @Inject
    public MainActivityViewModel(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public LiveData<List<Property>> getProperties(){
        if(allProperties == null){
            allProperties = propertyRepository.getAll();
        }
        return allProperties;
    }
}
