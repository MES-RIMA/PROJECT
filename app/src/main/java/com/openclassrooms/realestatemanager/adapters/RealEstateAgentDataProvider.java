package com.openclassrooms.realestatemanager.adapters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.openclassrooms.realestatemanager.dao.RealEstateAgentDao;
import com.openclassrooms.realestatemanager.data_provider.RealEstateAgentProvider;
import com.openclassrooms.realestatemanager.entities.RealEstateAgentEntity;
import com.openclassrooms.realestatemanager.models.RealEstateAgent;

import java.util.List;
import java.util.stream.Collectors;

public class RealEstateAgentDataProvider implements RealEstateAgentProvider {

    private final RealEstateAgentDao realEstateAgentDao;

    public RealEstateAgentDataProvider(RealEstateAgentDao realEstateAgentDao) {
        this.realEstateAgentDao = realEstateAgentDao;
    }

    @Override
    public LiveData<List<RealEstateAgent>> getAll() {
        return Transformations.map(realEstateAgentDao.getAll(), this::toModels);
    }

    private List<RealEstateAgent> toModels(List<RealEstateAgentEntity> entities) {
        return entities.stream().map(RealEstateAgentEntity::toModel).collect(Collectors.toList());
    }
}
