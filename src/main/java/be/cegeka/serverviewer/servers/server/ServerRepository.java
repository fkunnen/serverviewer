package be.cegeka.serverviewer.servers.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {

    List<Server> findByName(String name);

    List<Server> findByCode(String code);

    List<Server> findByHostname(String hostname);

}
