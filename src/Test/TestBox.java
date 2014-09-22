package Test;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class TestBox {
	public final static String ADAPTIVE_THRESHOLD = "Adaptive_Thershold";
	public final static String THRESHOLD = "Threshold";
	
	
	private enum METHOD_NAME_LIST{
		Adaptive_Thershold, Threshold;
	}
	
	
	
	//Constructor of the testBox
	public TestBox(String methodName, Mat img){
		initTestBox(methodName, img);
	}
	

	
	//Initiate testBox GUI
	private void initTestBox(String methodName, final Mat img){
		METHOD_NAME_LIST method = METHOD_NAME_LIST.valueOf(methodName);
		final HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("ADAPTIVE_THRESH_GAUSSIAN_C", Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C);
		map.put("ADAPTIVE_THRESH_MEAN_C", Imgproc.ADAPTIVE_THRESH_MEAN_C);
		map.put("THRESH_BINARY", Imgproc.THRESH_BINARY);
		map.put("THRESH_BINARY_INV", Imgproc.THRESH_BINARY_INV);
		
		
		
		switch (method) {
		//TestBox of Adaptive Threshold method
		case Adaptive_Thershold: {
			//Set frame and panel 
			final JFrame frame = new JFrame("TestBox---" + methodName);
			final Mat resultImg = img.clone();
			
			JPanel panel = new JPanel();
			frame.getContentPane().add(panel);
			panel.setLayout(null);
			
			//Add image label
			Image bufferedImg = toBufferedImage(img);
			final ImageIcon imgIcon = new ImageIcon(bufferedImg);
			final JLabel imgLabel = new JLabel();
			imgLabel.setBounds(30, 200, imgIcon.getIconWidth()+10, imgIcon.getIconHeight()+25);
			imgLabel.setIcon(imgIcon);
			panel.add(imgLabel);
			
			
			//Add Adaptive Method list label
			JLabel adaptive_method_list_label = new JLabel("Adaptive Method");
			adaptive_method_list_label.setBounds(50, 10, 150, 50);
			adaptive_method_list_label.setFont(new Font(Font.DIALOG,Font.BOLD,14));
			panel.add(adaptive_method_list_label);
			
			
			//Add Adaptive Method list
			String [] adaptive_method_name_list = new String[]{"ADAPTIVE_THRESH_GAUSSIAN_C", "ADAPTIVE_THRESH_MEAN_C"};
			final JList adaptive_method_list = new JList(adaptive_method_name_list);
			adaptive_method_list.setBounds(10, 50, 200, 50);
			panel.add(adaptive_method_list);
			
			
			
			
			//Add Threshold Type list label
			JLabel threshold_type_list_label = new JLabel("Threshold Type");
			threshold_type_list_label.setBounds(50, 90, 150, 50);
			threshold_type_list_label.setFont(new Font(Font.DIALOG,Font.BOLD,14));
			panel.add(threshold_type_list_label);
			
			
			//Add Threshold Type list 
			String [] threshold_type_name_list = new String[]{"THRESH_BINARY", "THRESH_BINARY_INV"};
			final JList threshold_type_list = new JList(threshold_type_name_list);
			threshold_type_list.setBounds(10, 130, 200, 50);
			panel.add(threshold_type_list);
			
			
			//Add chosen Threshold Type label
			final JLabel chosen_adaptive_method_label = new JLabel();
			
			//Add chosen Threshold Type label
			final JLabel chosen_threshold_type_label = new JLabel();
			
			
			
			//Adaptive Method list action
			adaptive_method_list.addListSelectionListener(new ListSelectionListener(){
				
				@Override
				public void valueChanged(ListSelectionEvent event) {
					// TODO Auto-generated method stub
					chosen_adaptive_method_label.setText(String.valueOf(adaptive_method_list.getSelectedValue()));
				}
			});
			
			
	
			
			
			//Threshold Type list action
			threshold_type_list.addListSelectionListener(new ListSelectionListener(){
				
				@Override
				public void valueChanged(ListSelectionEvent event) {
					// TODO Auto-generated method stub
					chosen_threshold_type_label.setText(String.valueOf(threshold_type_list.getSelectedValue()));
				}
			});
			
			
			//Add BlockSize slider label
			JLabel blockSize_label = new JLabel("Block Size");
			blockSize_label.setBounds(370,25, 100,20);
			blockSize_label.setFont(new Font(Font.DIALOG,Font.BOLD,14));
			panel.add(blockSize_label);
			
			
			//Add BlockSize slider
			final JSlider blockSize_slider = new JSlider();
			blockSize_slider.setBounds(230, 50, 500, 30);
			blockSize_slider.setPaintTicks(true);
			blockSize_slider.setPaintTrack(true);
			blockSize_slider.setMaximum(899);
			blockSize_slider.setMinimum(3);
			blockSize_slider.setValue(55);
			blockSize_slider.setMajorTickSpacing(10);
			panel.add(blockSize_slider);
			
			
			//Add BlockSize slider value text field
			final JTextField blockSize_value_textField = new JTextField(String.valueOf(blockSize_slider.getValue()));
			blockSize_value_textField.setBounds(385, 82, 40, 20);
			blockSize_value_textField.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
			panel.add(blockSize_value_textField);
			
			
			//Add C slider label
			JLabel C_label = new JLabel("C Value");
			C_label.setBounds(375,105, 100,20);
			C_label.setFont(new Font(Font.DIALOG,Font.BOLD,14));
			panel.add(C_label);
			
			
			//Add C slider
			final JSlider C_slider = new JSlider();
			C_slider.setBounds(230, 130, 400, 30);
			C_slider.setPaintTicks(true);
			C_slider.setPaintTrack(true);
			C_slider.setMaximum(250);
			C_slider.setMinimum(1);
			C_slider.setValue(1);
			C_slider.setMajorTickSpacing(10);
			panel.add(C_slider);
			
			
			//Add C slider value text field
			final JTextField C_value_textField = new JTextField(String.valueOf(C_slider.getValue()));
			C_value_textField.setBounds(385, 160, 40, 20);
			C_value_textField.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
			panel.add(C_value_textField);
			
			
			
			//BlockSize slider action
			blockSize_slider.addChangeListener(new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent arg0) {
					// TODO Auto-generated method stub
					if (img.channels() == 1){
						if (blockSize_slider.getValue()%2 != 0){
							try{
								blockSize_value_textField.setText(String.valueOf(blockSize_slider.getValue()));
								int blockSize = Integer.parseInt(blockSize_value_textField.getText());
								int C = Integer.parseInt(C_value_textField.getText());
								int thresholdType = map.get(chosen_threshold_type_label.getText());
								int adaptiveMethod = map.get(chosen_adaptive_method_label.getText());
								Imgproc.adaptiveThreshold(img, resultImg, 255, adaptiveMethod, thresholdType, blockSize, C);
								Image bufferedImg = toBufferedImage(resultImg);
								ImageIcon imgIcon = new ImageIcon(bufferedImg);
								imgLabel.setIcon(imgIcon);
							}
							catch(NullPointerException e){
								JOptionPane.showMessageDialog(frame,
									    "Please select Threshold Type and Adaptive Method!"  ,"Warning",
									    JOptionPane.WARNING_MESSAGE);
							}
						}
					}
				}
			});
			
			
			//C slider action
			C_slider.addChangeListener(new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent arg0) {
					// TODO Auto-generated method stub
					if (img.channels()==1){
						try {
							C_value_textField.setText(String.valueOf(C_slider.getValue()));
							int blockSize = Integer.parseInt(blockSize_value_textField.getText());
							int C = Integer.parseInt(C_value_textField.getText());
							int thresholdType = map.get(chosen_threshold_type_label.getText());
							int adaptiveMethod = map.get(chosen_adaptive_method_label.getText());
							//Mat resultImg = img.clone();
							Imgproc.adaptiveThreshold(img, resultImg, 255, adaptiveMethod, thresholdType, blockSize, C);
							Image bufferedImg = toBufferedImage(resultImg);
							ImageIcon imgIcon = new ImageIcon(bufferedImg);
							imgLabel.setIcon(imgIcon);
						}
						catch (NullPointerException e){
							JOptionPane.showMessageDialog(frame,
								    "Please select Threshold Type and Adaptive Method!"  ,"Warning",
								    JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			});
			
			
			//Add Update Value button
			JButton update_value_button = new JButton("Update Value");
			update_value_button.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
			update_value_button.setBounds(650, 120, 120, 30);
			panel.add(update_value_button);
			
			
			//Update Value Button action
			update_value_button.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent event) {
					// TODO Auto-generated method stub
					if (img.channels()==1){
						try {
							int blockSize = Integer.parseInt(blockSize_value_textField.getText());
							int C = Integer.parseInt(C_value_textField.getText());
							int thresholdType = map.get(chosen_threshold_type_label.getText());
							int adaptiveMethod = map.get(chosen_adaptive_method_label.getText());
							Imgproc.adaptiveThreshold(img, resultImg, 255, thresholdType, adaptiveMethod, blockSize, C);
							Image bufferedImg = toBufferedImage(resultImg);
							ImageIcon imgIcon = new ImageIcon(bufferedImg);
							imgLabel.setIcon(imgIcon);
							}
						catch (NullPointerException e){
							JOptionPane.showMessageDialog(frame,
									"Please select Threshold Type and Adaptive Method!"  ,"Warning",
									JOptionPane.WARNING_MESSAGE);
							}
					}
				}
			});
			
			
			
			//Add Save Image button
			final JButton save_button = new JButton("Save Image");
			save_button.setBounds(800, 700, 120, 30);
			save_button.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
			panel.add(save_button);
			
			
			//Add file chooser for saving image
			final JFileChooser save_file_chooser = new JFileChooser();
			panel.add(save_file_chooser);
			
			
			//Add save file path text field
			//final JTextField save_path_textField = new JTextField();
			//save_path_textField.setBounds(700, 500, 200, 110);
			//panel.add(save_path_textField);
			
			
			//Add Save Image button action
			save_button.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if (e.getSource() == save_button){
						int returnVal = save_file_chooser.showSaveDialog(null);
						
						if (returnVal == JFileChooser.APPROVE_OPTION){
							File file = save_file_chooser.getSelectedFile();
							//save_path_textField.setText(file.getPath());
							ImageIO.saveImage(resultImg, file.getPath());
						}
					}
				}
			});
			
			
			
			
			
			
			
			
			
			
			
			//Add Exit button
			JButton exit_button = new JButton("Exit");
			exit_button.setBounds(800,750,120,30);
			exit_button.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
			panel.add(exit_button);
			
			
			//Add Exit button action
			exit_button.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					frame.dispose();
				}
			});
			
			
			frame.setSize(1000, 900);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			
	
		
		}
		break;
		case Threshold: {
			
		}
		break;
		default: {
			break;
		}
		}
		
	}
	
	
	private Image toBufferedImage(Mat m){
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
	
	public Mat saveImage(){
		Mat resultImg = new Mat();
		return resultImg;
		
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat img = Highgui.imread("C:\\Users\\Yicheng\\Desktop\\work\\face recognition\\side view face\\" + "ID03_009.bmp", Highgui.CV_LOAD_IMAGE_COLOR); 
		Mat HSVImg = img.clone();
		Imgproc.cvtColor(img, HSVImg, Imgproc.COLOR_BGR2HSV);
		
		ArrayList<Mat> YCrCB_List = new ArrayList<Mat>();
		Core.split(HSVImg, YCrCB_List);
		Mat H_channel = YCrCB_List.get(0);
		Mat S_channel = YCrCB_List.get(1);
		Mat V_channel = YCrCB_List.get(2);
		
		TestBox adaptive_threshold_box = new TestBox(TestBox.ADAPTIVE_THRESHOLD, V_channel);
		
		//adaptive_threshold_box.show();
		
		//Mat resultImg = adaptive_threshold_box.saveImage();
		
		//Imshow.show(resultImg);
		
		
		
		
		
	}

}


