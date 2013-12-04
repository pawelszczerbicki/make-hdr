package pl.wroc.pwr.willBeRemoved;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;


public class MainWindow  extends JFrame {
    private JLabel Imglabel_1,Imglabel_2,Imglabel_3,label = new JLabel("0.5");
	private JSlider slider = new JSlider();;
    private BufferedImage img1,lableimg1,img2,lableimg2,img3,lableimg3;
    private float Alpha;
	private int i=1;
	public MainWindow() {
		super();
		setSize(800, 600);
		setTitle("HDR");
		init();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
//	public static BufferedImage resize(BufferedImage img, int newW, int newH) throws IOException {
//		  return Thumbnails.of(img).size(newW, newH).asBufferedImage();
//		}
	
	private void init(){
		
		getContentPane().setLayout(null);
	  //     File f = new File("C:\\FinalProject\\examples\\5\\1.jpg");
	  //     File f2 = new File("C:\\FinalProject\\examples\\5\\2.jpg");
	  //     File f3 = new File("C:\\FinalProject\\examples\\5\\3.jpg");
	       File f = new File("photo/ostrow1.jpg");
	       File f2 = new File("photo/ostrow2.jpg");
	       File f3 = new File("photo/ostrow3.jpg");
		try {
			img1 = ImageIO.read(f.toURI().toURL());
			img2 = ImageIO.read(f2.toURI().toURL());
			img3 = ImageIO.read(f3.toURI().toURL());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Alpha= (float) 0.5;
		JButton Load1 = new JButton("Load pictures");
		/*Load1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser(new File("C:\\FinalProject\\examples"));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, GIF," +
						" & PNG Images", "jpg", "gif", "png");
				chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(getParent());
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       System.out.println("You chose to open this file: " +
			            chooser.getSelectedFile().getName());
			       String path =  chooser.getSelectedFile().toString();
			       File f = new File(path);//.toURI().toURL();

					try {
						if (i==1)
						{
							img1 = ImageIO.read(f.toURI().toURL());
							i++;
							lableimg1=resize(img1, Imglabel_1.getWidth(), Imglabel_1.getHeight());
							Imglabel_1.setIcon(new ImageIcon(lableimg1));
						}
						else if (i==2)
						{
							img2 = ImageIO.read(f.toURI().toURL());
							i++;
							lableimg2=resize(img2, Imglabel_1.getWidth(), Imglabel_1.getHeight());
							Imglabel_2.setIcon(new ImageIcon(lableimg2));
						}
						else
						{
							img3 = ImageIO.read(f.toURI().toURL());
							i=1;
							lableimg3=resize(img3, Imglabel_1.getWidth(), Imglabel_1.getHeight());
							Imglabel_3.setIcon(new ImageIcon(lableimg3));
						}

					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

			    }
			}
		});*/
		Load1.setBounds(104, 97, 137, 25);
		getContentPane().add(Load1);
		
		JLabel lblGradient = new JLabel("Gradient Domain HDR Compositing");
		lblGradient.setBounds(243, 27, 233, 14);
		getContentPane().add(lblGradient);
		
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Alpha=(float)slider.getValue()/100;
				label.setText(Float.toString(Alpha));
			}
		});
		slider.setBounds(160, 211, 137, 23);
		getContentPane().add(slider);
		
		JLabel lblAlpha = new JLabel("Alpha");
		lblAlpha.setBounds(104, 220, 46, 14);
		getContentPane().add(lblAlpha);
		
		JButton btnExecute = new JButton("Execute");
		btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HDRalgorithm alg=new HDRalgorithm(img1,img2,img3,Alpha);
			}
		});
		btnExecute.setBounds(120, 357, 89, 23);
		getContentPane().add(btnExecute);
		btnExecute.setEnabled(true);
		
		JButton btnClearAll = new JButton("Clear All");
		btnClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Imglabel_1.setIcon(null);
				Imglabel_2.setIcon(null);
				Imglabel_3.setIcon(null);
				slider.setValue(50);
				label.setText("0.5");
				i=1;
				init();
			}
		});
		btnClearAll.setBounds(293, 357, 89, 23);
		getContentPane().add(btnClearAll);
		btnClearAll.setEnabled(true);
		
		label.setBounds(217, 230, 36, 25);
		getContentPane().add(label);
		
		JLabel lblNewLabel = new JLabel("Image1:");
		lblNewLabel.setBounds(458, 83, 71, 20);
		getContentPane().add(lblNewLabel);
		
		Imglabel_1 = new JLabel("");
		Imglabel_1.setBounds(541, 54, 208, 128);
		getContentPane().add(Imglabel_1);
		
		JLabel lblImage = new JLabel("Image2:");
		lblImage.setBounds(458, 235, 71, 20);
		getContentPane().add(lblImage);
		
		Imglabel_2 = new JLabel("");
		Imglabel_2.setBounds(541, 195, 208, 128);
		getContentPane().add(Imglabel_2);
		
		JLabel lblImage_1 = new JLabel("Image3:");
		lblImage_1.setBounds(458, 380, 71, 20);
		getContentPane().add(lblImage_1);
		
		Imglabel_3 = new JLabel("");
		Imglabel_3.setBounds(542, 334, 208, 128);
		getContentPane().add(Imglabel_3);
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}