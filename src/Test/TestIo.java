package Test;

import java.io.*;
import java.util.ArrayList;


public class TestIo {
	public static ArrayList<File> readFile(File f){
		if (!f.isDirectory()){
			System.out.println("Invalid file folder name, please check directory name.");
		}
		ArrayList<File> result = new ArrayList<File>();
		
		return readFileHelper(f, result);
	
	}
	
	public static ArrayList<File> readFileHelper(File f, ArrayList<File> list){
		ArrayList<File> result = new ArrayList<File>(list);
		if (!f.isDirectory()){
			System.out.println("Invalid file folder name, please check directory name.");
		}
		else{
			for (int i = 0; i< f.listFiles().length; i++){
				if (f.listFiles()[i].isDirectory()){
					result = readFileHelper(f.listFiles()[i], result);
				}
				else{
					if (isImageFile(f.listFiles()[i])){
						result.add(f.listFiles()[i]);
					}
				}
				
			}
		}
		return result;	
	}
	
	public static boolean isImageFile(File file){
		String fileName = file.getName();
		return fileName.endsWith(".bmp") || fileName.endsWith(".jpg") || fileName.endsWith(".JPG");
	}
	
	public static ArrayList<String> readFileName(File f){
		ArrayList<File> fileList = readFile(f);
		
		ArrayList<String> fileNameList = new ArrayList<String>();
		
		for (File file : fileList ){
			fileNameList.add(file.getName());
		}
		
		return fileNameList;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File f = new File("C:\\Users\\Yicheng\\Desktop\\work\\face recognition\\side view face");
		ArrayList<String> list = readFileName(f);
		System.out.println(list);
	}

}
