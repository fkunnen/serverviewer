package be.cegeka.serverviewer.applications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationValidator applicationValidator;

    @RequestMapping(value = "/applications", method = RequestMethod.GET)
    public String getAllApplications(Model model) {
        model.addAttribute("applications", applicationRepository.findAll());
        return "applications/application";
    }

    @RequestMapping(value= "/applications/create", method = RequestMethod.GET)
    public String createApplicationForm(Model model){
        model.addAttribute("app", new Application());
        return "applications/createEditApplication";
    }

    @RequestMapping(value = "/applications/create", method = RequestMethod.POST)
    public String createApplication(@ModelAttribute("app") Application application, BindingResult bindingResult) {
        applicationValidator.validate(application, bindingResult);
        if (bindingResult.hasFieldErrors()){
            return "applications/createEditApplication";
        }
        applicationRepository.save(application);
        return "redirect:/applications";
    }

    @RequestMapping(value = "/applications/{id}", method = RequestMethod.GET)
    public String editApplicationForm(@PathVariable("id") long id, Model model){
        model.addAttribute("app", applicationRepository.findOne(id));
        return "applications/createEditApplication";
    }

    @RequestMapping(value = "/applications/{id}", method = RequestMethod.PUT)
    public String editApplication(@PathVariable("id") Long id, @ModelAttribute("app") Application applicationDTO, BindingResult bindingResult){
        Application application = applicationRepository.findOne(id);
        application.setDescription(applicationDTO.getDescription());
        applicationValidator.validate(application, bindingResult);
        if (bindingResult.hasFieldErrors()){
            return "applications/createEditApplication";
        }
        applicationRepository.save(application);
        return "redirect:/applications";
    }

    @RequestMapping(value = "/applications/{id}/delete", method = RequestMethod.GET)
    public String deleteApplication(@PathVariable("id") Long id){
        applicationRepository.delete(id);
        return "redirect:/applications";
    }

}
