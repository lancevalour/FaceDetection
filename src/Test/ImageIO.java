package Test;

import java.io.File;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class ImageIO {
	public static ArrayList<String> readImage(String folderName){
		File f = new File(folderName);
		ArrayList<String> imageNameList = TestIo.readFileName(f);
		return imageNameList;
	}
	
	public static void saveImage(Mat img, String filename){
		String fileFolderName = filename.substring(0,filename.lastIndexOf("\\"));
		File file = new File(fileFolderName);
		file.mkdirs();

		//String filename = skinColorResultDirectory + imgName;
		
		System.out.println(String.format("Writing %s", filename));
		
		Highgui.imwrite(filename, img);
	}
}
