package be.cegeka.serverviewer.deployments.deployment;

import be.cegeka.serverviewer.applications.Application;
import be.cegeka.serverviewer.servers.server.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class DeploymentValidator implements Validator {

    @Autowired
    private DeploymentRepository deploymentRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Deployment.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Deployment deployment = (Deployment) target;

        validateDeploymentApplication(errors, deployment);
        validateDeploymentServer(errors, deployment);
        validateDeploymentApplicationServerUnique(errors, deployment);
        validateApplicationUrl(errors, deployment);
    }


    private void validateDeploymentApplication(Errors errors, Deployment deployment) {
        Application application = deployment.getApplication();
        if (application == null) {
            errors.rejectValue("application", "application.required", "Application is mandatory");
        }
    }

    private void validateDeploymentServer(Errors errors, Deployment deployment) {
        Server server = deployment.getServer();
        if (server == null) {
            errors.rejectValue("server", "server.required", "Server is mandatory");
        }
    }

    private void validateDeploymentApplicationServerUnique(Errors errors, Deployment deployment) {
        Application application = deployment.getApplication();
        Server server = deployment.getServer();
        if (deployment.isNew()) {
            List<Deployment> deployments = deploymentRepository.findByApplicationAndServer(application, server);
            if (deployments.contains(deployment)) {
                errors.rejectValue(
                        "application",
                        "deployment.application.server.unique",
                        new Object[]{application, server},
                        "A deployment with application '" + application.getName() + "' and server '" + server.getName() + "' already exists"
                );
            }
        }
    }

    private void validateApplicationUrl(Errors errors, Deployment deployment) {
        String applicationUrl = deployment.getApplicationUrl();
        if (StringUtils.isNotEmpty(applicationUrl) && applicationUrl.length() > 100) {
            errors.rejectValue(
                    "applicationUrl",
                    "deployment.applicationUrl.length",
                    "The application URL of this deployment is too long"
            );
        }
    }

}
