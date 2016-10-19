package be.cegeka.serverviewer.servers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Controller
public class EnvironmentController {

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    private EnvironmentValidator environmentValidator;

    @RequestMapping(value= "/", method = RequestMethod.GET)
    public String getHomePage(){
        return "index";
    }

    @RequestMapping(value = "/servers/environment", method = RequestMethod.GET)
    public String getAllEnvironments(Model model) {
        model.addAttribute("environments", environmentRepository.findAll());
        return "environment/environment";
    }

    @RequestMapping(value = "/servers/environment/{id}", method = RequestMethod.GET)
    public String getEnvironment(@PathVariable(value = "id") long id, Model model){
        model.addAttribute("environment", environmentRepository.findOne(id));
        return "environment/view";
    }

    @RequestMapping(value= "/servers/environment/create", method = RequestMethod.GET)
    public String createEnvironmentForm(Model model){
        Environment environment = new Environment();
        model.addAttribute("environment", environment);
        return "environment/createEditEnvironment";
    }

    @RequestMapping(value = "/servers/environment/create", method = RequestMethod.POST)
    public String createEnvironment(@ModelAttribute("environment") Environment environment, BindingResult bindingResult) {
        environmentValidator.validate(environment, bindingResult);
        if (bindingResult.hasFieldErrors()){
            return "environment/createEditEnvironment";
        }
        environmentRepository.save(environment);
        return "redirect:/servers/environment";
    }

}
