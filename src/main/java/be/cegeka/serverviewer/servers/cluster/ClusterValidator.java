package be.cegeka.serverviewer.servers.cluster;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class ClusterValidator implements Validator {

    @Autowired
    private ClusterRepository clusterRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Cluster.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Cluster cluster = (Cluster) target;

        validateClusterName(errors, cluster);
        validateClusterDescription(errors, cluster);
    }

    private void validateClusterName(Errors errors, Cluster cluster) {
        String name = cluster.getName();
        if (StringUtils.isEmpty(name)) {
            errors.rejectValue("name", "cluster.name.required", "Cluster name is mandatory");
            return;
        }

        if (cluster.isNew()) {
            List<Cluster> clusters = clusterRepository.findByName(name);
            if (clusters.contains(cluster)) {
                errors.rejectValue(
                        "name",
                        "cluster.name.unique",
                        new Object[]{name},
                        "A cluster with name '" + name + "' already exists"
                );
                return;
            }
        }

        if (name.length() > 45) {
            errors.rejectValue("name", "cluster.name.length", "The name of this cluster is too long");
            return;
        }
    }

    private void validateClusterDescription(Errors errors, Cluster cluster) {
        String description = cluster.getDescription();
        if (StringUtils.isNotEmpty(description) && description.length() > 100) {
            errors.rejectValue(
                    "description",
                    "cluster.description.length",
                    "The description of this cluster is too long"
            );
        }
    }


}
