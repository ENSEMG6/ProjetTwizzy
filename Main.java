import java.util.Arrays;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Main {


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println(Methodes.Matching("ref70.jpg","ref70.jpg"));
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//Methodes.Super_Matching("route4.jpg");
		
		Video.lancer_video();
		
		Mat m = Methodes.LectureImage("route.jpg");
		
		//System.out.println(m.size());
		//System.out.println(m())
		//System.out.println(Methodes.cut_resize(m,"ref30.jpg"));
		


	}

}