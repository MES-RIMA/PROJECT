package com.openclassrooms.realestatemanager.adapters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.openclassrooms.realestatemanager.dao.PhotoDao;
import com.openclassrooms.realestatemanager.data_provider.PhotoProvider;
import com.openclassrooms.realestatemanager.entities.PhotoEntity;
import com.openclassrooms.realestatemanager.models.Photo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PhotoDataProvider implements PhotoProvider {
    private final PhotoDao photoDao;

    public PhotoDataProvider(PhotoDao photoDao) {
        this.photoDao = photoDao;
    }

    @Override
    public LiveData<List<Photo>> getByPropertyId(int propertyId) {
        return Transformations.map(photoDao.getByPropertyId(propertyId), this::toModels);
    }
    private List<Photo> toModels(List<PhotoEntity> entities){
        return entities.stream().map(PhotoEntity::toModel).collect(Collectors.toList());
    }
    @Override
    public void update(Photo photo) {
        photoDao.update(new PhotoEntity(photo));
    }

    @Override
    public void delete(Photo photo) {
        photoDao.delete(new PhotoEntity(photo));
    }

    @Override
    public void create(Photo... photo) {
        photoDao.create(Arrays.stream(photo).map(PhotoEntity::new).toArray(PhotoEntity[]::new));
    }
}
