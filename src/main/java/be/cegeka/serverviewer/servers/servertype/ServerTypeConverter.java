package be.cegeka.serverviewer.servers.servertype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ServerTypeConverter implements Converter<String, ServerType> {

    @Autowired
    private ServerTypeRepository serverTypeRepository;


    @Override
    public ServerType convert(String id) {
        try {
            Long serverTypeId = Long.valueOf(id);
            return serverTypeRepository.getOne(serverTypeId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
