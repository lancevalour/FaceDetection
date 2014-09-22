package Test;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class SideViewFaceDetection {
	public static void extractSideViewContour(){
		System.out.println("\nRunning extractSideViewContour");
		ArrayList<String> imageNameList = ImageIO.readImage(sideViewFaceDirectory);
		
		for (int i = 0; i < imageNameList.size(); i++){
			String imgName = imageNameList.get(i);
			
			Mat img = Highgui.imread(sideViewFaceDirectory +imgName, Highgui.CV_LOAD_IMAGE_COLOR);
			
			if (!HairColorDetection.isFacingRight(img)){
				Core.flip(img, img, 1);
			}
			
			img = faceContourDetection(img);
			
			ImageIO.saveImage(img, sideViewContourResultDirectory  + imgName);
		}
	}
	
	
	
	private static Mat faceContourDetection(Mat img){
		Mat grayImg = img.clone();
		Imgproc.cvtColor(img, grayImg, Imgproc.COLOR_BGRA2GRAY);
		//Imshow.show(grayImg);
		Mat hsvImg = img.clone();
		Imgproc.cvtColor(img, hsvImg, Imgproc.COLOR_BGR2HSV);
		//Imshow.show(hsvImg);
		
		Mat equalizedImg = grayImg.clone();
		
		Imgproc.equalizeHist(grayImg, equalizedImg);
		//Imshow.show(equalizedImg);
		
		Mat cannyEdgedImg = grayImg.clone();
		Imgproc.Canny(grayImg, cannyEdgedImg, 200, 50);
		
		
		return cannyEdgedImg;
	}
	
	
	public final static String sideViewFaceDirectory = "C:\\Users\\Yicheng\\Desktop\\work\\face recognition\\side view face\\";
	public final static String sideViewContourResultDirectory = "C:\\Users\\Yicheng\\Desktop\\work\\face recognition\\side view contour result\\";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		extractSideViewContour();
		
	}

}
