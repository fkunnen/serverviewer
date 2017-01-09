package be.cegeka.serverviewer.deployments.middleware;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MiddlewareRepository extends JpaRepository<Middleware, Long> {

    List<Middleware> findByName(String name);

}
