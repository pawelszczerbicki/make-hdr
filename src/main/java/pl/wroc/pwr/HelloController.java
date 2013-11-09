package pl.wroc.pwr;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HelloController {

    private final Logger logger = Logger.getLogger(getClass());

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(Model model) {
        logger.info("index page");
        model.addAttribute("message", "Hello world!");
        return "index";
    }

    @RequestMapping(value="anotherindex", method = RequestMethod.GET)
    public String differentIndex(Model model) {
        logger.info("inny index");
        model.addAttribute("message", "Hello world! - Inny index!!");
        return "innyindex";
    }
}