package Test;

import java.awt.Frame;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;



import org.opencv.core.Mat;

public class Imshow {
	
	public static void show(Mat image, String windowName){
		Image img = toBufferedImage(image);
		
		JFrame frame = new JFrame(windowName);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		
		// Inserts the image icon
		ImageIcon imgIcon = new ImageIcon(img);
		frame.setSize(imgIcon.getIconWidth()+10,imgIcon.getIconHeight()+35);
		// Draw the Image data into the BufferedImage
		JLabel label1 = new JLabel(" ", imgIcon, JLabel.CENTER);
		frame.getContentPane().add(label1);
		 
		frame.validate();
		frame.setVisible(true);
	
	}
	
	public static void show(Mat image){
		Image img = toBufferedImage(image);
		
		JFrame frame = new JFrame("");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		
		// Inserts the image icon
		ImageIcon imgIcon = new ImageIcon(img);
		frame.setSize(imgIcon.getIconWidth()+10,imgIcon.getIconHeight()+35);
		// Draw the Image data into the BufferedImage
		JLabel label1 = new JLabel(" ", imgIcon, JLabel.CENTER);
		frame.getContentPane().add(label1);
		 
		frame.validate();
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	
	}
	
	
	public static Image toBufferedImage(Mat m){
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);  
        return image;

    }
	
	
	public static void closeAll(){
		Frame [] frames = JFrame.getFrames();
		for (int i = 0; i < frames.length; i++){
			frames[i].dispose();
		}
		
		
	}
	
}
