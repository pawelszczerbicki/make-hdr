package pl.wroc.pwr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pawel on 15.12.13.
 */
@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ImageService {

    @Autowired
    private HdrService hdrService;

    private List<BufferedImage> images = new ArrayList<>();

    public void addImage(BufferedImage i) {
        images.add(i);
    }

    public BufferedImage makeHdr(Integer algorithm){
        if(algorithm == 1){
            return hdrService.hdrSimpleAverage(images.get(0), images.get(1), images.get(2));
        }
        else{
            return hdrService.hdrCuriousAverage(images.get(0), images.get(1), images.get(2));
        }
    }

    public void clear() {
        images.clear();
    }
}
