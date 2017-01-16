package be.cegeka.serverviewer.servers.server;

import be.cegeka.serverviewer.servers.servertype.ServerType;
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

        validateServerType(errors, server);
        validateServerName(errors, server);
        validateServerCode(errors, server);
        validateServerHostname(errors, server);
        validateServerDescription(errors, server);
    }

    private void validateServerType(Errors errors, Server server) {
        ServerType serverType = server.getServerType();
        if (serverType == null) {
            errors.rejectValue("serverType", "server.type.required", "Server type is mandatory");
        }
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
        }
    }

    private void validateServerCode(Errors errors, Server server) {
        String code = server.getCode();
        if (StringUtils.isEmpty(code)) {
            errors.rejectValue("code", "server.code.required", "Server code is mandatory");
            return;
        }

        if (server.isNew()) {
            List<Server> servers = serverRepository.findByCode(code);
            if (servers.contains(server)) {
                errors.rejectValue(
                        "code",
                        "server.code.unique",
                        new Object[]{code},
                        "A server with code '" + code + "' already exists"
                );
                return;
            }
        }

        if (code.length() > 45) {
            errors.rejectValue("code", "server.code.length", "The code of this server is too long");
        }
    }

    private void validateServerHostname(Errors errors, Server server) {
        String hostname = server.getHostname();
        if (StringUtils.isEmpty(hostname)) {
            errors.rejectValue("hostname", "server.hostname.required", "Server hostname is mandatory");
            return;
        }

        if (server.isNew()) {
            List<Server> servers = serverRepository.findByHostname(hostname);
            if (servers.contains(server)) {
                errors.rejectValue(
                        "hostname",
                        "server.hostname.unique",
                        new Object[]{hostname},
                        "A server with hostname '" + hostname + "' already exists"
                );
                return;
            }
        }

        if (hostname.length() > 45) {
            errors.rejectValue("hostname", "server.hostname.length", "The hostname of this server is too long");
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
