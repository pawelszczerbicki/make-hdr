package pl.wroc.pwr;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pl.wroc.pwr.willBeRemoved.HDRalgorithm;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    @RequestMapping(value = "anotherindex", method = RequestMethod.GET)
    public String differentIndex(Model model) {
        logger.info("inny index");
        model.addAttribute("message", "Hello world! - Inny index!!");
        return "innyindex";
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getFile(MultipartFile im1, MultipartFile im2,MultipartFile im3) throws IOException {
        BufferedImage image1 = ImageIO.read(im1.getInputStream());
        BufferedImage image2 = ImageIO.read(im2.getInputStream());
        BufferedImage image3 = ImageIO.read(im3.getInputStream());
        logger.info("iamge width : " + image1.getWidth(null));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(new HDRalgorithm().HDR(image1, image2, image3), "jpg",os);
        return os.toByteArray();
    }
}