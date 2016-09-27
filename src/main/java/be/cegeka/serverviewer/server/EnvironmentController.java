package be.cegeka.serverviewer.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Controller
public class EnvironmentController {

    @Autowired
    private EnvironmentRepository environmentRepository;

    @RequestMapping(value = "/server/environment", method = RequestMethod.GET)
    public String getAllEnvironments(Model model) {
        model.addAttribute("environments", environmentRepository.findAll());
        return "environment/list";
    }

    @RequestMapping(value = "/server/environment/{id}", method = RequestMethod.GET)
    public String getEnvironment(@PathVariable(value = "id") long id, Model model){
        model.addAttribute("environment", environmentRepository.findOne(id));
        return "environment/view";
    }

    @RequestMapping(value = "/server/environment/create", method = RequestMethod.POST, consumes = APPLICATION_JSON_UTF8_VALUE)
    public String createEnvironment(@RequestBody Environment environment) {
        Environment savedEnvironment = environmentRepository.save(environment);
        return "redirect:/server/environment";
    }

}
