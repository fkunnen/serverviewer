package be.cegeka.serverviewer.servers.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LocationConverter implements Converter<String, Location> {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Location convert(String id) {
        try {
            Long locationId = Long.valueOf(id);
            return locationRepository.getOne(locationId);
        } catch (NumberFormatException e){
            return null;
        }
    }
}
