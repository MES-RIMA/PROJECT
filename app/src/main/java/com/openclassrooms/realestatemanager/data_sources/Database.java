package com.openclassrooms.realestatemanager.data_sources;

import android.app.Application;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.openclassrooms.realestatemanager.dao.PhotoDao;
import com.openclassrooms.realestatemanager.dao.PropertyDao;
import com.openclassrooms.realestatemanager.dao.RealEstateAgentDao;
import com.openclassrooms.realestatemanager.entities.PhotoEntity;
import com.openclassrooms.realestatemanager.entities.PropertyEntity;
import com.openclassrooms.realestatemanager.entities.RealEstateAgentEntity;

@androidx.room.Database(entities = {PropertyEntity.class, PhotoEntity.class, RealEstateAgentEntity.class},
        version = 1)
public abstract class Database extends RoomDatabase {
    private static Database instance;

    public abstract PropertyDao getPropertyDao();
    public abstract RealEstateAgentDao getRealEstateAgentDao();
    public abstract PhotoDao getPhotoDao();

    public static synchronized Database getInstance(Application application) {
        if (instance == null) {
            synchronized (Database.class) {
                instance =
                        Room.databaseBuilder(application, Database.class, "real_estate_manager.db").build();
            }
        }
        return instance;
    }
}
