package Test;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;



public class GUITest {
	public GUITest(){
		initGUI();
	}
	
	private void initGUI(){
		final JFrame frame = new JFrame("WebCam Test");
		final JPanel panel2 = new JPanel();
		//frame.getContentPane().add(panel2);
		panel2.setLayout(null);
		panel2.setBackground(new Color(255, 0 , 0));
		
		
		final JPanel panel = new JPanel();
		frame.add(panel);
		
		
		panel.setLayout(null);
	
		
		//Add open WebCam button
		JButton change_frame_button = new JButton("Change");
		change_frame_button.setBounds(100, 100, 130, 30);
		panel.add(change_frame_button);
		//change_frame_button.setVisible(true);
		
		change_frame_button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				frame.add(panel2);
				panel2.setVisible(true);
				panel.setVisible(false);
				//panel2.validate();
				//panel2.repaint();
				
			}
			
		});
		
		JButton change_back_frame_button = new JButton("Back");
		change_back_frame_button.setBounds(100, 200, 130, 30);
		panel2.add(change_back_frame_button);
		
		change_back_frame_button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				panel2.setVisible(false);
				frame.remove(panel2);
				frame.add(panel);
				panel.setVisible(true);
			}
			
		});
		
	
		
		
		frame.setSize(600, 600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		panel.setVisible(true);
		panel2.setVisible(false);
		
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				GUITest quitExample = new GUITest();
			}
		});
		
	}

}
