package be.cegeka.serverviewer.servers.server;

import be.cegeka.serverviewer.servers.cluster.Cluster;
import be.cegeka.serverviewer.servers.environment.Environment;
import be.cegeka.serverviewer.servers.location.Location;
import be.cegeka.serverviewer.servers.operatingsystem.OperatingSystem;
import be.cegeka.serverviewer.servers.servertype.ServerType;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "server")
public class Server implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="type_id")
    private ServerType serverType;

    @Column
    private String name;

    @Column
    private String code;

    @Column
    private String hostname;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name="location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name="environment_id")
    private Environment environment;

    @ManyToOne
    @JoinColumn(name="operating_system_id")
    private OperatingSystem operatingSystem;

    @ManyToOne
    @JoinColumn(name="cluster_id")
    private Cluster cluster;

    public Server(){}

    public Server(String name){
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    @Transient
    public boolean isNew() {
        return id == null;
    }

    public ServerType getServerType() {
        return serverType;
    }

    public void setServerType(ServerType serverType) {
        this.serverType = serverType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return Objects.equals(name, server.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
