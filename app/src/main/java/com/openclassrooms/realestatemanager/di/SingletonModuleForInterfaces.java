package com.openclassrooms.realestatemanager.di;

import com.openclassrooms.realestatemanager.adapters.PhotoDataProvider;
import com.openclassrooms.realestatemanager.adapters.PointOfInterestDataProvider;
import com.openclassrooms.realestatemanager.adapters.PropertyDataProvider;
import com.openclassrooms.realestatemanager.adapters.RealEstateAgentDataProvider;
import com.openclassrooms.realestatemanager.data_provider.PhotoProvider;
import com.openclassrooms.realestatemanager.data_provider.PointOfInterestProvider;
import com.openclassrooms.realestatemanager.data_provider.PropertyProvider;
import com.openclassrooms.realestatemanager.data_provider.RealEstateAgentProvider;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class SingletonModuleForInterfaces {
    @Binds
    @Singleton
    public abstract PropertyProvider bindPropertyProvider(PropertyDataProvider propertyDataProvider);
    @Binds
    @Singleton
    public abstract PhotoProvider bindPropertyPhotoProvider(PhotoDataProvider photoDataProvider);

    @Binds
    @Singleton
    public abstract RealEstateAgentProvider bindAgentProvider(
            RealEstateAgentDataProvider realEstateAgentDataProvider);

    @Binds
    @Singleton
    public abstract PointOfInterestProvider bingPointOfInterestDataProvider(
            PointOfInterestDataProvider pointOfInterestDataProvider);

}
