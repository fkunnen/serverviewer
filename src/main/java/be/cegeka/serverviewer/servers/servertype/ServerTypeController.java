package be.cegeka.serverviewer.servers.servertype;

import be.cegeka.serverviewer.servers.servertype.ServerType;
import be.cegeka.serverviewer.servers.servertype.ServerTypeRepository;
import be.cegeka.serverviewer.servers.servertype.ServerTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ServerTypeController {

    @Autowired
    private ServerTypeRepository serverTypeRepository;

    @Autowired
    private ServerTypeValidator serverTypeValidator;

    @RequestMapping(value = "/servers/servertype", method = RequestMethod.GET)
    public String getAllServerTypes(Model model) {
        model.addAttribute("serverTypes", serverTypeRepository.findAll());
        return "servertype/servertype";
    }

    @RequestMapping(value= "/servers/servertype/create", method = RequestMethod.GET)
    public String createServerTypeForm(Model model){
        model.addAttribute("serverType", new ServerType());
        return "servertype/createEditServerType";
    }

    @RequestMapping(value = "/servers/servertype/create", method = RequestMethod.POST)
    public String createServerType(@ModelAttribute("serverType") ServerType serverType, BindingResult bindingResult) {
        serverTypeValidator.validate(serverType, bindingResult);
        if (bindingResult.hasFieldErrors()){
            return "serverType/createEditServerType";
        }
        serverTypeRepository.save(serverType);
        return "redirect:/servers/servertype";
    }

    @RequestMapping(value = "/servers/servertype/{id}", method = RequestMethod.GET)
    public String editServerTypeForm(@PathVariable("id") long id, Model model){
        model.addAttribute("serverType", serverTypeRepository.findOne(id));
        return "servertype/createEditServerType";
    }

    @RequestMapping(value = "/servers/servertype/{id}", method = RequestMethod.PUT)
    public String editServerType(@PathVariable("id") Long id, @ModelAttribute("serverType") ServerType serverTypeDTO, BindingResult bindingResult){
        ServerType serverType = serverTypeRepository.findOne(id);
        serverType.setDescription(serverTypeDTO.getDescription());
        serverTypeValidator.validate(serverType, bindingResult);
        if (bindingResult.hasFieldErrors()){
            return "servertype/createEditServerType";
        }
        serverTypeRepository.save(serverType);
        return "redirect:/servers/servertype";
    }

    @RequestMapping(value = "/servers/servertype/{id}/delete", method = RequestMethod.GET)
    public String deleteServerType(@PathVariable("id") Long id){
        serverTypeRepository.delete(id);
        return "redirect:/servers/servertype";
    }

}
