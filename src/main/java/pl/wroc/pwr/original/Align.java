package pl.wroc.pwr.original;

/******************************************************************************
 *
 * Author: Brandon M. Smith
 * Class: Computer Vision - CS766
 * Professor: Li Zhang
 * Project 1 - HDR Images
 * File: Align.java
 *
 * Description:
 *      This is an implementation of the algorithm outlined in "Fast, Robust
 *      Image Registration for Compositing High Dynamic Range Photographs from
 *      Handheld Exposures" by Greg Ward.
 *
 *      The program is used on a UNIX command line like this:
 *
 *           java Align <image1 filename> <image2 filename> <image2 result filename>
 *
 *      where <image1 filename> is the first image. <image2 filename> is the
 *      second image that will be aligned based on image1. <image2 result filename>
 *      will contain a properly shifted version of image2.
 *
 *****************************************************************************/


import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.io.*;
import java.awt.*;

/**
 * Alligns two images, returning the result.
 *
 * @author Brandon Smith
 *
 */
public class Align
{
    /**
     * @param args args[0] is image1, args[2] is image2, and args[3] is the
     * image2 result.
     */
    public static void main(String[] args)
    {
        Align al = new Align();

        if(args.length != 3)
        {
            al.error("Usage: java Align <image1 filename> <image2 filename> <output filenam>");
        }

        System.out.println("     Shifting " + args[1] + " based on " + args[0] + " ...");

        MyImage img1 = al.getMyImage(args[0]);
        MyImage img2 = al.getMyImage(args[1]);

        int shift_ret[] = new int[2];
        shift_ret[0] = 0;
        shift_ret[1] = 0;
        int shift_bits = 6;	// (-+ 64 pixels) This was suggested by the paper
        // described above. It is the maximum bits allowed
        // in our return offset.

        // Recursive method given by the paper described above.
        al.GetExpShift(img1, img2, shift_bits, shift_ret);

        // We have the shift amount, so shift the image.
        img2.shift(shift_ret[0], shift_ret[1]);

        // Write out the file.
        img2.writeToFile(args[2]);

        System.out.println("          ..." + args[2] +
                " created. Shifted by x=" + shift_ret[0] +
                ", y=" + shift_ret[1] + ".");
    }


    public final static int TOLERANCE = 6;
    public final static boolean DEBUG = false;
    public final static int RED = 0;
    public final static int GREEN = 1;
    public final static int BLUE = 2;
    public final static int Z_MIN = 0;
    public final static int Z_MAX = 255;

    public Align()
    {
    }

    /**
     * This function/method was given in the paper described above. It is
     * recursive.
     *
     * @param img1 first image
     * @param img2 second image
     * @param shift_bits maximum number of bits allowed in our return offset
     * @param shift_ret array that stores the shift amounts [0] is x and [1]
     *        is y.
     */
    public void GetExpShift(final MyImage img1,
                            final MyImage img2,
                            final int shift_bits,
                            int shift_ret[])
    {
        debug("entering GetExpShift(...), shift_bits=" +
                shift_bits + " and shift_ret[]=(" + shift_ret[0] + "," +
                shift_ret[1] + ")");

        int 		min_err;
        int 		cur_shift[] = new int[2];
        int 		i, j;
        if (shift_bits > 0)
        {
            MyImage sm1_img1 = img1.ImageShrink2();
            MyImage sm1_img2 = img2.ImageShrink2();
            GetExpShift(sm1_img1, sm1_img2, shift_bits-1, cur_shift);
            //ImageFree(sm1_img1); <-- Java takes care of this automatically
            //ImageFree(sm1_img2); <-- Java takes care of this automatically
            cur_shift[0] *= 2;
            cur_shift[1] *= 2;
        }
        else
        {
            cur_shift[0] = cur_shift[1] = 0;
        }
        MyBitmap tb1 = ComputeThresholdBitmap(img1);
        MyBitmap eb1 = ComputeExclusionBitmap(img1);
        MyBitmap tb2 = ComputeThresholdBitmap(img2);
        MyBitmap eb2 = ComputeExclusionBitmap(img2);
        min_err = img1.getWidth() * img1.getHeight();
        for(i = -1; i <= 1; i++)
        {
            for(j = -1; j <= 1; j++)
            {
                int 		xs = cur_shift[0] + i;
                int 		ys = cur_shift[1] + j;
                MyBitmap shifted_tb2 = new MyBitmap(img1.getWidth(), img1.getHeight());
                MyBitmap shifted_eb2 = new MyBitmap(img1.getWidth(), img1.getHeight());
                MyBitmap diff_b      = new MyBitmap(img1.getWidth(), img1.getHeight());
                BitmapShift(tb2, xs, ys, shifted_tb2);
                BitmapShift(eb2, xs, ys, shifted_eb2);
                BitmapXOR(tb1, shifted_tb2, diff_b);
                BitmapAND(diff_b, eb1, diff_b);
                BitmapAND(diff_b, shifted_eb2, diff_b);
                int err = BitmapTotal(diff_b);
                if (err < min_err)
                {
                    shift_ret[0] = xs;
                    shift_ret[1] = ys;
                    min_err = err;
                }
                //BitmapFree(shifted_tb2); <-- Java takes care of this automatically
                //BitmapFree(shifted_eb2); <-- Java takes care of this automatically
            }
        }
        //BitmapFree(tb1); <-- Java takes care of this automatically
        //BitmapFree(tb2); <-- Java takes care of this automatically
    }

    /**
     * Compute and return the threshold bitmap associated with img.
     *
     * @param img the image used to compute the bitmap
     * @return the threshold bitmap associated with img
     */
    private MyBitmap ComputeThresholdBitmap(final MyImage img)
    {
        int width = img.getWidth();
        int height = img.getHeight();
        int threshold = img.getThreshold();

        int grey = 0;
        int x, y;
        int diff;
        Color c;

        MyBitmap tb = new MyBitmap(width, height);

        for(x = 0; x < width; x++)
        {
            for(y = 0; y < height; y++)
            {
                c = new Color( img.getRGB(x,y) );
                grey = (54*c.getRed() + 183*c.getGreen()+19*c.getBlue())/256;
                diff = grey - threshold;

                // if(grey > threshold)
                if(diff > 0)
                {
                    tb.set(x,y,true);
                }
                else
                { // grey <= threshold
                    tb.set(x,y,false);
                }
            }
        }

        return tb;
    }

    /**
     * Compute and return the exclusion bitmap associated with img.
     *
     * @param img the image used to compute the bitmap
     * @return the exclusion bitmap associated with img
     */
    private MyBitmap ComputeExclusionBitmap(final MyImage img)
    {
        int width = img.getWidth();
        int height = img.getHeight();
        int threshold = img.getThreshold();
        int tolerance = img.getTolerance();
        int grey = 0;
        int x, y;
        int diff;
        Color c;

        MyBitmap eb = new MyBitmap(width, height);

        for(x = 0; x < width; x++)
        {
            for(y = 0; y < height; y++)
            {
                c = new Color( img.getRGB(x,y) );
                grey = (54*c.getRed() + 183*c.getGreen()+19*c.getBlue())/256;
                diff = grey - threshold;

                if(Math.abs(diff) > tolerance)
                {
                    eb.set(x,y,true);
                }
                else
                {
                    eb.set(x,y,false);
                }
            }
        }

        return eb;
    }

    /**
     * Shift a bitmap by (x0, y0) and put the result into a preallocated
     * bitmap bm_ret, clearing exposed border areas to zero.
     *
     * @param bm original bitmap to be shifted
     * @param x0 shift amount in the x direction
     * @param y0 shift amount in the y direction
     * @param bm_ret preallocated bitmap that will be a shifted version of bm
     */
    private void BitmapShift(final MyBitmap bm, final int x0, final int y0, MyBitmap bm_ret)
    {
        // Note: bm_ret should be recently allocated, or at least not modified
        // from when it was created. This makes sure bm_ret is filled with zeros
        // so that we fulfill the requirement of "clearing exposed border areas
        // to zero."

        int width = bm.getWidth();
        int height = bm.getHeight();
        int nwidth = bm_ret.getWidth();
        int nheight = bm_ret.getHeight();
        boolean b;
        int nx;
        int ny;

        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                b = bm.get(x,y);
                nx = x+x0;
                ny = y+y0;

                if(nx < 0 || nx >= nwidth || ny < 0 || ny >= nheight)
                {
                    // We are outside the area of bm_ret.
                    // We must ignore this point.
                }
                else
                {
                    bm_ret.set(x+x0, y+y0, b);
                }
            }
        }
    }

    /**
     * Compute the "exclusive-or" of bm1 and bm2 and put the result into bm_ret.
     *
     * @param bm1 first bitmap
     * @param bm2 second bitmap
     * @param bm_ret preallocated bitmap that will equal bm1 XOR bm2
     */
    private void BitmapXOR(final MyBitmap bm1, final MyBitmap bm2, MyBitmap bm_ret)
    {
        int width = bm1.getWidth();
        int height = bm1.getHeight();
        boolean result;

        if(width != bm2.getWidth()    ||
                width != bm_ret.getWidth() ||
                height != bm2.getHeight()  ||
                height != bm_ret.getHeight())
        {
            error("inside BitmapXOR: bitmaps sizes do not match");
        }
        else
        {
            for(int x = 0; x < width; x++)
            {
                for(int y = 0; y < height; y++)
                {
                    result = bm1.get(x,y) ^ /*XOR*/ bm2.get(x,y);
                    bm_ret.set(x,y,result);
                }
            }
        }
    }

    /**
     * Compute the "and" of bm1 and bm2 and put the result into bm_ret.
     *
     * @param bm1 first bitmap
     * @param bm2 second bitmap
     * @param bm_ret preallocated bitmap that will equal bm1 AND bm2
     */
    private void BitmapAND(final MyBitmap bm1, final MyBitmap bm2, MyBitmap bm_ret)
    {
        int width = bm1.getWidth();
        int height = bm1.getHeight();
        boolean result;

        if(width != bm2.getWidth()    ||
                width != bm_ret.getWidth() ||
                height != bm2.getHeight()  ||
                height != bm_ret.getHeight())
        {
            error("inside BitmapAND: bitmaps sizes do not match");
        }
        else
        {
            for(int x = 0; x < width; x++)
            {
                for(int y = 0; y < height; y++)
                {
                    result = bm1.get(x,y) & /*AND*/ bm2.get(x,y);
                    bm_ret.set(x,y,result);
                }
            }
        }
    }

    /**
     * Compute the sum of all 1 (true) bits in the bitmap.
     *
     * @param bm the bitmap
     * @return the sum of all 1 (true) bits in bm
     */
    private int BitmapTotal(final MyBitmap bm)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int sum = 0;

        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                if(bm.get(x,y)) sum++;
            }
        }

        return sum;
    }

    /**
     * Get a BufferedImage object from the file named filename
     *
     * @param filename the name of the file to open
     * @return a BufferedImage corresponding to filename
     */
    private MyImage getMyImage(final String filename)
    {
        debug("entering getMyImage(...), filename=" + filename);

        FileInputStream fileInputStream = null;
        BufferedImage bufferedImage = null;

        try
        {
            fileInputStream = new FileInputStream(filename);
            bufferedImage = ImageIO.read(fileInputStream);
        }
        catch(FileNotFoundException e)
        {
            error(filename + " not found");
        }
        catch(SecurityException e)
        {
            error("you do not have permission to open " + filename);
        }
        catch(IllegalArgumentException e)
        {
            error("unable to use the FileInputStream created from " + filename);
        }
        catch(IOException e)
        {
            error("unable to open " + filename);
        }

        if(bufferedImage == null)
        {
            error("BufferedImage read from file " + filename + " is null");
        }
        if(bufferedImage.getData() == null)
        {
            error("bufferedImage.getData() == null");
        }

        MyImage img = new MyImage(bufferedImage);

        debug("exiting getMyImage(...)");
        return img;
    }

    /**
     * Prints error message and quits.
     *
     * @param message The error message to be printed to the console window.
     */
    public void error(final String message)
    {
        System.out.println("     Error: " + message);
        System.exit(0);
    }

    /**
     * Prints debug message
     *
     * @param message The debug message to be printed to the console window.
     */
    public void debug(final String message)
    {
        if(DEBUG == true)
        {
            System.out.println("DEBUG:\n     " + message);
        }
    }

    /**
     * Implements the Bitmap talked about in the paper described above.
     *
     * @author Brandon Smith
     *
     */
    public class MyBitmap
    {
        private boolean value[][];
        private int height;
        private int width;

        /**
         * Constructor: create a new bitmap of width and height
         *
         * @param width width of the new bitmap
         * @param height height of the new bitmap
         */
        public MyBitmap(final int width, final int height)
        {
            value = new boolean[width][height];
            this.width = width;
            this.height = height;

            for(int x = 0; x < width; x++)
            {
                for(int y = 0; y < height; y++)
                {
                    value[x][y] = false;
                }
            }
        }

        /**
         * Set a sepcific location in the bitmap to 1 or 0 (true or false)
         *
         * @param x x coordinate
         * @param y y coordinate
         * @param b value, which is 1 or 0 (true or false)
         */
        public void set(int x, int y, boolean b)
        {
            value[x][y] = b;
        }

        /**
         * Get the value at a specific location in the bitmap
         *
         * @param x x coordinate
         * @param y y coordinate
         * @return the value at x, y in the bitmap
         */
        public boolean get(int x, int y)
        {
            return value[x][y];
        }

        /**
         *
         * @return width of bitmap
         */
        public int getWidth()
        {
            return width;
        }

        /**
         *
         * @return height of bitmap
         */
        public int getHeight()
        {
            return height;
        }
    }

    /**
     * Implements the Image structure described in the paper above.
     *
     * @author Brandon Smith
     *
     */
    public class MyImage extends BufferedImage
    {
        //private int threshold = -1;
        private int tolerance = TOLERANCE;

        /**
         * Note: if this constructor is called, the calling method must
         * manually call computeThreshold on the newly created MyImage
         * object. Otherwise the threshold will not be properly set.
         *
         * @param width the width of the new object
         * @param height the height of the new object
         * @param type the type of storage format of the new object
         */
        private MyImage(final int width,
                        final int height,
                        final int type)
        {
            super(width, height, type);
        }

        /**
         * Constructs a new object from img
         *
         * @param img the MyImage object to copy into a new object
         */
        public MyImage(MyImage img)
        {
            super(img.getWidth(), img.getHeight(), img.getType());
            this.setData(img.getRaster());
            // computeThreshold() must be called AFTER storing the data
            //threshold = computeThreshold();
        }

        /**
         * Constructs a new object from bi
         *
         * @param bi the BufferedImage object to copy into a new object
         */
        public MyImage(BufferedImage bi)
        {
            super(bi.getWidth(), bi.getHeight(), bi.getType());
            this.setData(bi.getRaster());
            // computeThreshold() must be called AFTER storing the data
            //threshold = computeThreshold();
        }

        /**
         *
         * @return the threshold value
         */
        public int getThreshold()
        {
            return computeThreshold();
        }

        /**
         *
         * @return the tolerance value
         */
        public int getTolerance()
        {
            return tolerance;
        }

        /**
         * This computes the threshold of this image, which is the median grey
         * pixel value. Note: this method must NOT be called before this image
         * has data.
         *
         * @return the image median threshold.
         */
        private int computeThreshold()
        {
            debug("entering computeThreshold");

            int intensityCount[] = new int[256];
            final int width = this.getWidth();
            final int height = this.getHeight();

            int grey = 0;
            int red  = 0;
            int green = 0;
            int blue  = 0;
            int rgb = 0;
            int x, y;
            Color color;

            for(int i = 0; i < intensityCount.length; i++)
            {
                intensityCount[i] = 0;
            }

            for(x = 0; x < width; x++)
            {
                for(y = 0; y < height; y++)
                {
                    rgb = this.getRGB(x,y);
                    color = new Color(rgb);
                    red = color.getRed();
                    green = color.getGreen();
                    blue = color.getBlue();
                    grey = (54*red + 183*green+19*blue)/256;

                    intensityCount[grey]++;
                }
            }

            int median = 0;
            int greatestIntensity = 0;
            for(x = 0; x < intensityCount.length; x++)
            {
                if(intensityCount[x] > greatestIntensity)
                {
                    greatestIntensity = intensityCount[x];
                    median = x;
                }
            }

            debug("exiting computeThreshold, threshold=" + median);

            return median;
        }

        /**
         * Subsample the image img by a factor of two in each dimension and
         * return the result.
         *
         * @param img the original image.
         * @return a the subsampled (by a factor of 2) image.
         */
        private MyImage ImageShrink2()
        {
            int height = this.getHeight();
            int width  = this.getWidth();

            MyImage img_ret = new MyImage(width/2, height/2, this.getType());

            for(int x = 0; x < width; x+=2)
            {
                for(int y = 0; y < height; y+=2)
                {
                    img_ret.setRGB(x/2, y/2, this.getRGB(x,y));
                }
            }

            // THIS IS VERY IMPORTANT SINCE THE CONSTRUCTOR USED WITHIN THIS METHOD
            // DOES NOT CALL computeThreshold() !!!
            img_ret.computeThreshold();

            return img_ret;
        }

        /**
         * Shift this MyImage object by x0 and y0, filling blank areas with
         * black.
         * @param x0 x shift amount
         * @param y0 y shift amount
         */
        public void shift(final int x0, final int y0)
        {
            debug("entering MyImage.shift(" + x0 + "," + y0 + ")");
            int width = this.getWidth();
            int height = this.getHeight();
            int rgb;
            int absx0 = Math.abs(x0);
            int absy0 = Math.abs(y0);

            if(x0 < 0)
            {
                // Shift left by x0 bits
                if(y0 < 0)
                {
                    // Shift in the negative vertical direction
                    for(int x = absx0; x < width+absx0; x++)
                    {
                        for(int y = absy0; y < height+absy0; y++)
                        {
                            if(x >= width || y < 0 || x < 0 || y >= height)
                            {
                                // Fill with black
                                this.setRGB(x-absx0, y-absy0, Color.black.getRGB());
                            }
                            else
                            {
                                rgb = this.getRGB(x, y);
                                this.setRGB(x-absx0, y-absy0, rgb);
                            }
                        }
                    }
                }
                else
                {
                    // Shift in the positive vertical direction
                    for(int x = absx0; x < width+absx0; x++)
                    {
                        for(int y = height-1-absy0; y >= -absy0; y--)
                        {
                            if (x >= width || y < 0 || x < 0 || y >= height)
                            {
                                // Fill with black
                                this.setRGB(x-absx0, y+absy0, Color.black.getRGB());
                            }
                            else
                            {
                                rgb = this.getRGB(x, y);
                                this.setRGB(x-absx0, y+absy0, rgb);
                            }
                        }
                    }
                }


            }
            else // x0 >= 0
            {
                // Shift right by x0 bits
                if(y0 < 0)
                {
                    // Shift in the negative vertical direction
                    for(int x = width-1-absx0; x >= -absx0; x--)
                    {
                        for(int y = absy0; y < height+absy0; y++)
                        {
                            if(x >= width || y < 0 || x < 0 || y >= height)
                            {
                                // Fill with black
                                this.setRGB(x+absx0, y-absy0, Color.black.getRGB());
                            }
                            else
                            {
                                rgb = this.getRGB(x, y);
                                this.setRGB(x+absx0, y-absy0, rgb);
                            }
                        }
                    }
                }
                else
                {
                    // Shift in the positive vertical direction
                    for(int x = width-1-absx0; x >= -absx0; x--)
                    {
                        for(int y = height-1-absy0; y >= -absy0; y--)
                        {
                            if (x >= width || y < 0 || x < 0 || y >= height)
                            {
                                // Fill with black
                                this.setRGB(x+absx0, y+absy0, Color.black.getRGB());
                            }
                            else
                            {
                                rgb = this.getRGB(x, y);
                                this.setRGB(x+absx0, y+absy0, rgb);
                            }
                        }
                    }
                } // end else
            } // end else
            debug("exiting MyImage.shift(...)");
        } // end public void shift(...)

        /**
         * Sends this MyImage object to a file called filename
         *
         * @param filename the name of the file in which to store
         * the current MyImage object.
         */
        public void writeToFile(String filename)
        {
            debug("entering MyImage.writeToFile(" + filename + ")...");
            FileOutputStream fileOutputStream = null;
            String format = filename.trim();
            format = format.substring(format.length()-3,format.length());

            debug("     format = "+ format);

            try
            {
                fileOutputStream = new FileOutputStream(filename);
                ImageIO.write(this, format, fileOutputStream);
            }
            catch(FileNotFoundException e)
            {
                error(filename + " not found");
            }
            catch(SecurityException e)
            {
                error("you do not have permission to open " + filename);
            }
            catch(IllegalArgumentException e)
            {
                error("unable to use the FileInputStream created from " + filename);
            }
            catch(IOException e)
            {
                error("unable to open " + filename);
            }
            debug("exiting MyImage.writeToFile(...)");
        } // end public void writeMyImage(...)
    } // end class MyImage
} // end class Align
