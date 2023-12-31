package com.openclassrooms.realestatemanager.edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Photo;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.RealEstateAgent;
import com.openclassrooms.realestatemanager.repositories.AgentRepository;
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
    private final AgentRepository agentRepository;

    private final MutableLiveData<PropertyDataBinding> propertyBindingLiveData =
            new MutableLiveData<>();
    private final Executor doInBackground = Executors.newSingleThreadExecutor();
    private Property currentProperty;

    private LiveData<List<Property.PointOfInterest>> allPointOfInterest;
    private List<Property.PointOfInterest> currentPropertyPointOfInterest;
    private LiveData<List<RealEstateAgent>> allAgents;

    @Inject
    public EditPropertyViewModel(
            PropertyRepository propertyRepository,
            PhotoRepository photoRepository,
            PointOfInterestRepository pointOfInterestRepository, AgentRepository agentRepository) {
        this.propertyRepository = propertyRepository;
        this.photoRepository = photoRepository;
        this.pointOfInterestRepository = pointOfInterestRepository;
        this.agentRepository = agentRepository;
    }

    public LiveData<PropertyDataBinding> updateProperty(int propertyId) {
        final LiveData<Property> currentPropertyLiveData = propertyRepository.getById(propertyId);
        currentPropertyLiveData.observeForever(
                new Observer<Property>() {

                    @Override
                    public void onChanged(Property property) {
                        currentProperty = property;
                        final PropertyDataBinding propertyDataBinding = new PropertyDataBinding(currentProperty);
                        propertyDataBinding.setSelectedAgentPosition(currentProperty.getAgent().getId());
                        propertyBindingLiveData.setValue(propertyDataBinding);
                        currentPropertyLiveData.removeObserver(this);
                    }
                });
        return propertyBindingLiveData;
    }
    public LiveData<PropertyDataBinding> createNewProperty() {
        currentProperty = new Property();
        currentProperty.setPhotoList(new ArrayList<>());
        currentProperty.setAddress(new Property.Address());
        currentProperty.setPointOfInterestNearby(new ArrayList<>());
        propertyBindingLiveData.setValue(new PropertyDataBinding(currentProperty));
        return propertyBindingLiveData;
    }

    public void persist() {
        // TODO REFACTORING
        propertyBindingLiveData.getValue().apply();
        int selectedItemPos = propertyBindingLiveData.getValue().getSelectedAgentPosition();
        currentProperty.setAgent(allAgents.getValue().get(selectedItemPos));

        if (currentProperty.getMainPhotoUrl().isEmpty()) {
            currentProperty.setMainPhotoUrl(currentProperty.getPhotoList().get(0).getUrl());
        }
        LiveData<Integer> livePropertyId;
        if (currentProperty.getId() == 0) {
            livePropertyId = propertyRepository.create(currentProperty);
        } else {
            propertyRepository.update(currentProperty);
            livePropertyId = new MutableLiveData<>(currentProperty.getId());
        }

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

    public LiveData<List<RealEstateAgent>> getAllAgents() {
        allAgents = agentRepository.getAll();
        return allAgents;
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

    public boolean isPhotoDefined() {
        return !currentProperty.getPhotoList().isEmpty();
    }

}
