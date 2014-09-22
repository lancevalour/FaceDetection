package Test;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;



public class HelloOpenCV {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat mat = Mat.ones(3, 2,CvType.CV_16S);
		System.out.println("mat = " + mat.dump());

	}

}
