package be.cegeka.serverviewer.servers.cluster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClusterConverter implements Converter<String, Cluster> {

    @Autowired
    private ClusterRepository clusterRepository;


    @Override
    public Cluster convert(String id) {
        try {
            Long clusterId = Long.valueOf(id);
            return clusterRepository.getOne(clusterId);
        } catch (NumberFormatException e){
            return null;
        }
    }
}
