package be.cegeka.serverviewer.servers.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ServerConverter implements Converter<String, Server> {

    @Autowired
    private ServerRepository serverRepository;


    @Override
    public Server convert(String id) {
        try {
            Long serverId = Long.valueOf(id);
            return serverRepository.getOne(serverId);
        } catch (NumberFormatException e){
            return null;
        }
    }
}
