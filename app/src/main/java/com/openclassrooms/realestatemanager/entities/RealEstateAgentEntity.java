package com.openclassrooms.realestatemanager.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;
import com.openclassrooms.realestatemanager.models.RealEstateAgent;
@Entity(tableName = "real_estate_agent")
public class RealEstateAgentEntity extends RealEstateAgent {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "agent_id")
    public int id;

    public RealEstateAgent toModel() {
        final RealEstateAgent model = new RealEstateAgent(getName(), getPhotoUrl());
        model.setId(id);
        return model;
    }
    public RealEstateAgentEntity(String name, String photoUrl) {
        super(name, photoUrl);
    }
}
