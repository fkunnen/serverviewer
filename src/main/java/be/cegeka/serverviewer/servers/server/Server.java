package be.cegeka.serverviewer.servers.server;

import be.cegeka.serverviewer.servers.cluster.Cluster;
import be.cegeka.serverviewer.servers.environment.Environment;
import be.cegeka.serverviewer.servers.location.Location;
import be.cegeka.serverviewer.servers.operatingsystem.OperatingSystem;
import be.cegeka.serverviewer.servers.servertype.ServerType;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity(name = "server")
public class Server implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private ServerType type;

    @Column
    private String name;

    @Column
    private String code;

    @Column
    private String hostname;

    @Column
    private String description;

    @Column
    private Location location;

    @Column
    private Environment environment;

    @Column
    private OperatingSystem operatingSystem;

    @Column
    private Cluster cluster;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    public ServerType getType() {
        return type;
    }

    public void setType(ServerType type) {
        this.type = type;
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
}
