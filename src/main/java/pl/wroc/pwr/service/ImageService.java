package pl.wroc.pwr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pawel on 15.12.13.
 */
@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ImageService {

    private final static Double DEFAULT_ALPHA_PARAM = 0.2d;

    @Autowired
    private HdrService hdrService;

    private List<BufferedImage> images = new ArrayList<>();

    public void addImage(BufferedImage i) {
        images.add(i);
    }

    public BufferedImage makeHdr(Integer algorithm, Double alphaParam) {
        if (algorithm.equals(1)) {
            return hdrService.averageExtended(images);
        } else if (algorithm.equals(2)) {
            return hdrService.luminanceAlgorithm(images, alphaParam == null || alphaParam.equals(0) ? DEFAULT_ALPHA_PARAM : alphaParam);
        } else {
            return hdrService.thirdAlgorithm(images);
        }
    }

    public void clear() {
        images.clear();
    }

    public RenderedImage get(Integer photo) {
        return images.get(photo);
    }

    public Integer getAmount() {
        return images.size();
    }
}
