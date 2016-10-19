package be.cegeka.serverviewer.servers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class EnvironmentValidator implements Validator {

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Environment.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Environment environment = (Environment) target;

        validateEnvironmentName(errors, environment);
        validateEnvironmentDescription(errors, environment);
    }

    private void validateEnvironmentName(Errors errors, Environment environment) {
        String name = environment.getName();
        if (StringUtils.isEmpty(name)) {
            errors.rejectValue("name", "environment.name.required", "Name is mandatory");
            return;
        }

        List<Environment> environments = environmentRepository.findByName(name);
        if (environments.contains(environment)){
            errors.rejectValue(
                    "name",
                    "environment.name.unique",
                    new Object[]{name},
                    "An environment with name '" + name + "' already exists"
            );
            return;
        }

        if (name.length() > 45){
            errors.rejectValue("name", "environment.name.length", "The given name is too long");
            return;
        }
    }

    private void validateEnvironmentDescription(Errors errors, Environment environment) {
        String description = environment.getDescription();
        if (StringUtils.isNotEmpty(description) && description.length() > 100){
            errors.rejectValue(
                    "description",
                    "environment.description.length",
                    "The given description is too long"
            );
        }
    }
}
