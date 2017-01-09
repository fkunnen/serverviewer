package be.cegeka.serverviewer.servers.location;

import be.cegeka.serverviewer.servers.location.Location;
import be.cegeka.serverviewer.servers.location.LocationRepository;
import be.cegeka.serverviewer.servers.location.LocationValidator;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

public class LocationValidatorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private LocationValidator locationValidator = new LocationValidator();

    @Mock
    private LocationRepository locationRepository;

    @Test
    public void testSupports_LocationClassReturnsTrue(){
        boolean isAssignableFrom = locationValidator.supports(Location.class);
        Assertions.assertThat(isAssignableFrom).isTrue();
    }

    @Test
    public void testValidate_isValid(){
        Location location = new Location("Leuven");
        location.setDescription("Leuven location");
        Errors errors = new BeanPropertyBindingResult(location, "location");

        when(locationRepository.findByName(location.getName())).thenReturn(Collections.EMPTY_LIST);

        locationValidator.validate(location, errors);

        Assertions.assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    public void testValidate_LocationNameIsMandatory(){
        Location location = new Location();
        location.setDescription("Test location");
        Errors errors = new BeanPropertyBindingResult(location, "location");

        when(locationRepository.findByName(location.getName())).thenReturn(Collections.EMPTY_LIST);

        locationValidator.validate(location, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("Middleware name is mandatory");
    }

    @Test
    public void testValidate_LocationAlreadyExists(){
        Location location = new Location("Leuven");
        location.setDescription("Leuven location");
        Errors errors = new BeanPropertyBindingResult(location, "location");

        List<Location> currentLocations = Arrays.asList(location);
        when(locationRepository.findByName(location.getName())).thenReturn(currentLocations);

        locationValidator.validate(location, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("A location with name 'Leuven' already exists");
    }

    @Test
    public void testValidate_LocationNameTooLong(){
        Location location = new Location("The name for this location is more than 45 characters, the maximum size for a location name");
        location.setDescription("Leuven");
        Errors errors = new BeanPropertyBindingResult(location, "location");

        when(locationRepository.findByName(location.getName())).thenReturn(Collections.EMPTY_LIST);

        locationValidator.validate(location, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("name");
        Assertions.assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("The name of this location is too long");
    }

    @Test
    public void testValidate_LocationDescriptionTooLong(){
        Location location = new Location("Leuven");
        location.setDescription("This location can't be described in less than one hundred characters, the maximum size for a location description");
        Errors errors = new BeanPropertyBindingResult(location, "location");

        when(locationRepository.findByName(location.getName())).thenReturn(Collections.EMPTY_LIST);

        locationValidator.validate(location, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        Assertions.assertThat(errors.getFieldError().getField()).isEqualTo("description");
        Assertions.assertThat(errors.getFieldError("description").getDefaultMessage()).isEqualTo("The description of this location is too long");
    }


}