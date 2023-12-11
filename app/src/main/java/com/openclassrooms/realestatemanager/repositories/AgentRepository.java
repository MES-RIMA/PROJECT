package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data_provider.RealEstateAgentProvider;
import com.openclassrooms.realestatemanager.models.RealEstateAgent;

import java.util.List;

import javax.inject.Inject;

public class AgentRepository {
    private final RealEstateAgentProvider realEstateAgentProvider;

    @Inject
    public AgentRepository(RealEstateAgentProvider realEstateAgentProvider) {
        this.realEstateAgentProvider = realEstateAgentProvider;
    }

    public LiveData<List<RealEstateAgent>> getAll(){
        return realEstateAgentProvider.getAll();
    }
}
