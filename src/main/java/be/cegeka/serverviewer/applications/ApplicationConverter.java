package be.cegeka.serverviewer.applications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConverter implements Converter<String, Application> {

    @Autowired
    private ApplicationRepository applicationRepository;


    @Override
    public Application convert(String id) {
        try {
            Long applicationId = Long.valueOf(id);
            return applicationRepository.getOne(applicationId);
        } catch (NumberFormatException e){
            return null;
        }
    }
}
