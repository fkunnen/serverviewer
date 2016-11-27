package be.cegeka.serverviewer.servers.server;

import be.cegeka.serverviewer.servers.cluster.Cluster;
import be.cegeka.serverviewer.servers.environment.Environment;
import be.cegeka.serverviewer.servers.location.Location;
import be.cegeka.serverviewer.servers.operatingsystem.OperatingSystem;
import be.cegeka.serverviewer.servers.servertype.ServerType;

public class ServerTestBuilder {

    private static final String SERVER_NAME = "B2B PRD 1";
    private static final ServerType SERVER_TYPE_NAME = new ServerType("Virtual Server");
    private static final String CODE = "HI08553";
    private static final String HOSTNAME = "SVRSVZPB2BAPP01";
    private static final String DESCRIPTION = "B2B PRD Weblogic Managed server 1";
    private static final Location LOCATION = new Location("CGK.Leuven");
    private static final Environment ENVIRONMENT = new Environment("PRD");
    private static final OperatingSystem OPERATING_SYSTEM = new OperatingSystem("RHEL 5 (64 bit)");

    private String name = SERVER_NAME;
    private ServerType serverType = SERVER_TYPE_NAME;
    private String code = CODE;
    private String hostname = HOSTNAME;
    private String description = DESCRIPTION;
    private Location location = LOCATION;
    private Environment environment = ENVIRONMENT;
    private OperatingSystem operatingSystem = OPERATING_SYSTEM;
    private Cluster cluster;

    public ServerTestBuilder() {
    }

    public Server build(){
        Server server = new Server();
        server.setName(name);
        server.setServerType(serverType);
        server.setCode(code);
        server.setHostname(hostname);
        server.setDescription(description);
        server.setLocation(location);
        server.setEnvironment(environment);
        server.setOperatingSystem(operatingSystem);
        server.setCluster(cluster);

        return server;
    }

    public ServerTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ServerTestBuilder withServerType(ServerType serverType) {
        this.serverType = serverType;
        return this;
    }

    public ServerTestBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public ServerTestBuilder withHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public ServerTestBuilder withDescription(String description) {
        this.description = description;
        return this;

    }

    public ServerTestBuilder withLocation(Location location) {
        this.location = location;
        return this;
    }

    public ServerTestBuilder withEnvironment(Environment environment) {
        this.environment = environment;
        return this;
    }

    public ServerTestBuilder withOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
        return this;
    }

    public ServerTestBuilder withCluster(Cluster cluster) {
        this.cluster = cluster;
        return this;
    }
}
