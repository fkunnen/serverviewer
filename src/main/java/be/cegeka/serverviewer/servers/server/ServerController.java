package be.cegeka.serverviewer.servers.server;

import be.cegeka.serverviewer.servers.cluster.ClusterRepository;
import be.cegeka.serverviewer.servers.environment.EnvironmentRepository;
import be.cegeka.serverviewer.servers.location.LocationRepository;
import be.cegeka.serverviewer.servers.operatingsystem.OperatingSystemRepository;
import be.cegeka.serverviewer.servers.servertype.ServerTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ServerController {

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private ServerValidator serverValidator;

    @Autowired
    private ServerTypeRepository serverTypeRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    private OperatingSystemRepository operatingSystemRepository;

    @Autowired
    private ClusterRepository clusterRepository;

    @RequestMapping(value = "/servers/server", method = RequestMethod.GET)
    public String getAllServers(Model model) {
        model.addAttribute("servers", serverRepository.findAll());
        return "servers/server/server";
    }

    @RequestMapping(value= "/servers/server/create", method = RequestMethod.GET)
    public String createServerForm(Model model){
        model.addAttribute("server", new Server());
        addDependenciesAttributes(model);
        return "servers/server/createEditServer";
    }

    @RequestMapping(value = "/servers/server/create", method = RequestMethod.POST)
    public String createServer(@ModelAttribute("server") Server server, BindingResult bindingResult) {
        serverValidator.validate(server, bindingResult);
        if (bindingResult.hasFieldErrors()){
            return "servers/server/createEditServer";
        }
        serverRepository.save(server);
        return "redirect:/servers/server";
    }

    @RequestMapping(value = "/servers/server/{id}", method = RequestMethod.GET)
    public String editServerForm(@PathVariable("id") long id, Model model){
        model.addAttribute("server", serverRepository.findOne(id));
        addDependenciesAttributes(model);
        return "servers/server/createEditServer";
    }

    @RequestMapping(value = "/servers/server/{id}", method = RequestMethod.PUT)
    public String editServer(@PathVariable("id") Long id, @ModelAttribute("server") Server serverDTO, BindingResult bindingResult){
        Server server = serverRepository.findOne(id);
        server.setServerType(serverDTO.getServerType());
        server.setCode(serverDTO.getCode());
        server.setHostname(serverDTO.getHostname());
        server.setDescription(serverDTO.getDescription());
        server.setLocation(serverDTO.getLocation());
        server.setOperatingSystem(serverDTO.getOperatingSystem());
        server.setEnvironment(serverDTO.getEnvironment());
        server.setCluster(serverDTO.getCluster());
        serverValidator.validate(server, bindingResult);
        if (bindingResult.hasFieldErrors()){
            return "servers/server/createEditServer";
        }
        serverRepository.save(server);
        return "redirect:/servers/server";
    }

    @RequestMapping(value = "/servers/server/{id}/delete", method = RequestMethod.GET)
    public String deleteServer(@PathVariable("id") Long id){
        serverRepository.delete(id);
        return "redirect:/servers/server";
    }

    private void addDependenciesAttributes(Model model) {
        model.addAttribute("serverTypes", serverTypeRepository.findAll());
        model.addAttribute("locations", locationRepository.findAll());
        model.addAttribute("environments", environmentRepository.findAll());
        model.addAttribute("operatingSystems", operatingSystemRepository.findAll());
        model.addAttribute("cluster", clusterRepository.findAll());
    }

}
