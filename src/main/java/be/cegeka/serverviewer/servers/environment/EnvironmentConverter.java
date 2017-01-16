package be.cegeka.serverviewer.servers.environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentConverter implements Converter<String, Environment> {

    @Autowired
    private EnvironmentRepository environmentRepository;


    @Override
    public Environment convert(String id) {
        try {
            Long environmentId = Long.valueOf(id);
            return environmentRepository.getOne(environmentId);
        } catch (NumberFormatException e){
            return null;
        }
    }
}
