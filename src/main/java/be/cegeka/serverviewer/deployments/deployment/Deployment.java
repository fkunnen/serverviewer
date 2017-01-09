package be.cegeka.serverviewer.deployments.deployment;

import be.cegeka.serverviewer.applications.Application;
import be.cegeka.serverviewer.deployments.middleware.Middleware;
import be.cegeka.serverviewer.servers.server.Server;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "deployment")
public class Deployment implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Application application;

    @ManyToOne
    private Server server;

    @Column
    private Middleware middleware;

    @Column
    private boolean runsInDocker;

    @Column
    private String applicationUrl;

    public Deployment(){}

    public Deployment(Application application, Server server){
        this.application = application;
        this.server = server;
    }

    public Long getId() {
        return id;
    }

    @Override
    @Transient
    public boolean isNew() {
        return id == null;
    }

    @Override
    public String toString() {
        return application.getName() + " - " + server.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deployment that = (Deployment) o;

        if (!application.equals(that.application)) return false;
        return server.equals(that.server);
    }

    @Override
    public int hashCode() {
        int result = application.hashCode();
        result = 31 * result + server.hashCode();
        return result;
    }
}
