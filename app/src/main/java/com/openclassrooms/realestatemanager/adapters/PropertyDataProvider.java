package com.openclassrooms.realestatemanager.adapters;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.openclassrooms.realestatemanager.dao.PhotoDao;
import com.openclassrooms.realestatemanager.dao.PointOfInterestDao;
import com.openclassrooms.realestatemanager.dao.PropertyDao;
import com.openclassrooms.realestatemanager.dao.PropertyPointOfInterestCrossRefDao;
import com.openclassrooms.realestatemanager.dao.RealEstateAgentDao;
import com.openclassrooms.realestatemanager.data_provider.PropertyProvider;
import com.openclassrooms.realestatemanager.entities.PhotoEntity;
import com.openclassrooms.realestatemanager.entities.PointOfInterestEntity;
import com.openclassrooms.realestatemanager.entities.PropertyEntity;
import com.openclassrooms.realestatemanager.entities.RealEstateAgentEntity;
import com.openclassrooms.realestatemanager.entities.Relationships;
import com.openclassrooms.realestatemanager.models.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PropertyDataProvider implements PropertyProvider {
    private final RealEstateAgentDao realEstateAgentDao;
    private final PropertyDao propertyDao;
    private final PhotoDao photoDao;
    private final PointOfInterestDao pointOfInterestDao;
    private final PropertyPointOfInterestCrossRefDao propertyPointOfInterestCrossRefDao;
    private final Executor doInBackground = Executors.newSingleThreadExecutor();

    private MutableLiveData<List<Property>> allProperties;
    PropertyEntity propertyEntity;
    public PropertyDataProvider(
            RealEstateAgentDao realEstateAgentDao,
            PropertyDao propertyDao,
            PhotoDao photoDao,
            PointOfInterestDao pointOfInterestDao,
            PropertyPointOfInterestCrossRefDao propertyPointOfInterestCrossRefDao) {
        this.realEstateAgentDao = realEstateAgentDao;
        this.propertyDao = propertyDao;
        this.photoDao = photoDao;
        this.pointOfInterestDao = pointOfInterestDao;
        this.propertyPointOfInterestCrossRefDao = propertyPointOfInterestCrossRefDao;
    }

    @Override
    public LiveData<Property> getById(int propertyId) {
        // TODO REFACTORING
        final MutableLiveData<Property> propertyLiveData = new MutableLiveData<>();
        doInBackground.execute(
                () -> {
                    propertyEntity = propertyDao.getById(propertyId);
                    propertyEntity.setAgent(realEstateAgentDao.getById(propertyEntity.agentID));
                });

        final LiveData<List<PointOfInterestEntity>> pointOfInterestsLiveData =
                pointOfInterestDao.getPointOfInterestByPropertyId(propertyId);

        pointOfInterestsLiveData.observeForever(
                new Observer<List<PointOfInterestEntity>>() {

                    @Override
                    public void onChanged(List<PointOfInterestEntity> pointOfInterestEntities) {
                        propertyEntity.setPointOfInterestNearby(pointOfInterestEntities);
                        pointOfInterestsLiveData.removeObserver(this);

                        final LiveData<List<PhotoEntity>> photoEntitiesLiveData =
                                photoDao.getByPropertyId(propertyId);

                        photoEntitiesLiveData.observeForever(
                                new Observer<List<PhotoEntity>>() {

                                    @Override
                                    public void onChanged(List<PhotoEntity> photoEntities) {
                                        propertyEntity.setPhotoList(photoEntities);
                                        photoEntitiesLiveData.removeObserver(this);
                                        propertyLiveData.setValue(propertyEntity);
                                    }
                                });
                    }
                });
        return propertyLiveData;
    }
    @Override
    public LiveData<List<Property>> getAll() {
        if (allProperties == null) {
            allProperties = new MutableLiveData<>();
            doInBackground.execute(
                    () -> {
                        final List<Relationships.RealEstateAgentWithProperties> realEstateAgentWithProperties =
                                realEstateAgentDao.getAllAgentWithProperties();
                        final List<Property> properties = new ArrayList<>();

                        for (Relationships.RealEstateAgentWithProperties entry : realEstateAgentWithProperties) {
                            properties.addAll(entry.toProperties());
                        }
                        allProperties.postValue(properties);
                    });
        }
        return allProperties;
    }

    @Override
    public void update(Property property) {
        propertyDao.update(new PropertyEntity(property));
    }

    @Override
    public void delete(Property property) {
        propertyDao.delete(new PropertyEntity(property));
    }

    @Override
    public LiveData<Integer> create(Property property) {
        final MutableLiveData<Integer> liveId = new MutableLiveData<>();
        Executors.newSingleThreadExecutor()
                .execute(
                        () -> {
                            final PropertyEntity propertyEntity = new PropertyEntity(property);
                            Log.d("AGENT_ID", "IS :" + propertyEntity.agentID + " | VS :" + property.getAgent().getId());
                            final int id = (int) propertyDao.create(propertyEntity);
                            liveId.postValue(id);
                            property.setId(id);
                            allProperties.getValue().add(property);
                            allProperties.postValue(allProperties.getValue());
                        });
        return liveId;
    }

    @Override
    public void associateWithPointOfInterest(int propertyId, int pointOfInterestId) {
        propertyPointOfInterestCrossRefDao.create(getAssociation(propertyId, pointOfInterestId));
    }

    @Override
    public void removePointOfInterestFromProperty(int propertyId, int pointOfInterestId) {
        propertyPointOfInterestCrossRefDao.delete(getAssociation(propertyId, pointOfInterestId));
    }

    private Relationships.PropertyPointOfInterestCrossRef getAssociation(
            int propertyId, int pointOfInterestId) {
        final Relationships.PropertyPointOfInterestCrossRef association =
                new Relationships.PropertyPointOfInterestCrossRef();
        association.pointOfInterestId = pointOfInterestId;
        association.propertyId = propertyId;
        return association;
    }
}
