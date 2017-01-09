package be.cegeka.serverviewer.servers.operatingsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class OperatingSystemController {

    @Autowired
    private OperatingSystemRepository operatingSystemRepository;

    @Autowired
    private OperatingSystemValidator operatingSystemValidator;

    @RequestMapping(value = "/servers/operatingsystem", method = RequestMethod.GET)
    public String getAllOperatingSystems(Model model) {
        model.addAttribute("operatingSystems", operatingSystemRepository.findAll());
        return "servers/operatingsystem/operatingsystem";
    }

    @RequestMapping(value= "/servers/operatingsystem/create", method = RequestMethod.GET)
    public String createOperatingSystemForm(Model model){
        model.addAttribute("operatingSystem", new OperatingSystem());
        return "servers/operatingsystem/createEditOperatingSystem";
    }

    @RequestMapping(value = "/servers/operatingsystem/create", method = RequestMethod.POST)
    public String createOperatingSystem(@ModelAttribute("operatingSystem") OperatingSystem operatingSystem, BindingResult bindingResult) {
        operatingSystemValidator.validate(operatingSystem, bindingResult);
        if (bindingResult.hasFieldErrors()){
            return "servers/operatingSystem/createEditOperatingSystem";
        }
        operatingSystemRepository.save(operatingSystem);
        return "redirect:/servers/operatingsystem";
    }

    @RequestMapping(value = "/servers/operatingsystem/{id}", method = RequestMethod.GET)
    public String editOperatingSystemForm(@PathVariable("id") long id, Model model){
        model.addAttribute("operatingSystem", operatingSystemRepository.findOne(id));
        return "servers/operatingsystem/createEditOperatingSystem";
    }

    @RequestMapping(value = "/servers/operatingsystem/{id}", method = RequestMethod.PUT)
    public String editOperatingSystem(@PathVariable("id") Long id, @ModelAttribute("operatingSystem") OperatingSystem operatingSystemDTO, BindingResult bindingResult){
        OperatingSystem operatingSystem = operatingSystemRepository.findOne(id);
        operatingSystem.setDescription(operatingSystemDTO.getDescription());
        operatingSystemValidator.validate(operatingSystem, bindingResult);
        if (bindingResult.hasFieldErrors()){
            return "servers/operatingsystem/createEditOperatingSystem";
        }
        operatingSystemRepository.save(operatingSystem);
        return "redirect:/servers/operatingsystem";
    }

    @RequestMapping(value = "/servers/operatingsystem/{id}/delete", method = RequestMethod.GET)
    public String deleteOperatingSystem(@PathVariable("id") Long id){
        operatingSystemRepository.delete(id);
        return "redirect:/servers/operatingsystem";
    }

}
