package be.cegeka.serverviewer.applications;

import be.cegeka.serverviewer.servers.Environment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class ApplicationValidator implements Validator {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Application.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Application application = (Application) target;

        validateApplicationName(errors, application);
        validateApplicationDescription(errors, application);
    }

    private void validateApplicationName(Errors errors, Application application) {
        String name = application.getName();
        if (StringUtils.isEmpty(name)) {
            errors.rejectValue("name", "application.name.required", "Application name is mandatory");
            return;
        }

        if (application.isNew()) {
            List<Application> applications = applicationRepository.findByName(name);
            if (applications.contains(application)) {
                errors.rejectValue(
                        "name",
                        "application.name.unique",
                        new Object[]{name},
                        "An application with name '" + name + "' already exists"
                );
                return;
            }
        }

        if (name.length() > 45){
            errors.rejectValue("name", "application.name.length", "The name of this application is too long");
            return;
        }
    }

    private void validateApplicationDescription(Errors errors, Application application) {
        String description = application.getDescription();
        if (StringUtils.isNotEmpty(description) && description.length() > 100){
            errors.rejectValue(
                    "description",
                    "application.description.length",
                    "The description of this application is too long"
            );
        }
    }
}
