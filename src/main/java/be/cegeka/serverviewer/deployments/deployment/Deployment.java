package be.cegeka.serverviewer.deployments.deployment;

import be.cegeka.serverviewer.applications.Application;
import be.cegeka.serverviewer.deployments.middleware.Middleware;
import be.cegeka.serverviewer.servers.server.Server;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity
@Table(name = "deployment")
public class Deployment implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "application_id")
    private Application application;

    @ManyToOne
    @JoinColumn(name = "server_id")
    private Server server;

    @ManyToOne
    @JoinColumn(name = "middleware_id")
    private Middleware middleware;

    @Column(name = "runs_in_docker")
    private boolean dockerized;

    @Column(name = "application_url")
    private String applicationUrl;

    public Deployment(){}

    public Deployment(Application application, Server server){
        this.application = application;
        this.server = server;
    }

    public Long getId() {
        return id;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Middleware getMiddleware() {
        return middleware;
    }

    public void setMiddleware(Middleware middleware) {
        this.middleware = middleware;
    }

    public boolean isDockerized() {
        return dockerized;
    }

    public void setDockerized(boolean dockerized) {
        this.dockerized = dockerized;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    @Override
    @Transient
    public boolean isNew() {
        return id == null;
    }

    @Override
    public String toString() {
        if (application != null & server != null) {
            return application.getName() + " - " + server.getName();
        }
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deployment that = (Deployment) o;

        if (application != null ? !application.equals(that.application) : that.application != null) return false;
        return server != null ? server.equals(that.server) : that.server == null;
    }

    @Override
    public int hashCode() {
        int result = application != null ? application.hashCode() : 0;
        result = 31 * result + (server != null ? server.hashCode() : 0);
        return result;
    }
}
