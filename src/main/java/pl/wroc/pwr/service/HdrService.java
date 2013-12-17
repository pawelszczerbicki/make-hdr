package pl.wroc.pwr.service;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;

/**
 * Created by Pawel on 15.12.13.
 */
@Service
public class HdrService {

    public BufferedImage hdrSimpleAverage(BufferedImage im1, BufferedImage im2, BufferedImage im3) {
        BufferedImage HDRimg = new BufferedImage(im2.getWidth(), im2.getHeight(), im2.getType());
        int width = HDRimg.getWidth(null);
        int height = HDRimg.getHeight(null);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                Color c1 = new Color(im1.getRGB(i, j));
                Color c2 = new Color(im2.getRGB(i, j));
                Color c3 = new Color(im3.getRGB(i, j));

                int alpha = (c1.getAlpha() + c2.getAlpha() + c3.getAlpha()) / 3;
                int red = (c1.getRed() + c2.getRed() + c3.getRed()) / 3;
                int green = (c1.getGreen() + c2.getGreen() + c3.getGreen()) / 3;
                int blue = (c1.getBlue() + c2.getBlue() + c3.getBlue()) / 3;
                int pixel = (24 >> alpha) | (red << 16) | (green << 8) | blue;

                HDRimg.setRGB(i, j, pixel);

            }
        return HDRimg;
    }

    public BufferedImage hdrCuriousAverage(BufferedImage im1, BufferedImage im2, BufferedImage im3) {
        BufferedImage hdr = new BufferedImage(im2.getWidth(), im2.getHeight(), im2.getType());
        int width = hdr.getWidth(null);
        int height = hdr.getHeight(null);
        WritableRaster raster = hdr.getRaster();
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                int[] pixels = raster.getPixel(i, j, (int[]) null);

                Color c1 = new Color(im1.getRGB(i, j));
                Color c2 = new Color(im2.getRGB(i, j));
                Color c3 = new Color(im3.getRGB(i, j));
                int rmax = Math.max(c1.getRed(), c2.getRed());
                int gmax = Math.max(c1.getGreen(), c2.getGreen());
                int bmax = Math.max(c1.getBlue(), c2.getBlue());
                int rmin = Math.min(c1.getRed(), c2.getRed());
                int gmin = Math.min(c1.getGreen(), c2.getGreen());
                int bmin = Math.min(c1.getBlue(), c2.getBlue());
                int howManyMax = 0;
                howManyMax = (c1.getRed() + c2.getRed() + c3.getRed()) / 3 < 125 ? howManyMax : howManyMax + 1;
                howManyMax = (c1.getGreen() + c2.getGreen() + c3.getGreen()) / 3 < 125 ? howManyMax : howManyMax + 1;
                howManyMax = (c1.getBlue() + c2.getBlue() + c3.getBlue()) / 3 < 125 ? howManyMax : howManyMax + 1;
                if (howManyMax <= 2) {
                    pixels[0] = Math.max(c3.getRed(), rmax);
                    pixels[1] = Math.max(c3.getGreen(), gmax);
                    pixels[2] = Math.max(c3.getBlue(), bmax);
                } else {
                    pixels[0] = Math.min(c3.getRed(), rmin);
                    pixels[1] = Math.min(c3.getGreen(), gmin);
                    pixels[2] = Math.min(c3.getBlue(), bmin);
                }
                raster.setPixel(i, j, pixels);
            }
        return hdr;
    }

    public BufferedImage hdrBestAlgorithm(BufferedImage im1, BufferedImage im2, BufferedImage im3) {

        BufferedImage HDRimg = new BufferedImage(im2.getWidth(), im2.getHeight(), im2.getType());
        int width = HDRimg.getWidth(null);
        int height = HDRimg.getHeight(null);
        WritableRaster raster = HDRimg.getRaster();
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                int[] pixels = raster.getPixel(i, j, (int[]) null);

                Color c1 = new Color(im1.getRGB(i, j));
                Color c2 = new Color(im2.getRGB(i, j));
                Color c3 = new Color(im3.getRGB(i, j));
                int rmax = Math.max(c1.getRed(), c2.getRed());
                int gmax = Math.max(c1.getGreen(), c2.getGreen());
                int bmax = Math.max(c1.getBlue(), c2.getBlue());
//
                int rmin = Math.min(c1.getRed(), c2.getRed());
                int gmin = Math.min(c1.getGreen(), c2.getGreen());
                int bmin = Math.min(c1.getBlue(), c2.getBlue());
                int howManyMax = 0;
                howManyMax = (c1.getRed() + c2.getRed() + c3.getRed()) / 3 < 125 ? howManyMax : howManyMax + 1;
                howManyMax = (c1.getGreen() + c2.getGreen() + c3.getGreen()) / 3 < 125 ? howManyMax : howManyMax + 1;
                howManyMax = (c1.getBlue() + c2.getBlue() + c3.getBlue()) / 3 < 125 ? howManyMax : howManyMax + 1;
                if (howManyMax <= 2) {
                    pixels[0] = Math.max(c3.getRed(), rmax);
                    pixels[1] = Math.max(c3.getGreen(), gmax);
                    pixels[2] = Math.max(c3.getBlue(), bmax);
                } else {
                    pixels[0] = Math.min(c3.getRed(), rmin);
                    pixels[1] = Math.min(c3.getGreen(), gmin);
                    pixels[2] = Math.min(c3.getBlue(), bmin);
                }

//                 int luminance1 = new Double(0.257*c1.getRed()+0.504*c1.getGreen()+0.098*c1.getBlue()).intValue() + 16;
//                 int luminance2 = new Double(0.257*c2.getRed()+0.504*c2.getGreen()+0.098*c2.getBlue()).intValue() + 16;
//                 int luminance3 = new Double(0.257*c3.getRed()+0.504*c3.getGreen()+0.098*c3.getBlue()).intValue() + 16;
//
//                int v1 = new Double(0.439*c1.getRed()-0.368*c1.getGreen()-0.071*c1.getBlue()).intValue() + 128;
//                int v2 = new Double(0.439*c2.getRed()-0.368*c2.getGreen()-0.071*c2.getBlue()).intValue() + 128;
//                int v3 = new Double(0.439*c3.getRed()-0.368*c3.getGreen()-0.071*c3.getBlue()).intValue() + 128;
//
//                int u1 = new Double(-0.148*c1.getRed()-0.291*c1.getGreen()+0.439*c1.getBlue()).intValue() + 128;
//                int u2 = new Double(-0.148*c2.getRed()-0.291*c2.getGreen()+0.439*c2.getBlue()).intValue() + 128;
//                int u3 = new Double(-0.148*c3.getRed()-0.291*c3.getGreen()+0.439*c3.getBlue()).intValue() + 128;
//
//                int luminanceAvg = new Double((luminance1+luminance2+luminance3)/3).intValue();
//                int vAvg = new Double((v1+v2+v3)/3).intValue();
//                int uAvg = new Double((u1+u2+u3)/3).intValue();
//
//                pixels[0] = new Double(1.164*(luminanceAvg - 16) + 1.596*(vAvg - 128)).intValue();
//                 pixels[1] = new Double(1.164*(luminanceAvg - 16) - 0.813*(vAvg - 128) - 0.391*(uAvg - 128)).intValue();
//                  pixels[2] = new Double(1.164*(luminanceAvg - 16) + 2.018*(uAvg - 128)).intValue();
//
//                 pixels[0] = (c1.getRed() + c2.getRed() + c3.getRed())/3 < 125 ? Math.max(c3.getRed(), rmax): Math.min(c3.getRed(), rmin);
//                 pixels[1] = (c1.getGreen() + c2.getGreen() + c3.getGreen())/3 < 125 ? Math.max(c3.getGreen(), gmax): Math.min(c3.getGreen(), gmin);
//                 pixels[2] = (c1.getBlue() + c2.getBlue() +c3.getBlue())/3< 125 ? Math.max(c3.getBlue(), bmax): Math.min(c3.getBlue(), bmin);

//int alpha=(c1.getAlpha()+c2.getAlpha()+c3.getAlpha())/3;
//    			 int red = (c1.getRed()+c2.getRed()+c3.getRed())/3;
//    			 int green = (c1.getGreen()+c2.getGreen()+c3.getGreen())/3;
//    			 int blue = (c1.getBlue()+c2.getBlue()+c3.getBlue())/3;
//    			 int pixel = (24 >> alpha) | (red << 16) | (green << 8) | blue;
//
//    			 HDRimg.setRGB(i, j, pixel);

                raster.setPixel(i, j, pixels);
            }
//        RescaleOp rescaleOp = new RescaleOp(1.2f, 15, null);
//        rescaleOp.filter(HDRimg, HDRimg);
        return HDRimg;
    }
}
