package be.cegeka.serverviewer.servers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ClusterController {

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private ClusterValidator clusterValidator;

    @RequestMapping(value = "/servers/cluster", method = RequestMethod.GET)
    public String getAllClusters(Model model) {
        model.addAttribute("clusters", clusterRepository.findAll());
        return "cluster/cluster";
    }

    @RequestMapping(value= "/servers/cluster/create", method = RequestMethod.GET)
    public String createClusterForm(Model model){
        model.addAttribute("cluster", new Cluster());
        return "cluster/createEditCluster";
    }

    @RequestMapping(value = "/servers/cluster/create", method = RequestMethod.POST)
    public String createCluster(@ModelAttribute("cluster") Cluster cluster, BindingResult bindingResult) {
        clusterValidator.validate(cluster, bindingResult);
        if (bindingResult.hasFieldErrors()){
            return "cluster/createEditCluster";
        }
        clusterRepository.save(cluster);
        return "redirect:/servers/cluster";
    }

    @RequestMapping(value = "/servers/cluster/{id}", method = RequestMethod.GET)
    public String editClusterForm(@PathVariable("id") long id, Model model){
        model.addAttribute("cluster", clusterRepository.findOne(id));
        return "cluster/createEditCluster";
    }

    @RequestMapping(value = "/servers/cluster/{id}", method = RequestMethod.PUT)
    public String editCluster(@PathVariable("id") Long id, @ModelAttribute("cluster") Cluster clusterDTO, BindingResult bindingResult){
        Cluster cluster = clusterRepository.findOne(id);
        cluster.setDescription(clusterDTO.getDescription());
        clusterValidator.validate(cluster, bindingResult);
        if (bindingResult.hasFieldErrors()){
            return "cluster/createEditCluster";
        }
        clusterRepository.save(cluster);
        return "redirect:/servers/cluster";
    }

    @RequestMapping(value = "/servers/cluster/{id}/delete", method = RequestMethod.GET)
    public String deleteCluster(@PathVariable("id") Long id){
        clusterRepository.delete(id);
        return "redirect:/servers/cluster";
    }

}
