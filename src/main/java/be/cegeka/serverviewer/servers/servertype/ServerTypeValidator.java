package be.cegeka.serverviewer.servers.servertype;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class ServerTypeValidator implements Validator {

    @Autowired
    private ServerTypeRepository serverTypeRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ServerType.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ServerType serverType = (ServerType) target;

        validateServerTypeName(errors, serverType);
        validateServerTypeDescription(errors, serverType);
    }

    private void validateServerTypeName(Errors errors, ServerType serverType) {
        String name = serverType.getName();
        if (StringUtils.isEmpty(name)) {
            errors.rejectValue("name", "serverType.name.required", "Server type name is mandatory");
            return;
        }

        if (serverType.isNew()) {
            List<ServerType> serverTypes = serverTypeRepository.findByName(name);
            if (serverTypes.contains(serverType)) {
                errors.rejectValue(
                        "name",
                        "serverType.name.unique",
                        new Object[]{name},
                        "A server type with name '" + name + "' already exists"
                );
                return;
            }
        }

        if (name.length() > 45) {
            errors.rejectValue("name", "serverType.name.length", "The name of this server type is too long");
        }
    }

    private void validateServerTypeDescription(Errors errors, ServerType serverType) {
        String description = serverType.getDescription();
        if (StringUtils.isNotEmpty(description) && description.length() > 100) {
            errors.rejectValue(
                    "description",
                    "serverType.description.length",
                    "The description of this server type is too long"
            );
        }
    }


}
