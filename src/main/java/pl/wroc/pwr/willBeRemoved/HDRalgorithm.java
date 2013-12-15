package pl.wroc.pwr.willBeRemoved;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;


public class HDRalgorithm {
    private BufferedImage img1,img2,img3,img4,Gradientimg,Finalimage,Finalimage2,FixImage;
	private Float Alpha;
	private boolean isIn=false;
	private int tg=10;
	private JFrame HDRFrame = new JFrame("Average HDR image");
	private JFrame GradientFrame = new JFrame("Fixing image");
	private JFrame FinalFrame = new JFrame("HDR Final image");
	JLabel HDRHDRpictureLabel = new JLabel();
	JLabel GradientpictureLabel = new JLabel();
	JLabel GradientFinalpictureLabel = new JLabel();

	
	
	public HDRalgorithm(BufferedImage im1, BufferedImage im2, BufferedImage im3, Float a){
		img1=im1;
		img2=im2;
		img3=im3;
		Alpha=a;
		Finalimage=HDR(img1,img2,img3);
		PrintPicture(HDRHDRpictureLabel, HDRFrame, Finalimage, "HDR");
//		CreateGradient(img2);
//		img4=CreateFixImage(im1, im3);
//		//PrintPicture(GradientpictureLabel,GradientFrame,img4, "gradient");
//		GradientHDR(img1,img2,img3);
//		float brightness = BrightnessCheck(Finalimage2);
		//PrintPicture(GradientFinalpictureLabel,FinalFrame,Finalimage2, "gradientFInal");
		
		
	}

    public HDRalgorithm() {
    }

    private void GradientHDR(BufferedImage img1, BufferedImage img2, BufferedImage img3) {
		
		Finalimage2= new BufferedImage(img2.getWidth(), img2.getHeight(), img2.getType());
		Finalimage2=img2;
		int w = Gradientimg.getWidth(null);
   	    int h = Gradientimg.getHeight(null);
   	    for(int i=0;i<h;i++)
   	    	for (int j=1;j<w-1;j++)
   	    	{
   	    		Color c1 = new Color(Gradientimg.getRGB(j, i));
   	    		Color c1b = new Color(Gradientimg.getRGB(j-1, i));
   	    		Color c1a = new Color(Gradientimg.getRGB(j+1, i));
   	    		int green = c1.getGreen();
   	    		int greenb = c1b.getGreen();
   	    		int greena = c1a.getGreen();
   	    		if ((greenb<green) && (greena<green))
   	    		{
   	    			if ((green>tg) && (isIn==false))
   	    			{
   	    				isIn=true;
   	    			}
   	    			else 
   	    			{
   	    				isIn=false;	
   	    			}
   	    		}
   	    		if (true) 
   	    			fix(j+1,i);		
   	    	}
		
	}
	private void fix(int j, int i) {
		int rgb;
		float[] hsv = new float[3];
		float[] hsvtemp = new float[3];
		float[] hsvUnder = new float[3];
		float[] hsvOver = new float[3];
		float l,l2,l3;
		int x=3;
		float valpha[]={(float) 0.5,(float) 1,(float) 0.5,1,(float) 0.5,(float) 0.3,(float) 0.1};
		Color c1 = new Color(Finalimage2.getRGB(j, i));
		Color temp;
		int brightness = ((c1.getRed()+c1.getGreen()+c1.getBlue())/3);
		if (brightness>-1)
		{
	/*		if ((i-x>=0) && (i+x<=Finalimage2.getWidth()) && (j-x>=0) && (j+x<=Finalimage2.getHeight()))
				for (int k=i-x;k<=i+x;k++)
					for (int t=j-x,s=0;t<=j+x;t++,s++)
					{
						c1 = new Color(Finalimage2.getRGB(t, k));
						HSLColor base = new HSLColor( c1 );
						l=base.getLuminance();
						Color cUnder = new Color(img1.getRGB(t, k));
						HSLColor baseunder = new HSLColor( cUnder );
						l2=baseunder.getLuminance();
						//temp=base.adjustLuminance(l2+(valpha[s]*((l2/l)*(l-l2))));
						temp=base.adjustLuminance((valpha[s]*l2+(1-valpha[s])*l));
						//if (l>l2)
						//	temp=base.adjustLuminance((l2/l)*100);
						//else temp=base.adjustLuminance(l);
						int pixel = (24 >> temp.getAlpha()) | (temp.getRed() << 16) | (temp.getGreen() << 8) | temp.getBlue();
						Finalimage2.setRGB(t, k, pixel);
					}
			*/
			HSLColor base = new HSLColor( c1 );
			l=base.getLuminance();
			Color cFix = new Color(img4.getRGB(j, i));
			HSLColor baseunder = new HSLColor( cFix );
			l2=baseunder.getLuminance();
			//temp=base.adjustLuminance(l-10);
			temp=base.adjustLuminance((float) ((l2*(1-Alpha))+(l*Alpha)));
			int pixel = (24 >> temp.getAlpha()) | (temp.getRed() << 16) | (temp.getGreen() << 8) | temp.getBlue();
			Finalimage2.setRGB(j, i, pixel);
			
			//lnow=base.getLuminance();
			//Color.RGBtoHSB(c1.getRed(),c1.getGreen(),c1.getBlue(),hsv);
			
			/*
			int red = ((int)(c1.getRed()*Alpha)+(int)(cUnder.getRed()*(1-Alpha)));
			int green = ((int)(c1.getGreen()*Alpha)+(int)(cUnder.getGreen()*(1-Alpha)));
			int blue = ((int)(c1.getBlue()*Alpha)+(int)(cUnder.getBlue()*(1-Alpha)));
			*/
			//int red = c1.getRed()-30;
			//int green = c1.getGreen()-20;
			//int blue = c1.getBlue()-10;
			
			//temp=base.adjustLuminance((l/(l+(l/l2)))*100);
			//temp=base.adjustLuminance(l-10);
			
			/*
			int alpha1=255;
			int red = 0;
			int green = 0;
			int blue = 0;
			int pixel = (24 >> alpha1) | (red << 16) | (green << 8) | blue;
			Finalimage2.setRGB(j, i, pixel);
			*/
			
		}
		else if (brightness<-1)
		{
			//Color.RGBtoHSB(c1.getRed(),c1.getGreen(),c1.getBlue(),hsv);
			Color cOver = new Color(img3.getRGB(j, i));
			/*
			int red = ((int)(c1.getRed()*Alpha)+(int)(cOver.getRed()*(1-Alpha)));
			int green = ((int)(c1.getGreen()*Alpha)+(int)(cOver.getGreen()*(1-Alpha)));
			int blue = ((int)(c1.getBlue()*Alpha)+(int)(cOver.getBlue()*(1-Alpha)));
			
			int red = c1.getRed()+30;
			int green = c1.getGreen()+20;
			int blue = c1.getBlue()+10;
			int pixel = (24 >> alpha1) | (red << 16) | (green << 8) | blue;
			Finalimage2.setRGB(j, i, pixel);

			/*
			int alpha1=255;
			int red = 0;
			int green = 0;
			int blue = 0;
			int pixel = (24 >> alpha1) | (red << 16) | (green << 8) | blue;*/
			//hsvtemp[2]=hsv[2]=(Alpha*hsvOver[2]+(1-Alpha)*hsv[2]);
			//hsvtemp[2]=(float) (hsv[2]+0.07);
			//rgb=Color.HSBtoRGB(hsv[0], hsv[1], hsvtemp[2]);
			HSLColor base = new HSLColor( c1 );
			l=base.getLuminance();
			HSLColor baseover = new HSLColor( cOver );
			l2=baseover.getLuminance();
			//temp=base.adjustLuminance((l/(l+(l/l2)))*100);
			//temp=base.adjustLuminance((l2-(l/l2)*(l2-l)));
			temp=base.adjustLuminance((float) ((l2*0.8)+(l*0.2)));
			int pixel = (24 >> temp.getAlpha()) | (temp.getRed() << 16) | (temp.getGreen() << 8) | temp.getBlue();
			Finalimage2.setRGB(j, i, pixel);
		}  
		
		
	}
	public BufferedImage HDRimg;
    public BufferedImage HDR(BufferedImage im1,BufferedImage im2,BufferedImage im3){
    	
    	 //TODO if two of three colors are higher than hals (128) get all colors from one picture

    	 HDRimg = new BufferedImage(im2.getWidth(), im2.getHeight(), im2.getType());    	  
    	 int width = HDRimg.getWidth(null);
    	 int height = HDRimg.getHeight(null);
        WritableRaster raster = HDRimg.getRaster();
    	 for(int i=0;i<width;i++)
    		 for (int j=0;j<height;j++)
    		 {
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
                 howManyMax = (c1.getRed() + c2.getRed() + c3.getRed())/3 < 125 ? howManyMax: howManyMax + 1;
                 howManyMax = (c1.getGreen() + c2.getGreen() + c3.getGreen())/3 < 125 ? howManyMax: howManyMax + 1;
                 howManyMax = (c1.getBlue() + c2.getBlue() +c3.getBlue())/3< 125 ? howManyMax: howManyMax + 1;
                 if(howManyMax >=2){
                     pixels[0] = Math.max(c3.getRed(), rmax);
                     pixels[1] = Math.max(c3.getGreen(), gmax);
                     pixels[2] = Math.max(c3.getBlue(), bmax);
                 } else{
                     pixels[0] = Math.min(c3.getRed(), rmin);
                     pixels[1] = Math.min(c3.getGreen(), gmin);
                     pixels[2] = Math.min(c3.getBlue(), bmin);
                 }


//                 pixels[0] = (c1.getRed() + c2.getRed() + c3.getRed())/3 < 125 ? Math.max(c3.getRed(), rmax): Math.min(c3.getRed(), rmin);
//                 pixels[1] = (c1.getGreen() + c2.getGreen() + c3.getGreen())/3 < 125 ? Math.max(c3.getGreen(), gmax): Math.min(c3.getGreen(), gmin);
//                 pixels[2] = (c1.getBlue() + c2.getBlue() +c3.getBlue())/3< 125 ? Math.max(c3.getBlue(), bmax): Math.min(c3.getBlue(), bmin);
//
// int alpha=(c1.getAlpha()+c2.getAlpha()+c3.getAlpha())/3;
//    			 int red = (c1.getRed()+c2.getRed()+c3.getRed())/3;
//    			 int green = (c1.getGreen()+c2.getGreen()+c3.getGreen())/3;
//    			 int blue = (c1.getBlue()+c2.getBlue()+c3.getBlue())/3;
//    			 int pixel = (24 >> alpha) | (red << 16) | (green << 8) | blue;

//    			 HDRimg.setRGB(i, j, pixel);
                 raster.setPixel(i, j, pixels);
    		 }
    	  
	return HDRimg;
	
    }

    public BufferedImage HDRluminance(BufferedImage im1,BufferedImage im2,BufferedImage im3){

        HDRimg = new BufferedImage(im2.getWidth(), im2.getHeight(), im2.getType());
        int width = HDRimg.getWidth(null);
        int height = HDRimg.getHeight(null);
        WritableRaster raster = HDRimg.getRaster();
        for(int i=0;i<width;i++)
            for (int j=0;j<height;j++)
            {
                int[] pixels = raster.getPixel(i, j, (int[]) null);

                Color c1 = new Color(im1.getRGB(i, j));
                Color c2 = new Color(im2.getRGB(i, j));
                Color c3 = new Color(im3.getRGB(i, j));
                double redFraction = 0.2126;
                double greenFraction = 0.7152;
                double blueFraction = 0.0722;

                double lumination = (redFraction*c2.getRed()) + (greenFraction*c2.getGreen()) + (blueFraction*c2.getBlue());

                double finalLumination = lumination/(lumination+1);

                pixels[0] = new Double(finalLumination/redFraction).intValue();
                pixels[1] = new Double(finalLumination/greenFraction).intValue();
                pixels[2] = new Double(finalLumination/blueFraction).intValue();

                raster.setPixel(i, j, pixels);
            }

        return HDRimg;

    }
    
    
    public BufferedImage CreateFixImage(BufferedImage im1,BufferedImage im3){
    	
    	

     FixImage = new BufferedImage(im1.getWidth(), im1.getHeight(), im1.getType());    	  
   	 int w = FixImage.getWidth(null);
   	 int h = FixImage.getHeight(null);
   	 float brightness,beta=(float) 0.5;
   	 
   	 
   	 brightness=BrightnessCheck(img2);
   	 if (brightness<100)
   		 beta=(float) 0.2;
   	 else beta=(float) 0.7;
  			
   
   	 for(int i=0;i<h;i++)
   		 for (int j=0;j<w;j++)
   		 {
   			 Color c1 = new Color(im1.getRGB(j, i));
   			 Color c3 = new Color(im3.getRGB(j, i));
   			 int alpha=(c1.getAlpha()+c3.getAlpha())/2;
   			 int blue = (int) (beta*(c1.getBlue())+(1-beta)*(c3.getBlue()));
			 int green = (int) (beta*(c1.getGreen())+(1-beta)*(c3.getGreen()));
			 int red = (int) (beta*(c1.getRed())+(1-beta)*(c3.getRed()));
	
   			 
   	/*		 
   			 float temp=((float)c1.getBlue())/255;
   			 int alpha=(c1.getAlpha()+c3.getAlpha())/2;
  			 int blue = (int) ((temp/(temp+1))*255);
  			 temp=((float)c1.getGreen())/255;
  			 int green = (int) ((temp/(temp+1))*255);
  			 temp=((float)c1.getRed())/255;
  			 int red = (int) ((temp/(temp+1))*255);
			 
	*/		 
			 int pixel = (24 >> alpha) | (red << 16) | (green << 8) | blue;
			 FixImage.setRGB(j, i, pixel);
   	
   		 }
   	  
	return FixImage;
	
   }
    private void CreateGradient(BufferedImage img){
    	 Gradientimg=new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
    	 int w = Gradientimg.getWidth(null);
  	     int h = Gradientimg.getHeight(null);
  	   for(int i=0;i<h;i++)
  		 for (int j=0;j<w-1;j++)
  		 {
  			 Color c1 = new Color(img.getRGB(j, i));
  			 Color c2 = new Color(img.getRGB(j+1, i));
  			 int alpha=Math.abs(c1.getAlpha()-c2.getAlpha());
  			 int red = Math.abs(c1.getRed()-c2.getRed());
  			 int green = Math.abs(c1.getGreen()-c2.getGreen());
  			 int blue = Math.abs(c1.getBlue()-c2.getBlue());
  			 int pixel = (24 >> alpha) | (red << 16) | (green << 8) | blue;
  			 Gradientimg.setRGB(j, i, pixel);
  		 }
    }
    
    private void PrintPicture(JLabel PictureLabel,JFrame PicFrame, BufferedImage img, String name ){

        try {
            ImageIO.write(img, "jpg", new File(name+".jpg"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        PictureLabel.setIcon(new ImageIcon(img));
	    // add the label to the frame
    	PicFrame.getContentPane().add(PictureLabel);
	    // pack everything (does many stuff. e.g. resizes the frame to fit the image)
    	PicFrame.pack();
    	PicFrame.setVisible(true);
    }
    
    private float BrightnessCheck(BufferedImage img){
    	long count=0;
    	float avg;
    	int w = img.getWidth(null);
    	int h = img.getHeight(null);
    	for(int i=0;i<h;i++)
     		 for (int j=0;j<w;j++)
     		 {
     			Color c1 = new Color(img.getRGB(j, i)); 
     			int brightness = ((c1.getRed()+c1.getGreen()+c1.getBlue())/3);
     			count=count+brightness;	
     		 }
      	 avg=(count/(h*w));
      	 System.out.print(avg);
      	 return avg;
    }
    
    
}