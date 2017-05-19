package be.cegeka.serverviewer.deployments.deployment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class DeploymentConverter implements Converter<String, Deployment> {

    @Autowired
    private DeploymentRepository deploymentRepository;

    @Override
    public Deployment convert(String id) {
        try {
            Long deploymentId = Long.valueOf(id);
            return deploymentRepository.getOne(deploymentId);
        } catch (NumberFormatException e){
            return null;
        }
    }
}
