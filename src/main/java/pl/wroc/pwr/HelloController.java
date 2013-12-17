package pl.wroc.pwr;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pl.wroc.pwr.service.ImageService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
@RequestMapping("/")
public class HelloController {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ImageService imageService;

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(Model model) {
        logger.info("index page");
        imageService.clear();
        return "index";
    }

    @RequestMapping(value = "anotherindex", method = RequestMethod.GET)
    public String differentIndex(Model model) {
        logger.info("inny index");
        return "innyindex";
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public String getFile(MultipartFile file) throws IOException {
        logger.info("received, filename: " + file.getOriginalFilename());
        imageService.addImage(ImageIO.read(file.getInputStream()));
        return "received";
    }

    @RequestMapping(value = "make-hdr", method = RequestMethod.GET,  produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] makeHdr(@RequestParam Integer algorithm) throws IOException {
        logger.info("Making hdr");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(imageService.makeHdr(algorithm), "jpg",os);
        return os.toByteArray();
    }

    @RequestMapping(value = "clear", method = RequestMethod.GET)
    @ResponseBody
    public String clear() throws IOException {
        logger.info("Clear photos");
        imageService.clear();
        return "ok";
    }

}