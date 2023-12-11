package com.openclassrooms.realestatemanager.edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Photo;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.RealEstateAgent;
import com.openclassrooms.realestatemanager.repositories.PhotoRepository;
import com.openclassrooms.realestatemanager.repositories.PointOfInterestRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EditPropertyViewModel extends ViewModel {
    private LiveData<Integer> livePointOfInterestId;
    public static final RealEstateAgent AGENT_PLACEHOLDER =
            new RealEstateAgent("Select Agent", "file:///android_asset/person.png");
    // DATA_SOURCES
    private final PropertyRepository propertyRepository;
    private final PhotoRepository photoRepository;
    private final PointOfInterestRepository pointOfInterestRepository;


    private final MutableLiveData<PropertyDataBinding> propertyToBeUpdated = new MutableLiveData<>();
    private final Executor doInBackground = Executors.newSingleThreadExecutor();
    private Property currentProperty;
    private LiveData<List<Property.PointOfInterest>> allPointOfInterest;
    private List<Property.PointOfInterest> currentPropertyPointOfInterest;
    @Inject
    public EditPropertyViewModel(
            PropertyRepository propertyRepository,
            PhotoRepository photoRepository,
            PointOfInterestRepository pointOfInterestRepository) {
        this.propertyRepository = propertyRepository;
        this.photoRepository = photoRepository;
        this.pointOfInterestRepository = pointOfInterestRepository;
    }

    public LiveData<PropertyDataBinding> updateProperty(int propertyId) {
        doInBackground.execute(
                () -> {
                    currentProperty = propertyRepository.getById(propertyId);
                    propertyToBeUpdated.setValue(new PropertyDataBinding(currentProperty));
                });
        return propertyToBeUpdated;
    }

    public PropertyDataBinding createNewProperty() {
        currentProperty = new Property();
        currentProperty.setPhotoList(new ArrayList<>());
        currentProperty.setAddress(new Property.Address());
        currentProperty.setPointOfInterestNearby(new ArrayList<>());
        return new PropertyDataBinding(currentProperty);
    }

    public void persist() {
        // TODO REFACTORING
        if(currentProperty.getMainPhotoUrl().isEmpty()){
            currentProperty.setMainPhotoUrl(currentProperty.getPhotoList().get(0).getUrl());
        }
        final LiveData<Integer> livePropertyId = propertyRepository.create(currentProperty);

        livePropertyId.observeForever(
                new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer propertyId) {

                        for (Property.PointOfInterest pointOfInterest : currentProperty.getPointOfInterestNearby()) {
                            livePointOfInterestId = pointOfInterestRepository.create(pointOfInterest);
                            livePointOfInterestId.observeForever(
                                    new Observer<Integer>() {
                                        @Override
                                        public void onChanged(Integer pointOfInterestId) {
                                            propertyRepository.addPointOfInterestToProperty(
                                                    propertyId, pointOfInterestId);
                                            livePointOfInterestId.removeObserver(this);
                                        }
                                    });
                        }

                        final Photo[] propertyPhotos =
                                currentProperty.getPhotoList().stream()
                                        .peek(photo -> photo.setPropertyId(propertyId))
                                        .toArray(Photo[]::new);

                        photoRepository.create(propertyPhotos);

                        livePropertyId.removeObserver(this);
                    }
                });
    }
    public List<RealEstateAgent> getAllAgents() {
        final List<RealEstateAgent> agentList = new ArrayList<>();
        agentList.add(0, AGENT_PLACEHOLDER);
        return agentList;
    }

    public LiveData<List<Property.PointOfInterest>> getAllPointsOfInterests() {
        if (allPointOfInterest == null) {
            allPointOfInterest = pointOfInterestRepository.getAll();
        }
        return allPointOfInterest;
    }

    public List<Property.PointOfInterest> getCurrentPropertyPointOfInterests() {
        if (currentPropertyPointOfInterest == null) {
            currentPropertyPointOfInterest = currentProperty.getPointOfInterestNearby();
        }
        return currentPropertyPointOfInterest;
    }

    public void addPointOfInterestToCurrentProperty(Property.PointOfInterest pointOfInterest) {
        currentProperty.getPointOfInterestNearby().add(pointOfInterest);

    }

    public void removePointOrInterestFromCurrentProperty(Property.PointOfInterest pointOfInterest) {
        currentProperty.getPointOfInterestNearby().remove(pointOfInterest);

    }

    public LiveData<Integer> createPointOfInterest(Property.PointOfInterest pointOfInterest) {
        return pointOfInterestRepository.create(pointOfInterest);
    }

    public boolean containsPointOfInterest(Property.PointOfInterest pointOfInterest) {
        return currentProperty.getPointOfInterestNearby().contains(pointOfInterest);
    }

    public void addPhotoToCurrentProperty(Photo photo) {
        currentProperty.getPhotoList().add(photo);
    }

    public List<Photo> getPropertyPhotos() {
        return currentProperty.getPhotoList();
    }
    public void setMainPhoto(Photo photo) {
        currentProperty.setMainPhotoUrl(photo.getUrl());
    }

    public boolean isPhotoDefined(){
        return !currentProperty.getPhotoList().isEmpty();
    }

}
