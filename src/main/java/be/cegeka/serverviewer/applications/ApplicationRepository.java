package be.cegeka.serverviewer.applications;

import be.cegeka.serverviewer.servers.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByName(String name);

}
