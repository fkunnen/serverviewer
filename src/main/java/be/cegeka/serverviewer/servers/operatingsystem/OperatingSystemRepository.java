package be.cegeka.serverviewer.servers.operatingsystem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperatingSystemRepository extends JpaRepository<OperatingSystem, Long> {

    List<OperatingSystem> findByName(String name);

}
