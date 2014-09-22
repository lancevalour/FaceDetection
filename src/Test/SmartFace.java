package Test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
//import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.core.Point;

/**
 * @author Yicheng Zhang 
 *
 */

public class SmartFace {
	private static JFrame baseFrame;
	private static JMenuBar menuBar;
	private static JPanel blank_panel;
	private static JPanel welcome_panel;
	private static JPanel face_detection_panel;
	private static JPanel skin_color_detection_panel;
	private static JPanel hair_color_detection_panel;
	
	
	
	
	
	
	/**
	 * Class constructor
	 */
	public SmartFace(){
		initiateSmartFace();
	}
	
	
	
	/**
	 * Initiate the Smart Face GUI
	 */
	private static void initiateSmartFace(){
		baseFrame = new JFrame("Smart Face");
		
		createMenu();
		baseFrame.setJMenuBar(menuBar);

		setWelcomePanel();
		baseFrame.getContentPane().add(welcome_panel);
		
		
		
		setWelcomePanel();
		setFaceDetectionPanel();
		

		baseFrame.setVisible(true);
		baseFrame.setLocation(500, 20);
		baseFrame.setSize(1000, 850);
		//baseFrame.setResizable(false);
		//baseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//baseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		baseFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// Add base frame close action
		baseFrame.addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				int confirm = JOptionPane.showOptionDialog(null, "Are you sure to close the application", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (confirm == 0){
				
					if (camera != null){
						camera.release();
					}
					
					System.exit(0);
					
				}
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	
	/**
	 * Add menu and menu items
	 */
	private static void createMenu(){
		menuBar = new JMenuBar();
		
		// Add "File" menu 
		JMenu file_menu = new JMenu("File");
		file_menu.setMnemonic(KeyEvent.VK_A);
		file_menu.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 15));
		//menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		menuBar.add(file_menu);
		
		// For future implementation of more menu item
		JMenuItem file_menu_more_to_add_menuItem = new JMenuItem("More to add", KeyEvent.VK_T);
		file_menu_more_to_add_menuItem.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 15));
		file_menu.add(file_menu_more_to_add_menuItem);
		
		file_menu.addSeparator();
		
		// Add "Exit" menu item
		JMenuItem exit_menuItem = new JMenuItem("Exit", KeyEvent.VK_T);
		exit_menuItem.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 15));
		file_menu.add(exit_menuItem);
		
		exit_menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int confirm = JOptionPane.showOptionDialog(null, "Are you sure to close the application", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (confirm == 0){
				
					if (camera != null){
						camera.release();
					}
					
					System.exit(0);
					
				}
			}
		});

		
		// Add "Function" menu
		JMenu function_menu = new JMenu("Function");
		function_menu.setMnemonic(KeyEvent.VK_A);
		function_menu.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 15));
		//menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
		menuBar.add(function_menu);
		
		
		// Add "Face Detection" menu item
		JMenuItem face_detection_menuItem;
		face_detection_menuItem = new JMenuItem("Face Detection", KeyEvent.VK_T);
		face_detection_menuItem.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 15));
		function_menu.add(face_detection_menuItem);
		
		
		// Add "Face Detection" menu item action
		face_detection_menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//setFaceDetectionPanel();
				//setFaceDetectionPanel();
				baseFrame.getContentPane().removeAll();
				//baseFrame.remove(welcome_panel);
				//baseFrame.add(baseFrame.getContentPane());
				baseFrame.getContentPane().add(face_detection_panel);
				baseFrame.revalidate();
				baseFrame.repaint();
			}
		});
		
		
		// Add "Skin Color Detection" menu item
		JMenuItem skin_color_detection_menuItem = new JMenuItem("Skin Color Detection", KeyEvent.VK_T);
		skin_color_detection_menuItem.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 15));
		function_menu.add(skin_color_detection_menuItem);
	
		
		//Add "Hair Color Detection" menu item
		JMenuItem hair_color_detection_menuItem = new JMenuItem("Hair Color Detection", KeyEvent.VK_T);
		hair_color_detection_menuItem.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 15));
		function_menu.add(hair_color_detection_menuItem);
		
		
		//Add "Face Recognition" menu item
		JMenuItem face_recognition_menuItem = new JMenuItem("Face Recognition", KeyEvent.VK_T);
		face_recognition_menuItem.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 15));
		function_menu.add(face_recognition_menuItem);
		
		
		
		
		
		
		
		// Add "More to add" menu item
		JMenuItem more_to_add_menuItem = new JMenuItem("More to add", KeyEvent.VK_T);
		more_to_add_menuItem.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 15));
		function_menu.add(more_to_add_menuItem);
		
		
		// Add "More" menu
		JMenu more_menu = new JMenu("More");
		more_menu.setMnemonic(KeyEvent.VK_A);
		more_menu.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 15));
		menuBar.add(more_menu);
		
		// Add "More to add" menu item
		more_to_add_menuItem = new JMenuItem("More to add", KeyEvent.VK_T);
		more_to_add_menuItem.setFont(new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 15));
		more_menu.add(more_to_add_menuItem);
		
	}
	
	
	
	/**
	 * Set welcome panel
	 */
	private static void setWelcomePanel(){
		welcome_panel = new JPanel();
		welcome_panel.setLayout(null);
		
		
	}
	
	
	/**
	 * Set face detection panel
	 */
	private static VideoCapture camera = null;
	private static Thread webCamThread; 
	private static Thread webCamDetectionThread;
	private static Mat currentImg;
	private static void setFaceDetectionPanel(){
		String [] classifier_name_list = new String[]{
			"haarcascade_frontalface_alt",
			"haarcascade_frontalface_alt2",
			"haarcascade_frontalface_alt_tree",
			"haarcascade_frontalface_default",
			"haarcascade_profileface",
			//"lbpcascade_frontalface"
			};
		
		String classifierDirectory = "C:\\OpenCV-2.4.6\\opencv\\sources\\data\\haarcascades\\";
		
		final HashMap<String, CascadeClassifier> map = new HashMap<String, CascadeClassifier>();
		for (int i = 0; i< classifier_name_list.length; i++){
			map.put(classifier_name_list[i], new CascadeClassifier(classifierDirectory+ classifier_name_list[i] + ".xml"));
		}
		
		face_detection_panel = new JPanel();
		face_detection_panel.setLayout(null);
		//face_detection_panel.setLayout(new BorderLayout());
		currentImg = new Mat();
		
		
		//final Mat currentImg = new Mat();
		// Add image label
		//Image bufferedImg = toBufferedImage(img);
		//ImageIcon imgIcon = new ImageIcon(bufferedImg);
		final JLabel imgLabel = new JLabel();
		//imgLabel.setBounds(30, 200, imgIcon.getIconWidth()+10, imgIcon.getIconHeight()+25);
		//imgLabel.setIcon(null);
		face_detection_panel.add(imgLabel);
		
		
		
		
		// Add display result label
		final JLabel display_label = new JLabel();
		display_label.setBounds(40, 670, 600, 90);
		display_label.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
		face_detection_panel.add(display_label);		
		
		
		
		
		
		// Add Classifier Name list label
		JLabel classifier_name_label = new JLabel("Classifier Name");
		classifier_name_label.setBounds(60,0,150,50);
		classifier_name_label.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
		face_detection_panel.add(classifier_name_label);
		
		// Add Classifier Name list 
		final JList classifier_list = new JList(classifier_name_list);
		classifier_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		classifier_list.setLayoutOrientation(JList.VERTICAL);
		classifier_list.setFont(new Font(Font.DIALOG, Font.PLAIN, 14));
		//classifier_list.setBounds(10, 50, 200, 50);
		face_detection_panel.add(classifier_list);
		
		// Add Classifier Name list scroller
		JScrollPane listScroller = new JScrollPane(classifier_list);
		listScroller.setBounds(10, 50, 250, 82);
		face_detection_panel.add(listScroller);
		
		// Add Open Image button
		final JButton open_image_button = new JButton("Open Image");
		open_image_button.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		open_image_button.setBounds(300, 50, 125, 30);
		face_detection_panel.add(open_image_button);
		
		// Add Open Image button action
		open_image_button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (camera == null){
					JFileChooser open_file_chooser = new JFileChooser();
					open_file_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					open_file_chooser.setCurrentDirectory(new File("C:\\Users\\Yicheng\\Desktop"));
					baseFrame.add(open_file_chooser);
					
					if (e.getSource() == open_image_button){
						int returnVal = open_file_chooser.showOpenDialog(null);
						
						if (returnVal == JFileChooser.APPROVE_OPTION){
							File file = open_file_chooser.getSelectedFile();
							if (TestIo.isImageFile(file)){
								currentImg = Highgui.imread(file.getPath());
								double col_row_ratio = (double)currentImg.cols()/currentImg.rows();
								
								Imgproc.resize(currentImg, currentImg, new Size(500*col_row_ratio,500));
								Image bufferedImg = toBufferedImage(currentImg);
								ImageIcon imgIcon = new ImageIcon(bufferedImg);
								imgLabel.setBounds(30, 175, imgIcon.getIconWidth(), imgIcon.getIconHeight());
								imgLabel.setIcon(imgIcon);
							}
							else{
								JOptionPane.showMessageDialog(baseFrame, "Please open the image file!", "Warning" , JOptionPane.WARNING_MESSAGE);
							}
						}
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "Please close the camera!", "Warning" , JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		// Add Close Image button
		JButton close_image_button = new JButton("Close Image");
		close_image_button.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		close_image_button.setBounds(300, 100, 125, 30);
		face_detection_panel.add(close_image_button);
		
		// Add Close Image button action
		close_image_button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (camera == null){
					imgLabel.setIcon(null);
					display_label.setText(null);
				}
				else {
					JOptionPane.showMessageDialog(null, "Please close the camera!", "Warning" , JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		// Add Open Camera button
		JButton open_camera_button = new JButton("Open Camera");
		open_camera_button.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		open_camera_button.setBounds(455, 50, 135, 30);
		face_detection_panel.add(open_camera_button);
		
		
		// Define webcam display thread
				
		
		
		// Add Open Camera button action
		open_camera_button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (camera == null){
				SwingUtilities.invokeLater(new Runnable(){
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						webCamThread = new Thread(new Runnable(){
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								
								camera = new VideoCapture(0);
								if (!camera.isOpened()){
									JOptionPane.showMessageDialog(baseFrame, "Error loading camera!", "Warning" , JOptionPane.WARNING_MESSAGE);
								}
								else{
									//Mat img = new Mat();
									try {
										Thread.sleep(2000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								
									while (camera.isOpened()){
										camera.retrieve(currentImg);
										double col_row_ratio = (double)currentImg.cols()/currentImg.rows();
										Imgproc.resize(currentImg, currentImg, new Size(500*col_row_ratio,500));
										Image bufferedImg = toBufferedImage(currentImg);
										ImageIcon imgIcon = new ImageIcon(bufferedImg);
										imgLabel.setBounds(30, 175, imgIcon.getIconWidth(), imgIcon.getIconHeight());
										imgLabel.setIcon(imgIcon);
										imgLabel.revalidate();
										imgLabel.repaint();
									}
							
								}
							}
						});
						
						webCamThread.start();
					}
					
				});
				
			
				
			}
			}
			
		});
		
				
		
		// Add Close Camera button
		JButton close_camera_button = new JButton("Close Camera");
		close_camera_button.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		close_camera_button.setBounds(455, 100, 135, 30);
		face_detection_panel.add(close_camera_button);
		
		// Add Close Camera button action
		close_camera_button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				SwingUtilities.invokeLater(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (webCamThread != null){
							webCamThread.stop();
						}
						if (camera != null){
							camera.release();
						}
						camera = null;
						imgLabel.setIcon(null);
					}
					
				});
			
			}
			
		});
		
	
		
		// Add Start Detection button
		JButton start_detection_button = new JButton("Start Detection");
		start_detection_button.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		start_detection_button.setBounds(620, 50, 150, 30);
		face_detection_panel.add(start_detection_button);
		
		// Add Start Detection button action
		start_detection_button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (camera != null){
					SwingUtilities.invokeLater(new Runnable(){
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							webCamThread.stop();
							webCamDetectionThread = new Thread(new Runnable(){

								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {
										Thread.sleep(2000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
										if (!currentImg.empty()){
											try {
												while (camera.isOpened()){
												CascadeClassifier classifier = map.get(classifier_list.getSelectedValue());
												
												MatOfRect faceDetections = new MatOfRect();
												//currentImg = null;
												camera.retrieve(currentImg);
												double col_row_ratio = (double)currentImg.cols()/currentImg.rows();
												Imgproc.resize(currentImg, currentImg, new Size(500*col_row_ratio,500));
												classifier.detectMultiScale(currentImg, faceDetections);
												
												
												//System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
												display_label.setText(String.format("Detected %s faces", faceDetections.toArray().length));
												
												Mat detectedImg = currentImg.clone();
												for (Rect rect : faceDetections.toArray()){
													Core.rectangle(detectedImg, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,255,0));
												}
												
												
												Image bufferedImg = toBufferedImage(detectedImg);
												ImageIcon imgIcon = new ImageIcon(bufferedImg);
												imgLabel.setBounds(30, 175, imgIcon.getIconWidth(), imgIcon.getIconHeight());
												//imgLabel.setIcon(null);
												imgLabel.setIcon(imgIcon);
												}
											}
											catch (NullPointerException e){
												JOptionPane.showMessageDialog(baseFrame, "Please select the classifier!", "Warning" , JOptionPane.WARNING_MESSAGE);
											}
											
										}	
										else{
											JOptionPane.showMessageDialog(baseFrame, "No image displayed!", "Warning" , JOptionPane.WARNING_MESSAGE);
										}
									}
								
								
							});
							webCamDetectionThread.start();
						}
						
						
					});
					
				}
				else{
					if (!currentImg.empty()){
						try {
							CascadeClassifier classifier = map.get(classifier_list.getSelectedValue());
							
							MatOfRect faceDetections = new MatOfRect();
							//currentImg = null;
							classifier.detectMultiScale(currentImg, faceDetections);
							
							
							//System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
							display_label.setText(String.format("Detected %s faces", faceDetections.toArray().length));
							
							Mat detectedImg = currentImg.clone();
							for (Rect rect : faceDetections.toArray()){
								Core.rectangle(detectedImg, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,255,0));
							}
							
							
							Image bufferedImg = toBufferedImage(detectedImg);
							ImageIcon imgIcon = new ImageIcon(bufferedImg);
							imgLabel.setBounds(30, 175, imgIcon.getIconWidth(), imgIcon.getIconHeight());
							//imgLabel.setIcon(null);
							imgLabel.setIcon(imgIcon);
							
						}
						catch (NullPointerException e){
							JOptionPane.showMessageDialog(baseFrame, "Please select the classifier!", "Warning" , JOptionPane.WARNING_MESSAGE);
						}
						
					}	
					else{
						JOptionPane.showMessageDialog(baseFrame, "No image displayed!", "Warning" , JOptionPane.WARNING_MESSAGE);
					}
				}
				
			}
			
		});
		
		
		// Add Stop Detection button
		JButton stop_detection_button = new JButton("Stop Detection");
		stop_detection_button.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		stop_detection_button.setBounds(620, 100, 150, 30);
		face_detection_panel.add(stop_detection_button);
		
		// Add Stop Detection button action
		stop_detection_button.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				SwingUtilities.invokeLater(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (webCamThread != null){
							webCamThread.stop();
						}
						if (webCamDetectionThread != null){
							webCamDetectionThread.stop();
						}
						if (camera != null){
							camera.release();
						}
						camera = null;
						imgLabel.setIcon(null);
						if (display_label.getText() != null){
							display_label.setText(null);
						}
					}
					
				});
			
				
				
			}
		});
		
		// Add Capture Image button
		JButton capture_image_button = new JButton("Capture Image");
		capture_image_button.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		capture_image_button.setBounds(800, 50, 140, 30);
		face_detection_panel.add(capture_image_button);
		
		// Add Exit button
		JButton exit_button = new JButton("Exit");
		exit_button.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		exit_button.setBounds(800, 100, 140, 30);
		face_detection_panel.add(exit_button);
		
		// Add Exit button action
		exit_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//setFaceDetectionPanel();
				//setWelcomePanel();
				//camera.release();
				if (webCamThread != null){
					webCamThread.stop();
				}
				if (camera != null){
					camera.release();
				}
				imgLabel.setIcon(null);
				baseFrame.getContentPane().removeAll();
				//baseFrame.remove(welcome_panel);
				//baseFrame.add(baseFrame.getContentPane());
				baseFrame.getContentPane().add(welcome_panel);
				baseFrame.revalidate();
				baseFrame.repaint();
			
				
				
			
				
				
			}
		});
		
		
		
		//face_detection_panel.setBackground(new Color(255, 0, 0));
		//face_detection_panel.setVisible(true);
	}
	
	
	private static void setSkinColorDetectionPanel(){
		
	}
	
	private static void setHairColorDetectionPanel(){
		
	}
	
	private static void setFaceRecognitionPanel(){
		
	}
	
	
	private static Image toBufferedImage(Mat m){
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
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				
				SmartFace smartFace = new SmartFace();	
			}
			
		});
		
		
		

	}

}
