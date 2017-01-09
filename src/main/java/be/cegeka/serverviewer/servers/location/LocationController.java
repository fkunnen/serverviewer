package be.cegeka.serverviewer.servers.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationValidator locationValidator;

    @RequestMapping(value = "/servers/location", method = RequestMethod.GET)
    public String getAllLocations(Model model) {
        model.addAttribute("locations", locationRepository.findAll());
        return "servers/location/location";
    }

    @RequestMapping(value= "/servers/location/create", method = RequestMethod.GET)
    public String createLocationForm(Model model){
        model.addAttribute("location", new Location());
        return "servers/location/createEditLocation";
    }

    @RequestMapping(value = "/servers/location/create", method = RequestMethod.POST)
    public String createLocation(@ModelAttribute("location") Location location, BindingResult bindingResult) {
        locationValidator.validate(location, bindingResult);
        if (bindingResult.hasFieldErrors()){
            return "servers/location/createEditLocation";
        }
        locationRepository.save(location);
        return "redirect:/servers/location";
    }

    @RequestMapping(value = "/servers/location/{id}", method = RequestMethod.GET)
    public String editLocationForm(@PathVariable("id") long id, Model model){
        model.addAttribute("location", locationRepository.findOne(id));
        return "servers/location/createEditLocation";
    }

    @RequestMapping(value = "/servers/location/{id}", method = RequestMethod.PUT)
    public String editLocation(@PathVariable("id") Long id, @ModelAttribute("location") Location locationDTO, BindingResult bindingResult){
        Location location = locationRepository.findOne(id);
        location.setDescription(locationDTO.getDescription());
        locationValidator.validate(location, bindingResult);
        if (bindingResult.hasFieldErrors()){
            return "servers/location/createEditLocation";
        }
        locationRepository.save(location);
        return "redirect:/servers/location";
    }

    @RequestMapping(value = "/servers/location/{id}/delete", method = RequestMethod.GET)
    public String deleteLocation(@PathVariable("id") Long id){
        locationRepository.delete(id);
        return "redirect:/servers/location";
    }

}
