package com.openclassrooms.realestatemanager.adapters;

import com.openclassrooms.realestatemanager.dao.PhotoDao;
import com.openclassrooms.realestatemanager.dao.PointOfInterestDao;
import com.openclassrooms.realestatemanager.dao.PropertyDao;
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

public class PropertyDataProvider implements PropertyProvider {
    private final RealEstateAgentDao realEstateAgentDao;
    private final PropertyDao propertyDao;
    private final PhotoDao photoDao;
    private final PointOfInterestDao pointOfInterestDao;

    public PropertyDataProvider(RealEstateAgentDao realEstateAgentDao, PropertyDao propertyDao, PhotoDao photoDao, PointOfInterestDao pointOfInterestDao) {
        this.realEstateAgentDao = realEstateAgentDao;
        this.propertyDao = propertyDao;
        this.photoDao = photoDao;
        this.pointOfInterestDao = pointOfInterestDao;
    }

    @Override
    public Property getById(int propertyId) {
        final PropertyEntity propertyEntity = propertyDao.getById(propertyId);
        final RealEstateAgentEntity agentEntity = realEstateAgentDao.getById(propertyEntity.agentID);

        final List<PointOfInterestEntity> pointOfInterestEntities =
                pointOfInterestDao.getPointOfInterestByPropertyId(propertyId);

        final List<PhotoEntity> photoEntities = photoDao.getByPropertyId(propertyId);

        propertyEntity.setAgent(agentEntity);
        propertyEntity.setPhotoList(photoEntities);
        propertyEntity.setPointOfInterestNearby(pointOfInterestEntities);

        return propertyEntity;
    }

    @Override
    public List<Property> getAll() {
        final List<Relationships.RealEstateAgentWithProperties> realEstateAgentWithProperties =
                realEstateAgentDao.getAllAgentWithProperties();
        final List<Property> properties = new ArrayList<>();

        for (Relationships.RealEstateAgentWithProperties entry : realEstateAgentWithProperties) {
            properties.addAll(entry.toProperties());
        }

        return properties;
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
    public void create(Property property) {
        propertyDao.create(new PropertyEntity(property));
    }
}
