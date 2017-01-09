package be.cegeka.serverviewer.deployments.middleware;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class MiddlewareValidator implements Validator {

    @Autowired
    private MiddlewareRepository middlewareRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Middleware.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Middleware middleware = (Middleware) target;

        validateLocationName(errors, middleware);
        validateLocationDescription(errors, middleware);
    }

    private void validateLocationName(Errors errors, Middleware middleware) {
        String name = middleware.getName();
        if (StringUtils.isEmpty(name)) {
            errors.rejectValue("name", "middleware.name.required", "Middleware name is mandatory");
            return;
        }

        if (middleware.isNew()) {
            List<Middleware> middlewares = middlewareRepository.findByName(name);
            if (middlewares.contains(middleware)) {
                errors.rejectValue(
                        "name",
                        "middleware.name.unique",
                        new Object[]{name},
                        "A middleware with name '" + name + "' already exists"
                );
                return;
            }
        }

        if (name.length() > 45) {
            errors.rejectValue("name", "middleware.name.length", "The name of this middleware is too long");
            return;
        }
    }

    private void validateLocationDescription(Errors errors, Middleware middleware) {
        String description = middleware.getDescription();
        if (StringUtils.isNotEmpty(description) && description.length() > 100) {
            errors.rejectValue(
                    "description",
                    "middleware.description.length",
                    "The description of this middleware is too long"
            );
        }
    }


}
