package be.cegeka.serverviewer.servers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvironmentRepository extends JpaRepository<Environment, Long> {

    List<Environment> findByName(String name);

}
