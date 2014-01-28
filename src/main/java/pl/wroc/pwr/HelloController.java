package pl.wroc.pwr;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pl.wroc.pwr.service.ImageService;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class HelloController {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ImageService imageService;

    @RequestMapping
    public String printWelcome() {
        imageService.clear();
        return "index";
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public String getFile(MultipartFile file) throws IOException {
        logger.info("received, filename: " + file.getOriginalFilename());
        imageService.addImage(ImageIO.read(file.getInputStream()));
        return "received";
    }

    @RequestMapping(value = "make-hdr", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] makeHdr(@RequestParam Integer algorithm, @RequestParam(required = false) Double alpha) throws IOException {
        logger.info("Making hdr");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(imageService.makeHdr(algorithm, alpha), "jpg", os);
        return os.toByteArray();
    }

    @RequestMapping(value = "uploaded-photo", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getPhoto(@RequestParam Integer photo) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(imageService.get(photo), "jpg", os);
        return os.toByteArray();
    }

    @RequestMapping(value = "amount")
    @ResponseBody
    public String getAmount(){
        return imageService.getAmount().toString();
    }

    @RequestMapping(value = "clear", method = RequestMethod.GET)
    @ResponseBody
    public String clear() throws IOException {
        logger.info("Clear photos");
        imageService.clear();
        return "ok";
    }

}