package be.cegeka.serverviewer.servers.servertype;

import be.cegeka.serverviewer.servers.operatingsystem.OperatingSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerTypeRepository extends JpaRepository<ServerType, Long> {

    List<ServerType> findByName(String name);

}
