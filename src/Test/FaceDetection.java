package Test;

import java.awt.Color;
import java.awt.Image;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;


public class FaceDetection {
	
	public static void preProcess(Mat img){
		Imgproc.cvtColor(img, img, Imgproc.COLOR_BGRA2GRAY);
		Imgproc.adaptiveThreshold(img, img, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, 0, 0,1.0);
	}
	
	
	public static void faceDetect(){
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("\nRunning FaceDetection");
		
		CascadeClassifier faceDetector = new CascadeClassifier(classifierDirectory + classifierName +".xml");
		
		ArrayList<String> imageNameList = ImageIO.readImage(faceDetectionImageDirectory);
		
		for (int i = 0; i < imageNameList.size(); i++){
			
			String imgName = imageNameList.get(i);
			
			Mat img = Highgui.imread(faceDetectionImageDirectory +imgName, Highgui.CV_LOAD_IMAGE_COLOR);
			
			img = detectFaceInSingleImage(img, faceDetector);
			/*Imgproc.resize(img, img, new Size(img.cols(), img.rows()));
			
			MatOfRect faceDetections = new MatOfRect();
			faceDetector.detectMultiScale(img, faceDetections);
			
			
			System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
			
			for (Rect rect : faceDetections.toArray()){
				Core.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,255,0));
			}*/
			
			//System.out.println(faceDetections.toArray().length);
			
			ImageIO.saveImage(img, faceDetectionResultDirectory + classifierName + "\\" + imgName);
		
			
		}
		
	}
	
	public static Mat detectFaceInSingleImage(Mat img, CascadeClassifier faceDetector){
		//Imgproc.resize(img, img, new Size(img.cols(), img.rows()));
		
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(img, faceDetections);
		
		
		System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
		
		for (Rect rect : faceDetections.toArray()){
			Core.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,255,0));
		}
		
		return img;
		
	}
	
	public static void webCamFaceDetect(){
		VideoCapture camera = new VideoCapture(0);
		
		if (!camera.isOpened()){
			System.out.println("Error loading camera!");
		}
		else{
			Mat frame = new Mat();
			JFrame window = new JFrame("web cam test");
			JPanel panel = new JPanel();
			window.getContentPane().add(panel);
			JLabel imgLabel = new JLabel();
			panel.add(imgLabel);
			panel.setLayout(null);
			
			window.setSize(1000, 1000);
			window.setLocationRelativeTo(null);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setVisible(true);
			CascadeClassifier faceDetector = new CascadeClassifier(classifierDirectory + classifierName +".xml");
			
		
			
			while(camera.isOpened()){
				//System.out.println("Camera running!");
				//Imshow.show(frame);
				
				camera.retrieve(frame);
				//camera.read(frame);
				//Mat grayImg = frame.clone();
			    //Imgproc.cvtColor(frame, grayImg, Imgproc.COLOR_BGR2GRAY);
				
				//frame = detectFaceInSingleImage(frame,faceDetector);
				
				Image bufferedImg = toBufferedImage(frame);
				ImageIcon imgIcon = new ImageIcon(bufferedImg);
				
				imgLabel.setBounds(30, 200, imgIcon.getIconWidth()+10, imgIcon.getIconHeight()+25);
				imgLabel.setIcon(imgIcon);
				
			}
		}
		
		camera.release();
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
	
	public final static String classifierName = "haarcascade_frontalface_alt";
	public final static String classifierDirectory = "C:\\Users\\Yicheng\\Desktop\\work\\face recognition\\face detection trained classifier\\";
	public final static String faceDetectionImageDirectory = "C:\\Users\\Yicheng\\Desktop\\work\\face recognition\\face\\ID01\\";
	public final static String faceDetectionResultDirectory = "C:\\Users\\Yicheng\\Desktop\\work\\face recognition\\face detection result-GTAV\\";
	public final static String sideViewFaceDirectory = "C:\\Users\\Yicheng\\Desktop\\work\\face recognition\\side view face\\";
	public final static String sideViewResultDirectory = "C:\\Users\\Yicheng\\Desktop\\work\\face recognition\\side view result\\";
	public final static String sideViewContourResultDirectory = "C:\\Users\\Yicheng\\Desktop\\work\\face recognition\\side view contour result\\";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stu
		//faceDetect();
		Imshow.closeAll();
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//Mat img = Highgui.imread(sideViewFaceDirectory + "ID12_005.bmp", Highgui.CV_LOAD_IMAGE_COLOR);
		
		webCamFaceDetect();
		//Mat skinBinaryImg = skinColorThreshold(img);
		//saveImage(skinBinaryImg, skinColorResultDirectory  + "123213.jpg");
		//skinColorDetect();
		
		//Imshow.show(skinBinaryImg);
		
		/*Mat grayImg = img.clone();
		Imgproc.cvtColor(img, grayImg, Imgproc.COLOR_BGRA2GRAY);
		
		Mat binaryImg = grayImg.clone();
		Imgproc.adaptiveThreshold(grayImg, binaryImg, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 155,1);
		Imshow.show(binaryImg);*/
		
		
		//isFaceRight(img);
		
		//ArrayList<Mat> colorChannel = new ArrayList<Mat>();
		
		//Core.split(img, colorChannel);
		
		
		
		
		
		//extractSideViewContour();
		
		//skinColorDetect();
		//String filename = skinColorResultDirectory  + "1.bmp";
		
		//System.out.println(filename.substring(0,filename.lastIndexOf("\\")));
		
		//saveImage(img, skinColorResultDirectory  + "1.bmp");
		//System.out.println(isFaceRight(img));
		
	}
	
	
	
}


