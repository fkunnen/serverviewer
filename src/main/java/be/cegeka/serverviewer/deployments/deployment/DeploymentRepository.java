package be.cegeka.serverviewer.deployments.deployment;

import be.cegeka.serverviewer.applications.Application;
import be.cegeka.serverviewer.servers.server.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeploymentRepository extends JpaRepository<Deployment, Long> {

    List<Deployment> findByApplicationAndServer(Application application, Server server);

}
