package be.cegeka.serverviewer.deployments.middleware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MiddlewareController {

    @Autowired
    private MiddlewareRepository middlewareRepository;

    @Autowired
    private MiddlewareValidator middlewareValidator;

    @RequestMapping(value = "/deployments/middleware", method = RequestMethod.GET)
    public String getAllMiddlewares(Model model) {
        model.addAttribute("middlewares", middlewareRepository.findAll());
        return "deployments/middleware/middleware";
    }

    @RequestMapping(value= "/deployments/middleware/create", method = RequestMethod.GET)
    public String createMiddlewareForm(Model model){
        model.addAttribute("middleware", new Middleware());
        return "deployments/middleware/createEditMiddleware";
    }

    @RequestMapping(value = "/deployments/middleware/create", method = RequestMethod.POST)
    public String createMiddleware(@ModelAttribute("middleware") Middleware middleware, BindingResult bindingResult) {
        middlewareValidator.validate(middleware, bindingResult);
        if (bindingResult.hasFieldErrors()){
            return "deployments/middleware/createEditMiddleware";
        }
        middlewareRepository.save(middleware);
        return "redirect:/deployments/middleware";
    }

    @RequestMapping(value = "/deployments/middleware/{id}", method = RequestMethod.GET)
    public String editMiddlewareForm(@PathVariable("id") long id, Model model){
        model.addAttribute("middleware", middlewareRepository.findOne(id));
        return "deployments/middleware/createEditMiddleware";
    }

    @RequestMapping(value = "/deployments/middleware/{id}", method = RequestMethod.PUT)
    public String editMiddleware(@PathVariable("id") Long id, @ModelAttribute("middleware") Middleware middlewareDTO, BindingResult bindingResult){
        Middleware middleware = middlewareRepository.findOne(id);
        middleware.setDescription(middlewareDTO.getDescription());
        middlewareValidator.validate(middleware, bindingResult);
        if (bindingResult.hasFieldErrors()){
            return "deployments/middleware/createEditMiddleware";
        }
        middlewareRepository.save(middleware);
        return "redirect:/deployments/middleware";
    }

    @RequestMapping(value = "/deployments/middleware/{id}/delete", method = RequestMethod.GET)
    public String deleteMiddleware(@PathVariable("id") Long id){
        middlewareRepository.delete(id);
        return "redirect:/deployments/middleware";
    }

}
