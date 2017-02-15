package be.cegeka.serverviewer.deployments.middleware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MiddlewareConverter implements Converter<String, Middleware> {

    @Autowired
    private MiddlewareRepository middlewareRepository;


    @Override
    public Middleware convert(String id) {
        try {
            Long middlewareId = Long.valueOf(id);
            return middlewareRepository.getOne(middlewareId);
        } catch (NumberFormatException e){
            return null;
        }
    }
}
