package be.cegeka.serverviewer.servers.server;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class ServerValidator implements Validator {

    @Autowired
    private ServerRepository serverRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Server.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Server server = (Server) target;

        validateServerName(errors, server);
        validateServerDescription(errors, server);
    }

    private void validateServerName(Errors errors, Server server) {
        String name = server.getName();
        if (StringUtils.isEmpty(name)) {
            errors.rejectValue("name", "server.name.required", "Server name is mandatory");
            return;
        }

        if (server.isNew()) {
            List<Server> servers = serverRepository.findByName(name);
            if (servers.contains(server)) {
                errors.rejectValue(
                        "name",
                        "server.name.unique",
                        new Object[]{name},
                        "A server with name '" + name + "' already exists"
                );
                return;
            }
        }

        if (name.length() > 45) {
            errors.rejectValue("name", "server.name.length", "The name of this server is too long");
            return;
        }
    }

    private void validateServerDescription(Errors errors, Server server) {
        String description = server.getDescription();
        if (StringUtils.isNotEmpty(description) && description.length() > 100) {
            errors.rejectValue(
                    "description",
                    "server.description.length",
                    "The description of this server is too long"
            );
        }
    }


}
