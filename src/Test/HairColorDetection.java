package Test;


import java.util.ArrayList;



import java.util.Collections;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class HairColorDetection {
	
	//Based on color space information, threshold original RGB image into binary image
	private static Mat skinColorThreshold(Mat img){
		img = adjustBrightness(img);
		Mat resultImg = new Mat(img.rows(), img.cols(), 3);
		
		Mat YCrCbImg = img.clone();
		Imgproc.cvtColor(img, YCrCbImg, Imgproc.COLOR_BGR2HSV);
		
		ArrayList<Mat> YCrCB_List = new ArrayList<Mat>();
		Core.split(YCrCbImg, YCrCB_List);
		Mat H_channel = YCrCB_List.get(0);
		Mat S_channel = YCrCB_List.get(1);
		Mat V_channel = YCrCB_List.get(2);
		
		Mat H_channel_binImg = img.clone();
		Mat S_channel_binImg = img.clone();
		Mat V_channel_binImg = img.clone();
		
		Imgproc.adaptiveThreshold(V_channel, resultImg, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 281,55);
	
		
	/*	for (int i = 0; i<img.rows(); i++){
			for (int j = 0; j< img.cols(); j++){
				
				//if (Cr_channel.get(i, j)[0] >=146 && Cr_channel.get(i,j)[0]<=183 && CB_channel.get(i,j)[0] >=77 && CB_channel.get(i, j)[0]<=127 ){
				if (Y_channel.get(i, j)[0] >= 5 && Y_channel.get(i, j)[0]<=20){
					resultImg.put(i, j, new double[]{ 255} );
				}
				else{
					resultImg.put(i, j, new double[]{ 0} );
				}
			}
		}*/
		
		Core.bitwise_not(resultImg, resultImg);

		
		Imgproc.morphologyEx(resultImg, resultImg, Imgproc.MORPH_DILATE, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10,10)));
		Imgproc.medianBlur(resultImg, resultImg,35);
		
		return resultImg;
	}
	

	
	//Calculate the horizontal histogram of the image
	private static ArrayList<Integer> calculateHorizontalHistogram(Mat img){
		//int [] horizontalHist = new int [img.cols()];
		ArrayList<Integer> horizontalHist = new ArrayList<Integer>();
		
		for (int i = 0; i< img.cols(); i++){
			int temp = 0;
			for (int j = 0; j < img.rows(); j++){
				temp += img.get(j, i)[0]/255;
			}
			//System.out.println(temp);
			horizontalHist.add(temp);
			//horizontalHist[i] = temp;
		}
		
		return horizontalHist;		
	}
	
	
	//Calculate the vertical histogram of the image
	private static ArrayList<Integer> calculateVerticalHistogram(Mat img){
		ArrayList<Integer> verticalHist = new ArrayList<Integer>();
		
		for (int i = 0; i< img.rows(); i++){
			int temp = 0;
			for (int j = 0; j < img.cols(); j++){
				temp += img.get(i, j)[0]/255;
			}
			verticalHist.add(temp);
		}
		
		return verticalHist;
		
	}
	
	
	//Segment the rear head ROI
	private static Mat segmentRearHeadROI(Mat img){
		ArrayList<Integer> verticalHist = calculateVerticalHistogram(img);	
		
		
		int y;
		for (y= 0; y< verticalHist.size(); y++){
			if (verticalHist.get(y) >(double)Collections.max(verticalHist)*0.5){
				break;
			}
		}
		
		boolean isBold = false;
		if (y > img.rows()/2){
			isBold = true;
			return boldSkinColorThreshold(img);
		}
		else{
		
		
		int height;
		for (height = y+1; height < verticalHist.size(); height++){
			if (verticalHist.get(height) < (double)Collections.max(verticalHist)*0.63){
				break;
			}
		}
		
		if (height-y<5){
			height = y + 100;
		}
		
	
		Mat heightCutImg = img.submat(new Rect(0, y, img.cols(), height-y));
		
		ArrayList<Integer> horizontalHist = calculateHorizontalHistogram(heightCutImg);
		
		int x;
		for (x = 0; x < horizontalHist.size(); x++ ){
			if (horizontalHist.get(x) > (double)Collections.max(horizontalHist)*0.5){
				break;
			}
		}
		
		int width;
		for (width = x+1; width < horizontalHist.size(); width++){
			if (horizontalHist.get(width) < (double)Collections.max(horizontalHist)*0.2){
				break;
			}
		}
		
		Mat finalCutImg = heightCutImg.submat(new Rect(x, 0, width - x, heightCutImg.rows()));
		
		
		finalCutImg = removeIsolatedSmallBlocks(finalCutImg);
		Imgproc.cvtColor(finalCutImg, finalCutImg, Imgproc.COLOR_GRAY2BGR);
		
		Core.line(finalCutImg, new Point(0, finalCutImg.rows()/2), new Point(finalCutImg.cols(), finalCutImg.rows()/2), CV_RGB(0,0,255));
		Core.line(finalCutImg, new Point(finalCutImg.cols()/2, 0), new Point(finalCutImg.cols()/2, finalCutImg.rows()), CV_RGB(0, 0, 255));
		
		return finalCutImg;
		
		}
		
	}
	
	
	//Remove the small color blocks in the image
	private static Mat removeIsolatedSmallBlocks(Mat img){
		Imgproc.morphologyEx(img, img, Imgproc.MORPH_ERODE, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(35,35)));
		Imgproc.morphologyEx(img, img, Imgproc.MORPH_DILATE, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(30,30)));
		return img;
	}
	
	
	//Bold threshold method
	private static Mat boldSkinColorThreshold(Mat img){
		Imgproc.morphologyEx(img, img, Imgproc.MORPH_ERODE, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10,10)));
		
		ArrayList<Integer> horizontalHist = calculateHorizontalHistogram(img);
		
		int x;
		for (x = 0; x < horizontalHist.size(); x++ ){
			if (horizontalHist.get(x) > (double)Collections.max(horizontalHist)*0.5){
				break;
			}
		}
		
		int width;
		for (width = x+1; width < horizontalHist.size(); width++){
			if (horizontalHist.get(width) < (double)Collections.max(horizontalHist)*0.1){
				break;
			}
		}
		
		Mat widthCutImg = img.submat(new Rect(x, 0, width - x, img.rows()));
		
		return widthCutImg;
	}
	
	
	//Calculate the sum of the pixels of a matrix
	private static int sumOfMat(Mat img){
		int sum = 0;
		for (int i = 0; i < img.rows(); i++){
			for (int j = 0; j< img.cols(); j++){
				sum += img.get(i, j)[0];
			}
		}
		
		return sum;
	}
	
	
	
	//Draw the horizontal histogram of the image
	private static Mat drawHorizontalHistrogram(Mat img){
		ArrayList<Integer> horizontalHist = calculateHorizontalHistogram(img);
		
		int hist_height = img.rows();
		int hist_width = img.cols()+20;
		
		Mat histImage = new Mat(hist_height, hist_width,CvType.CV_8UC3);
		
		for (int i = 0; i < img.cols(); i++){
			Core.line(histImage, new Point(i+1, hist_height), 
					new Point(i+1, hist_height - horizontalHist.get(i)),
					CV_RGB(0,255,0),2, 8, 0);
		}
		int mean = mean(horizontalHist);
		
		
		Core.line(histImage, new Point(0, hist_height - mean/255), new Point(hist_width, hist_height-mean/255), CV_RGB(0,0,255), 2, 8, 0);
		return histImage;
	
	}
	
	
	
	//Judge if the face is facing right 
	public static boolean isFacingRight(Mat img){
		
		//Judge if facing right using horizontal histogram 
/*		img = skinColorThreshold(img);
		ArrayList<Integer> horizontalHist = calculateHorizontalHistogram(img);
		
		int meanOfHorizontalHist = mean(horizontalHist);
		
		ArrayList<Integer> truncatedHorizontalHist = new ArrayList<Integer>();
		for (int i = 0; i < horizontalHist.size(); i++){
			if (horizontalHist.get(i) > 10){
				truncatedHorizontalHist.add(horizontalHist.get(i));
			}
		}
		
		int maxOfTruncatedHorizontalHist = Collections.max(truncatedHorizontalHist);
		
		int indexOfMaxOfTruncatedHorizontalHist = truncatedHorizontalHist.indexOf(maxOfTruncatedHorizontalHist);
		
		return indexOfMaxOfTruncatedHorizontalHist < truncatedHorizontalHist.size()/2;*/
		
		
		
		//Judge if facing right by calculating the direction of color block
		img = skinColorThreshold(img);
		Mat finalCutImg = segmentRearHeadROI(img);
		int upperLeftArea = sumOfMat(finalCutImg.submat(new Rect(0, 0, finalCutImg.cols()/2, finalCutImg.rows()/2)));
		int upperRightArea = sumOfMat(finalCutImg.submat(new Rect(finalCutImg.cols()/2, 0, finalCutImg.cols()/2, finalCutImg.rows()/2)));
		int lowerLeftArea = sumOfMat(finalCutImg.submat(new Rect(0, finalCutImg.rows()/2, finalCutImg.cols()/2, finalCutImg.rows()/2)));
		int lowerRightArea = sumOfMat(finalCutImg.submat(new Rect(finalCutImg.cols()/2, finalCutImg.rows()/2, finalCutImg.cols()/2, finalCutImg.rows()/2)));
		
		return (upperLeftArea + lowerRightArea) < (upperRightArea + lowerLeftArea);
		
	}
	
	
	
	//Calculate the mean of the ArrayList
	private static int mean(ArrayList<Integer> list){
		int sum = 0;
		for (int i = 0; i < list.size(); i++){
			sum += list.get(i);
		}
		return (int)sum/list.size();
	}
	
	
	
	
	//Run all images to detect the region of skin, save the result to the target file
	public static void skinColorDetect(){
		System.out.println("\nRunning skinColorDetection");
		ArrayList<String> imageNameList = ImageIO.readImage(sideViewFaceDirectory);
		
		for (int i = 0; i < imageNameList.size(); i++){
			String imgName = imageNameList.get(i);
			
			Mat img = Highgui.imread(sideViewFaceDirectory +imgName, Highgui.CV_LOAD_IMAGE_COLOR);
			
			//boolean isFacingRight = isFacingRight(img);
			Mat resultImg = skinColorThreshold(img);
			//resultImg = segmentRearHeadROI(resultImg);
		
			ImageIO.saveImage(resultImg, hairColorThresholdResultDirectory  + imgName);
		}
	}
	
	public static void rearHeadROIDetect(){
		System.out.println("\nRunning rearHeadROIDetection");
		ArrayList<String> imageNameList = ImageIO.readImage(sideViewFaceDirectory);
		
		for (int i = 0; i < imageNameList.size(); i++){
			String imgName = imageNameList.get(i);
			
			Mat img = Highgui.imread(sideViewFaceDirectory +imgName, Highgui.CV_LOAD_IMAGE_COLOR);
			
			//boolean isFacingRight = isFacingRight(img);
			Mat resultImg = skinColorThreshold(img);
			resultImg = segmentRearHeadROI(resultImg);
		
			ImageIO.saveImage(resultImg, rearHeadROIResultDirectory  + imgName);
		}
	}
	
	
	
	public static void faceDirectionDetect(){
		System.out.println("\nRunning faceDirectionDetection");
		ArrayList<String> imageNameList = ImageIO.readImage(sideViewFaceDirectory);
		
		for (int i = 0; i < imageNameList.size(); i++){
			String imgName = imageNameList.get(i);
			
			Mat img = Highgui.imread(sideViewFaceDirectory +imgName, Highgui.CV_LOAD_IMAGE_COLOR);
			
		
			//Mat resultImg = skinColorThreshold(img);
			boolean isFacingRight = isFacingRight(img);
		
			ImageIO.saveImage(img, faceDirectionResultDirectory + isFacingRight + imgName);
		}
	}
	
	
	
	//Calculate the histogram of the gray-scale image
	private static void calculateHistogram(Mat img){
		ArrayList<Mat> images = new ArrayList<Mat>();
		Core.split(img, images);
		ArrayList<Mat> images1 = new ArrayList<Mat>();
		images1.add(images.get(0));
		
		MatOfInt  histSize = new MatOfInt(256);
		
		MatOfFloat histRange = new MatOfFloat(0f, 255f);
		
		boolean accumulate = false;
		
		Mat hist = new Mat();
		
		Imgproc.calcHist(images1, new MatOfInt(0), new Mat(), hist, histSize, histRange, accumulate);
		
		//ImageIO.saveImage(hist, histogramResultDirectory + "1.jpg");
		//Imshow.show(hist);
		
		int hist_width = 512;
		int hist_height = 600;
		long bin_w;
		bin_w = Math.round((double) (hist_width/256));
		
		Mat histImage = new Mat(hist_height, hist_width,CvType.CV_8UC3);
		
		Core.normalize(hist, hist,3, histImage.rows(), Core.NORM_MINMAX);
		
		for (int i = 1; i< 256; i++ ){
			Core.line(histImage, new Point(bin_w*(i-1), hist_height - Math.round(hist.get(i-1,0)[0])), 
					new Point(bin_w*(i), hist_height - Math.round(Math.round(hist.get(i, 0)[0]))),
					CV_RGB(0,255,0),2, 8, 0);
			
		}
		
		//return histImage;
		
		Imshow.show(histImage);
		
		
	}
	
	
	//CV_RGB function, generate the color 
	private static Scalar CV_RGB(int i, int j, int k) {
		// TODO Auto-generated method stub
		return new Scalar(i, j, k);
	}
	
	//Run all image and calculate histogram and save 
	public static void saveHistogram(){
		System.out.println("\nRunning calculate histogram");
		ArrayList<String> imageNameList = ImageIO.readImage(sideViewFaceDirectory);
		
		for (int i = 0; i < imageNameList.size(); i++){
			String imgName = imageNameList.get(i);
			
			Mat img = Highgui.imread(sideViewFaceDirectory +imgName, Highgui.CV_LOAD_IMAGE_COLOR);
			
			//boolean isFacingRight = isFacingRight(img);
			Mat resultImg = skinColorThreshold(img);
			Mat histImg = drawHorizontalHistrogram(resultImg);
		
			ImageIO.saveImage(histImg, histogramResultDirectory  + imgName);
		}
	}
	
	
	
	//Calculate the sum of the brightness value of a single RGB image 
	private static ArrayList<Long> calBrightnessSum(Mat img){
		ArrayList<Mat> RGB_channels = new ArrayList<Mat>();
		Core.split(img, RGB_channels);
		
		ArrayList<Long> Bsum = new ArrayList<Long>();
		
		for (int channel = 0; channel < 3; channel++){
			long temp = 0;
			for (int i = 0; i < img.rows(); i++){
				for (int j = 0; j < img.cols(); j++){
					temp += RGB_channels.get(channel).get(i, j)[0];
				}
			}
			Bsum.add(temp);
		}
		return Bsum;
	}
	
	
	//Adjust the brightness of a single RGB image
	private static Mat adjustBrightness(Mat img){
		ArrayList<Long> Bsum = calBrightnessSum(img);
		
		double [] channel_ratios = new double[3];
		
		for (int i = 0; i< 3; i++){
			channel_ratios[i] = (double)standard_RGB_brightness[i]/Bsum.get(i);
		}
		
		/*for (int channel = 0; channel< 3; channel++){*/
			for (int i = 0; i < img.rows(); i++){
				for (int j = 0; j< img.cols(); j++){
					double [] origPix = img.get(i, j);
					img.put(i, j, new double []{origPix[0]*channel_ratios[0], origPix[1]*channel_ratios[1], origPix[2]*channel_ratios[2]});
				}
			}
//		}
		
		return img;
	}
	
	
	
	
	public final static String sideViewFaceDirectory = "C:\\Users\\Yicheng\\Desktop\\work\\face recognition\\side view face\\";
	public final static String hairColorResultDirectory = "C:\\Users\\Yicheng\\Desktop\\work\\face recognition\\hair color result\\";
	public final static String histogramResultDirectory = "C:\\Users\\Yicheng\\Desktop\\work\\face recognition\\hair color histogram result\\";
	public final static String hairColorThresholdResultDirectory = "C:\\Users\\Yicheng\\Desktop\\work\\face recognition\\hair color threshhold result\\";
	public final static long [] standard_RGB_brightness = new long []{12170019, 12007717, 12736623};
	public final static String faceDirectionResultDirectory = "C:\\Users\\Yicheng\\Desktop\\work\\face recognition\\face direction result\\";
	public final static String rearHeadROIResultDirectory = "C:\\Users\\Yicheng\\Desktop\\work\\face recognition\\rear head ROI result\\";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
/*		Mat img = Highgui.imread(sideViewFaceDirectory + "ID01_005.bmp", Highgui.CV_LOAD_IMAGE_COLOR);
		System.out.println(calBrightnessSum(img));
		
		Mat img1 = Highgui.imread(sideViewFaceDirectory + "ID43_027.bmp", Highgui.CV_LOAD_IMAGE_COLOR);
		System.out.println(calBrightnessSum(img1));
		img1= adjustBrightness(img1);
		//Imshow.show(img1);
		for (int i = 0; i< result.length; i++){
			System.out.println(result[i]);
		}
		
		
		System.out.println(calBrightnessSum(img1));
		Imshow.show(img1);*/
		//ImageIO.saveImage(img1, skinColorThresholdResultDirectory  + "1");
		
		/*Mat grayImg = img.clone();
		Imgproc.cvtColor(img, grayImg, Imgproc.COLOR_BGRA2GRAY);
		Mat equImg = grayImg.clone();
		Imgproc.equalizeHist(grayImg, equImg);
		
		calculateHistogram(equImg);
		
		calculateHistogram(grayImg);*/
	/*	Mat img1 = Highgui.imread(sideViewFaceDirectory + "ID06_005.bmp", Highgui.CV_LOAD_IMAGE_COLOR);
		Mat img2 = Highgui.imread(sideViewFaceDirectory + "ID06_009.bmp", Highgui.CV_LOAD_IMAGE_COLOR);
		Mat img3 = Highgui.imread(sideViewFaceDirectory + "ID06_014.bmp", Highgui.CV_LOAD_IMAGE_COLOR);
		Mat img4 = Highgui.imread(sideViewFaceDirectory + "ID06_018.bmp", Highgui.CV_LOAD_IMAGE_COLOR);
		Mat img5 = Highgui.imread(sideViewFaceDirectory + "ID06_023.bmp", Highgui.CV_LOAD_IMAGE_COLOR);
		Mat img6 = Highgui.imread(sideViewFaceDirectory + "ID06_027.bmp", Highgui.CV_LOAD_IMAGE_COLOR);
		
		
		Mat resultImg1 = skinColorThreshold(img1);
		Mat resultImg2 = skinColorThreshold(img2);
		Mat resultImg3 = skinColorThreshold(img3);
		Mat resultImg4 = skinColorThreshold(img4);
		Mat resultImg5 = skinColorThreshold(img5);
		Mat resultImg6 = skinColorThreshold(img6);
		
		Mat img11 = Highgui.imread(sideViewFaceDirectory + "ID32_005.bmp", Highgui.CV_LOAD_IMAGE_COLOR);
		Mat img12 = Highgui.imread(sideViewFaceDirectory + "ID32_009.bmp", Highgui.CV_LOAD_IMAGE_COLOR);
		Mat img13 = Highgui.imread(sideViewFaceDirectory + "ID32_014.bmp", Highgui.CV_LOAD_IMAGE_COLOR);
		Mat img14 = Highgui.imread(sideViewFaceDirectory + "ID32_018.bmp", Highgui.CV_LOAD_IMAGE_COLOR);
		Mat img15 = Highgui.imread(sideViewFaceDirectory + "ID32_023.bmp", Highgui.CV_LOAD_IMAGE_COLOR);
		Mat img16 = Highgui.imread(sideViewFaceDirectory + "ID32_027.bmp", Highgui.CV_LOAD_IMAGE_COLOR);
		
		
		Mat resultImg11 = skinColorThreshold(img11);
		Mat resultImg12 = skinColorThreshold(img12);
		Mat resultImg13 = skinColorThreshold(img13);
		Mat resultImg14 = skinColorThreshold(img14);
		Mat resultImg15 = skinColorThreshold(img15);
		Mat resultImg16 = skinColorThreshold(img16);*/
		//skinColorDetect();
		Mat img1 = Highgui.imread(sideViewFaceDirectory + "ID32_018.bmp", Highgui.CV_LOAD_IMAGE_COLOR);
	/*	Mat resultImg = skinColorThreshold(img1);
		resultImg = segmentRearHeadROI(resultImg);
		resultImg = removeIsolatedSmallBlocks(resultImg);
		Imshow.show(resultImg);*/
		Mat resultImg = skinColorThreshold(img1);
		resultImg = segmentRearHeadROI(resultImg);
		System.out.println(String.valueOf(isFacingRight(img1)));
		Imshow.show(resultImg);
		//Mat resultImg1 = skinColorThreshold(img1);
		//System.out.println(calculateVerticalHistogram(resultImg1));
	   // resultImg1 = segmentRearHeadROI(resultImg1);
		//Imshow.show(resultImg1);
		//Mat resultImg11 = skinColorThreshold(img1);
		
		//System.out.println(calculateHorizontalHistogram(resultImg1));
		
		//drawHorizontalHistrogram(resultImg1);
		//faceDirectionDetect();
		
		//System.out.println(String.valueOf(isFacingRight(img1)));
		//skinColorDetect();
		//saveHistogram();
		//skinColorDetect();
		//rearHeadROIDetect();
		faceDirectionDetect();
		
		
		
		
	}

}
