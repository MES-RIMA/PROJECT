package com.openclassrooms.realestatemanager.adapters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.openclassrooms.realestatemanager.dao.PointOfInterestDao;
import com.openclassrooms.realestatemanager.data_provider.PointOfInterestProvider;
import com.openclassrooms.realestatemanager.entities.PointOfInterestEntity;
import com.openclassrooms.realestatemanager.models.Property;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class PointOfInterestDataProvider implements PointOfInterestProvider {
    private final PointOfInterestDao pointOfInterestDao;

    public PointOfInterestDataProvider(PointOfInterestDao pointOfInterestDao) {
        this.pointOfInterestDao = pointOfInterestDao;
    }

    @Override
    public LiveData<List<Property.PointOfInterest>> getPointOfInterestByPropertyId(int propertyId) {
        return Transformations.map(
                pointOfInterestDao.getPointOfInterestByPropertyId(propertyId), this::toModels);
    }

    @Override
    public LiveData<Integer> create(Property.PointOfInterest pointOfInterest) {
        final MutableLiveData<Integer> liveId = new MutableLiveData<>();
        Executors.newSingleThreadExecutor()
                .execute(
                        () -> {
                            liveId.postValue(
                                    (int) pointOfInterestDao.create(new PointOfInterestEntity(pointOfInterest)));
                        });
        return liveId;
    }


    @Override
    public LiveData<List<Property.PointOfInterest>> getAll() {
        return Transformations.map(pointOfInterestDao.getAll(), this::toModels);
    }

    @Override
    public void delete(Property.PointOfInterest pointOfInterest) {
        pointOfInterestDao.delete(new PointOfInterestEntity(pointOfInterest));
    }

    @Override
    public void update(Property.PointOfInterest pointOfInterest) {
        pointOfInterestDao.update(new PointOfInterestEntity(pointOfInterest));
    }
    private List<Property.PointOfInterest> toModels(List<PointOfInterestEntity> entities) {
        return entities.stream().map(PointOfInterestEntity::toModel).collect(Collectors.toList());
    }
}
