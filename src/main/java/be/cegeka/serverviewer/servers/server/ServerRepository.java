package be.cegeka.serverviewer.servers.server;

import be.cegeka.serverviewer.servers.servertype.ServerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {

    List<Server> findByName(String name);

}
