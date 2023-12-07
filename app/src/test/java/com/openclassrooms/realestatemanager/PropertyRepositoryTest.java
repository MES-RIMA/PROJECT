package com.openclassrooms.realestatemanager;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.openclassrooms.realestatemanager.data_provider.PropertyProvider;
import com.openclassrooms.realestatemanager.models.Photo;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.RealEstateAgent;
import com.openclassrooms.realestatemanager.repositories.PropertyRepository;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

public class PropertyRepositoryTest {
    private PropertyProvider propertyProvider;
    private PropertyRepository propertyRepository;
    private final Photo fakePhoto = new Photo("url", "desc");
    private final RealEstateAgent fakeAgent = new RealEstateAgent("name", "PhotoUrl");
    private final Property.Address fakeAddress = new Property.Address("locality", "postalCode", "formattedAddr");
    private final Property fakeProperty =
            new Property(
                    Property.Type.HOUSE,
                    2.5,
                    54.56,
                    5,
                    "desc",
                    Collections.singletonList(fakePhoto),
                    fakeAddress,
                    Collections.singletonList(new Property.PointOrInterest("name")),
                    false,
                    464,
                    3254,
                    fakeAgent);

    @Before
    public void setUp() {
        propertyProvider = mock(PropertyProvider.class);
        propertyRepository = new PropertyRepository(propertyProvider);
        setUpMocks();
    }

    private void setUpMocks(){
        when(propertyProvider.getById(fakeProperty.getId())).thenReturn(fakeProperty);
        when(propertyProvider.getAll()).thenReturn(Collections.singletonList(fakeProperty));
    }

    @Test
    public void create() {
        propertyRepository.create(fakeProperty);
        verify(propertyProvider, timeout(100)).create(fakeProperty);
    }

    @Test
    public void getById() {
        assertEquals(propertyRepository.getById(fakeProperty.getId()), fakeProperty);
    }

    @Test
    public void getAll() {
        assertTrue(propertyRepository.getAll().contains(fakeProperty));
    }

    @Test
    public void update() {
        propertyRepository.update(fakeProperty);
        verify(propertyProvider, timeout(100)).update(fakeProperty);
    }

    @Test
    public void delete() {
        propertyRepository.delete(fakeProperty);
        verify(propertyProvider, timeout(100)).delete(fakeProperty);
    }
}
