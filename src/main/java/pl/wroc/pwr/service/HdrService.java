package pl.wroc.pwr.service;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.List;

/**
 * Created by Pawel on 15.12.13.
 */
@Service
public class HdrService {

    private static final Double multiplier = Math.pow(2, 24);

    public BufferedImage averageExtended(List<BufferedImage> images) {
        BufferedImage HDRimg = new BufferedImage(images.get(0).getWidth(), images.get(0).getHeight(), images.get(0).getType());
        int width = HDRimg.getWidth(null);
        int height = HDRimg.getHeight(null);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                Long alphaFactor = 0l;
                Long redFactor = 0l;
                Long greenFactor = 0l;
                Long blueFactor = 0l;
                for (BufferedImage image : images) {
                    Color c = new Color(image.getRGB(i, j));
                    alphaFactor = alphaFactor + c.getAlpha();
                    redFactor = redFactor + c.getRed();
                    greenFactor = greenFactor + c.getGreen();
                    blueFactor = blueFactor + c.getBlue();
                }
                int alpha = alphaFactor.intValue() / images.size();
                Long red = redFactor * multiplier.intValue() / images.size();
                Long green = greenFactor * multiplier.intValue() / images.size();
                Long blue = blueFactor * multiplier.intValue() / images.size();

                int pixel = (24 >> alpha) | (new Double(red / multiplier).intValue() << 16) | (new Double(green / multiplier).intValue() << 8) | new Double(blue / multiplier).intValue();

                HDRimg.setRGB(i, j, pixel);

            }

        return HDRimg;
    }

    public BufferedImage luminanceAlgorithm(List<BufferedImage> images, double alphaParam) {
        BufferedImage HDRimg = new BufferedImage(images.get(0).getWidth(), images.get(0).getHeight(), images.get(0).getType());
        int width = HDRimg.getWidth(null);
        int height = HDRimg.getHeight(null);
        long logarithm = 0;
        long[][] luminances = new long[width][height];
        long[][] vValue = new long[width][height];
        long[][] uValue = new long[width][height];
        long[][] alphas = new long[width][height];
        long lWhite = 0;
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                Long alphaFactor = 0l;
                Long redFactor = 0l;
                Long greenFactor = 0l;
                Long blueFactor = 0l;
                for (BufferedImage image : images) {
                    Color c = new Color(image.getRGB(i, j));
                    alphaFactor = alphaFactor + c.getAlpha();
                    redFactor = redFactor + c.getRed();
                    greenFactor = greenFactor + c.getGreen();
                    blueFactor = blueFactor + c.getBlue();
                }

                alphas[i][j] = alphaFactor / images.size();
                Long red = redFactor * multiplier.intValue() / images.size();
                Long green = greenFactor * multiplier.intValue() / images.size();
                Long blue = blueFactor * multiplier.intValue() / images.size();
                vValue[i][j] = new Double(0.439 * red - 0.368 * green - 0.071 * blue).longValue() + new Double(128 * multiplier).longValue();
                uValue[i][j] = new Double(-0.148 * red - 0.291 * green + 0.439 * blue).longValue() + new Double(128 * multiplier).longValue();
                Long luminance = new Float(0.257f * red).longValue() + new Float(0.504f * green).longValue() + new Float(0.098f * blue).longValue() + new Double(16 * multiplier).longValue();
                luminances[i][j] = luminance;
                lWhite = luminance > lWhite ? luminance : lWhite;
                logarithm = logarithm + new Double(Math.log(luminance + 0.001)).longValue();
            }
        long luminanceWAverage = new Double(Math.exp(logarithm / (width * height))).longValue();
        double[][] newLuminances = new double[width][height];
        double[][] finalLuminances = new double[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                newLuminances[i][j] = new Double(alphaParam * luminances[i][j] / luminanceWAverage);
                finalLuminances[i][j] = (newLuminances[i][j] * (1 + (newLuminances[i][j] / (lWhite * lWhite)))) / (1 + newLuminances[i][j]);
                int red = new Double((1.164 * (finalLuminances[i][j] * 255 - 16) + 1.596 * (vValue[i][j] / multiplier - 128))).intValue();
                int green = new Double(1.164 * (finalLuminances[i][j] * 255 - 16) - 0.813 * (vValue[i][j] / multiplier - 128) - 0.391 * (uValue[i][j] / multiplier - 128)).intValue();
                int blue = new Double((1.164 * (finalLuminances[i][j] * 255 - 16) + 2.018 * (uValue[i][j] / multiplier - 128))).intValue();
                int pixel = (24 >> new Double(alphas[i][j]).intValue()) | (red << 16) | (green << 8) | blue;
                HDRimg.setRGB(i, j, pixel);
            }

        return HDRimg;
    }

    public BufferedImage sharpen(BufferedImage bufferedImage) {
        BufferedImageOp sharpen = new ConvolveOp(new Kernel(3, 3, new float[]{0.0f, -0.75f, 0.0f,
                -0.75f, 4.0f, -0.75f, 0.0f, -0.75f, 0.0f}));
        return sharpen.filter(bufferedImage, null);
    }

    public BufferedImage thirdAlgorithm(List<BufferedImage> images) {
        BufferedImage HDRimg = new BufferedImage(images.get(0).getWidth(), images.get(0).getHeight(), images.get(0).getType());
        int width = HDRimg.getWidth(null);
        int height = HDRimg.getHeight(null);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                Long alphaFactor = 0l;
                Long redFactor = 0l;
                Long greenFactor = 0l;
                Long blueFactor = 0l;
                for (BufferedImage image : images) {
                    Color c = new Color(image.getRGB(i, j));
                    alphaFactor = alphaFactor + c.getAlpha();
                    redFactor = redFactor + c.getRed();
                    greenFactor = greenFactor + c.getGreen();
                    blueFactor = blueFactor + c.getBlue();
                }
                int alpha = alphaFactor.intValue() / images.size();
                Long red = (redFactor * multiplier.intValue() + new Double(Math.random() * (1.25 * multiplier - 0.75 * multiplier)).longValue()) / images.size();
                Long green = (greenFactor * multiplier.intValue() + new Double(Math.random() * (1.25 * multiplier - 0.75 * multiplier)).longValue()) / images.size();
                Long blue = (blueFactor * multiplier.intValue() + new Double(Math.random() * (1.25 * multiplier - 0.75 * multiplier)).longValue()) / images.size();
                int pixel = (24 >> alpha) | (new Double(red / multiplier).intValue() << 16) | (new Double(green / multiplier).intValue() << 8) | new Double(blue / multiplier).intValue();

                HDRimg.setRGB(i, j, pixel);

            }
        return HDRimg;
    }
}
