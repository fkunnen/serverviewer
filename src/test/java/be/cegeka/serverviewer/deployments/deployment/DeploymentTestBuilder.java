package be.cegeka.serverviewer.deployments.deployment;


import be.cegeka.serverviewer.applications.Application;
import be.cegeka.serverviewer.deployments.middleware.Middleware;
import be.cegeka.serverviewer.servers.server.Server;
import be.cegeka.serverviewer.servers.server.ServerTestBuilder;

public class DeploymentTestBuilder {

    private static final Application APPLICATION = new Application("B2B");
    private static final Server SERVER = new ServerTestBuilder().build();
    private static final Middleware MIDDLEWARE = new Middleware("Weblogic 12c");
    private static final String APPLICATION_URL = "DeploymentTestBuilder";

    private Application application = APPLICATION;
    private Server server = SERVER;
    private Middleware middleware = MIDDLEWARE;
    private boolean dockerized = false;
    private String applicationUrl = APPLICATION_URL;

    public DeploymentTestBuilder() {
    }

    public Deployment build(){
        Deployment deployment = new Deployment(application, server);
        deployment.setMiddleware(middleware);
        //deployment.setDockerized(dockerized);
        deployment.setApplicationUrl(applicationUrl);
        return deployment;
    }

    public DeploymentTestBuilder withApplication(Application application) {
        this.application = application;
        return this;
    }

    public DeploymentTestBuilder withServer(Server server) {
        this.server = server;
        return this;
    }

    public DeploymentTestBuilder withMiddleware(Middleware middleware) {
        this.middleware = middleware;
        return this;
    }

    public DeploymentTestBuilder withDockerized(boolean dockerized) {
        this.dockerized = dockerized;
        return this;
    }

    public DeploymentTestBuilder withApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
        return this;
    }

}
