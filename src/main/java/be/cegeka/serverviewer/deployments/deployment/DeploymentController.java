package be.cegeka.serverviewer.deployments.deployment;

import be.cegeka.serverviewer.applications.ApplicationRepository;
import be.cegeka.serverviewer.deployments.middleware.MiddlewareRepository;
import be.cegeka.serverviewer.servers.server.ServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DeploymentController {

    @Autowired
    private DeploymentRepository deploymentRepository;

    @Autowired
    private DeploymentValidator deploymentValidator;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private MiddlewareRepository middlewareRepository;

    @RequestMapping(value = "/deployments/deployment", method = RequestMethod.GET)
    public String getAllDeployments(Model model) {
        model.addAttribute("deployments", deploymentRepository.findAll());
        addDependenciesAttributes(model);
        return "deployments/deployment/deployment";
    }

    @RequestMapping(value= "/deployments/deployment/create", method = RequestMethod.GET)
    public String createDeploymentForm(Model model){
        model.addAttribute("deployment", new Deployment());
        return "deployments/deployment/createEditDeployment";
    }

    @RequestMapping(value = "/deployments/deployment/create", method = RequestMethod.POST)
    public String createDeployment(@ModelAttribute("deployment") Deployment deployment, BindingResult bindingResult) {
        deploymentValidator.validate(deployment, bindingResult);
        if (bindingResult.hasFieldErrors()){
            return "deployments/deployment/createEditDeployment";
        }
        deploymentRepository.save(deployment);
        return "redirect:/deployments/deployment";
    }

    @RequestMapping(value = "/deployments/deployment/{id}", method = RequestMethod.GET)
    public String editDeploymentForm(@PathVariable("id") long id, Model model){
        model.addAttribute("deployment", deploymentRepository.findOne(id));
        addDependenciesAttributes(model);
        return "deployments/deployment/createEditDeployment";
    }

    @RequestMapping(value = "/deployments/deployment/{id}", method = RequestMethod.PUT)
    public String editDeployment(@PathVariable("id") Long id, @ModelAttribute("deployment") Deployment deploymentDTO, BindingResult bindingResult){
        Deployment deployment = deploymentRepository.findOne(id);
        deployment.setApplication(deploymentDTO.getApplication());
        deployment.setServer(deploymentDTO.getServer());
        deployment.setMiddleware(deploymentDTO.getMiddleware());
        deployment.setDockerized(deploymentDTO.isDockerized());
        deployment.setApplicationUrl(deploymentDTO.getApplicationUrl());
        deploymentValidator.validate(deployment, bindingResult);
        if (bindingResult.hasFieldErrors()){
            return "deployments/deployment/createEditDeployment";
        }
        deploymentRepository.save(deployment);
        return "redirect:/deployments/deployment";
    }

    @RequestMapping(value = "/deployments/deployment/{id}/delete", method = RequestMethod.GET)
    public String deleteDeployment(@PathVariable("id") Long id){
        deploymentRepository.delete(id);
        return "redirect:/deployments/deployment";
    }

    private void addDependenciesAttributes(Model model) {
        model.addAttribute("applications", applicationRepository.findAll());
        model.addAttribute("servers", serverRepository.findAll());
        model.addAttribute("middlewares", middlewareRepository.findAll());
    }

}
