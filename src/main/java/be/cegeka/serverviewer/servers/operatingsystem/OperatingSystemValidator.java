package be.cegeka.serverviewer.servers.operatingsystem;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class OperatingSystemValidator implements Validator {

    @Autowired
    private OperatingSystemRepository operatingSystemRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return OperatingSystem.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OperatingSystem operatingSystem = (OperatingSystem) target;

        validateOperatingSystemName(errors, operatingSystem);
        validateOperatingSystemDescription(errors, operatingSystem);
    }

    private void validateOperatingSystemName(Errors errors, OperatingSystem operatingSystem) {
        String name = operatingSystem.getName();
        if (StringUtils.isEmpty(name)) {
            errors.rejectValue("name", "operatingSystem.name.required", "Operating system name is mandatory");
            return;
        }

        if (operatingSystem.isNew()) {
            List<OperatingSystem> operatingSystems = operatingSystemRepository.findByName(name);
            if (operatingSystems.contains(operatingSystem)) {
                errors.rejectValue(
                        "name",
                        "operatingSystem.name.unique",
                        new Object[]{name},
                        "An operating system with name '" + name + "' already exists"
                );
                return;
            }
        }

        if (name.length() > 45) {
            errors.rejectValue("name", "operatingSystem.name.length", "The name of this operating system is too long");
            return;
        }
    }

    private void validateOperatingSystemDescription(Errors errors, OperatingSystem operatingSystem) {
        String description = operatingSystem.getDescription();
        if (StringUtils.isNotEmpty(description) && description.length() > 100) {
            errors.rejectValue(
                    "description",
                    "operatingSystem.description.length",
                    "The description of this operating system is too long"
            );
        }
    }


}
