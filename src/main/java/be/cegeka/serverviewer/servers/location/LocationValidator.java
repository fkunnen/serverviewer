package be.cegeka.serverviewer.servers.location;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class LocationValidator implements Validator {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Location.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Location location = (Location) target;

        validateLocationName(errors, location);
        validateLocationDescription(errors, location);
    }

    private void validateLocationName(Errors errors, Location location) {
        String name = location.getName();
        if (StringUtils.isEmpty(name)) {
            errors.rejectValue("name", "location.name.required", "Middleware name is mandatory");
            return;
        }

        if (location.isNew()) {
            List<Location> locations = locationRepository.findByName(name);
            if (locations.contains(location)) {
                errors.rejectValue(
                        "name",
                        "location.name.unique",
                        new Object[]{name},
                        "A location with name '" + name + "' already exists"
                );
                return;
            }
        }

        if (name.length() > 45) {
            errors.rejectValue("name", "location.name.length", "The name of this location is too long");
        }
    }

    private void validateLocationDescription(Errors errors, Location location) {
        String description = location.getDescription();
        if (StringUtils.isNotEmpty(description) && description.length() > 100) {
            errors.rejectValue(
                    "description",
                    "location.description.length",
                    "The description of this location is too long"
            );
        }
    }


}
