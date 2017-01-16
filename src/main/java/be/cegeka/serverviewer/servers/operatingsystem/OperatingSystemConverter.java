package be.cegeka.serverviewer.servers.operatingsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OperatingSystemConverter implements Converter<String, OperatingSystem> {

    @Autowired
    private OperatingSystemRepository operatingSystemRepository;

    @Override
    public OperatingSystem convert(String id) {
        try {
            Long operatingSystemId = Long.valueOf(id);
            return operatingSystemRepository.getOne(operatingSystemId);
        } catch (NumberFormatException e){
            return null;
        }
    }
}
