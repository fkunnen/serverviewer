package be.cegeka.serverviewer.servers.cluster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClusterRepository extends JpaRepository<Cluster, Long> {

    List<Cluster> findByName(String name);

}
