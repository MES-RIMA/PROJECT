package com.openclassrooms.realestatemanager.data_provider;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.models.RealEstateAgent;

import java.util.List;

public interface RealEstateAgentProvider {
    LiveData<List<RealEstateAgent>> getAll();
}
