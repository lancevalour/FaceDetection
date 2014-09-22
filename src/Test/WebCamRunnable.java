/**
 * 
 */
package Test;

/**
 * @author Yicheng
 *
 */
public class WebCamRunnable implements Runnable{

	/**
	 * @param args
	 */
	
	public void run(){
		System.out.println("Hello");
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread webCamThread = new Thread(new WebCamRunnable());
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		webCamThread.start(); 
	}

}
