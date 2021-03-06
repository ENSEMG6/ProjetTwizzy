import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.beans.FeatureDescriptor;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.rmi.CORBA.Stub;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Methodes {

	static String p30 ="ref30.jpg";
	static String p50 ="ref50.jpg";
	static String p70 ="ref70.jpg";
	static String p90 ="ref90.jpg";
	static String p110 ="ref110.jpg";

	public static Mat LectureImage(String fichier){					//methode pour lire une image dans m
		File f = new File(fichier);
		Mat m = Highgui.imread(f.getAbsolutePath());
		return m;
	}

	public static void ImShow (String title, Mat img){
		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".png", img, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;
		try{
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
			JFrame frame = new JFrame();
			frame.setTitle(title);
			frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
			frame.pack();
			frame.setVisible(true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static Mat Seuillage(Mat m){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat hsv_m = Mat.zeros(m.size(), m.type());
		Imgproc.cvtColor(m, hsv_m, Imgproc.COLOR_BGR2HSV);			//converti en HSV
		Mat treshold = new Mat();
		Mat treshold1 = new Mat();
		Mat treshold2 = new Mat();
		Core.inRange(hsv_m, new Scalar(0,100,100), new Scalar(10,255,255), treshold1);	// born inf,born sup, img
		Core.inRange(hsv_m, new Scalar(160,100,100), new Scalar(179,255,255), treshold2);
		Core.bitwise_or(treshold1, treshold2, treshold);
		return treshold;
	}

	public static List<MatOfPoint> DetecterContour(Mat m){
		int tresh=100;
		Mat canny = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		MatOfInt4 hierarchy = new MatOfInt4();

		Imgproc.Canny(m, canny, tresh, tresh*2);
		Imgproc.findContours(canny, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		return contours;
	}

	public static Mat DetectionCercle(Mat m, Mat treshold){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		List<MatOfPoint> contours = DetecterContour(treshold);

		MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
		float[] radius = new float[1];
		Point center = new Point();
		for (int c = 0; c < contours.size(); c++) {
			MatOfPoint contour = contours.get(c);
			double contourArea = Imgproc.contourArea(contour);
			matOfPoint2f.fromList(contour.toList());
			Imgproc.minEnclosingCircle(matOfPoint2f, center, radius);
			if((contourArea/(Math.PI*radius[0]*radius[0]))>=0.8){
				Core.circle(m, center, (int)radius[0], new Scalar(0255,0),2);
			}
		}
		return m;
	}


	/* ici tout est inutile pour le programme mais utile pour la compr�hension
	 * 
	 * 
	 * 
	 * 
	 * 

	public static void Exo1(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat m = LectureImage("opencv.png");							//lecture de l'image dans m
		for (int i = 0; i < m.height(); i++) {
			for (int j = 0; j < m.width(); j++) {
				double[] BGR = m.get(i,j);							//recupere les valeurs RGB
				if(BGR[0]==255 && BGR[1]==255 && BGR[2]==255){		//si pixel blanc
					System.out.print(".");
				}else{
					System.out.print("+");
				}
			}
			System.out.println();
		}
	}

	public static void Exo2(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat m = LectureImage("bgr.png");
		Vector<Mat> channels = new Vector<Mat>();
		Core.split(m, channels);
		for (int i = 0; i < channels.size(); i++) {
			ImShow(Integer.toString(i),channels.get(i));
		}
	}

	public static void ExoSeuil(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat m = LectureImage("circles.jpg");
		Mat hsv_m = Mat.zeros(m.size(), m.type());
		Imgproc.cvtColor(m, hsv_m, Imgproc.COLOR_BGR2HSV);			//converti en HSV
		Mat treshold = new Mat();
		Mat treshold1 = new Mat();
		Mat treshold2 = new Mat();
		Core.inRange(hsv_m, new Scalar(0,100,100), new Scalar(10,255,255), treshold1);	// born inf,born sup, img
		Core.inRange(hsv_m, new Scalar(160,100,100), new Scalar(179,255,255), treshold2);
		Core.bitwise_or(treshold1, treshold2, treshold);
		Imgproc.GaussianBlur(treshold, treshold, new Size(9,9), 2,2);					//fusion des 2 mask
		ImShow("",m);
		ImShow("Cercles Rouges",treshold);
	}

	public static void ExoContour(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat m = LectureImage("circles_rectangles.jpg");
		ImShow("Image",m);

		Mat hsv_m = Mat.zeros(m.size(), m.type());
		Imgproc.cvtColor(m, hsv_m, Imgproc.COLOR_BGR2HSV);
		ImShow("HSV",hsv_m);

		Mat treshold = Seuillage(m);
		ImShow("Seuille", treshold);

		int tresh=100;
		Mat canny = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		MatOfInt4 hierarchy = new MatOfInt4();

		Imgproc.Canny(treshold, canny, tresh, tresh*2);
		Imgproc.findContours(canny, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		Mat drawing = Mat.zeros(canny.size(), CvType.CV_8UC3);

		Random rand = new Random();

		for (int i = 0; i < contours.size(); i++) {
			Scalar color = new Scalar(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));
			Imgproc.drawContours(drawing, contours, i, color,1,8,hierarchy,0,new Point() );
		}
		ImShow("contours", drawing);

	}

	public static void ExoCercleRouge(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat m = LectureImage("circles_rectangles.jpg");
		ImShow("Image",m);

		Mat treshold = Seuillage(m);

		m=DetectionCercle(m, treshold);
		ImShow("Cercles Rouges", m);
	}

	public static double Matching_String(String route,String panneau){

		double moy=0;
		double var = 0;

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat m = LectureImage(route);
		Mat ball3 = LectureImage(panneau);
		//ImShow("Image",m);
		Mat treshold = Seuillage(m);
		List<MatOfPoint> contours = DetecterContour(treshold);
		MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
		float[] radius = new float[1];
		Point center = new Point();
		for (int c = 0; c < contours.size(); c++) {
			MatOfPoint contour = contours.get(c);
			double contourArea = Imgproc.contourArea(contour);
			matOfPoint2f.fromList(contour.toList());
			Imgproc.minEnclosingCircle(matOfPoint2f, center, radius);
			if((contourArea/(Math.PI*radius[0]*radius[0]))>=0.8){
				Core.circle(m, center, (int)radius[0], new Scalar(0255,0),2);
				Rect rect = Imgproc.boundingRect(contour);
				Core.rectangle(m, new Point(rect.x,rect.y),
						new Point(rect.x+rect.y+rect.height,rect.y+rect.height),
						new Scalar(0,255,0),2);
				Mat tmp = m.submat(rect.y,rect.y+rect.height,rect.x,rect.x+rect.width/2);
				Mat ball = Mat.zeros(tmp.size(), tmp.type());
				tmp.copyTo(ball);
				//ImShow("ball", ball);


				Mat tmp_base = ball3.submat(0,ball3.height(),0,ball3.width()/2);
				tmp_base.copyTo(ball3);

				//System.out.println(ball3.width());
				//Mise � l'�chelle
				Mat sroadSign = ball3;
				Mat object = ball;
				Mat sObject = new Mat();
				Imgproc.resize(object, sObject, sroadSign.size());

				Mat grayObject = new Mat(sObject.rows(), sObject.cols(), sObject.type());
				Imgproc.cvtColor(sObject, grayObject, Imgproc.COLOR_BGRA2GRAY);
				Core.normalize(grayObject, grayObject,0,255,Core.NORM_MINMAX);

				Mat graySign = new Mat(sroadSign.rows(), sroadSign.cols(), sroadSign.type());
				Imgproc.cvtColor(sroadSign, graySign, Imgproc.COLOR_BGRA2GRAY);
				Core.normalize(graySign, graySign, 0,255,Core.NORM_MINMAX);

				//*Extraction des caract�ristiques
				FeatureDetector orbDetector = FeatureDetector.create(FeatureDetector.FAST);
				DescriptorExtractor orbExtractor = DescriptorExtractor.create(DescriptorExtractor.BRIEF);

				MatOfKeyPoint objectKeypoints = new MatOfKeyPoint();
				orbDetector.detect(grayObject, objectKeypoints);

				MatOfKeyPoint signKeypoints = new MatOfKeyPoint();
				orbDetector.detect(graySign, signKeypoints);

				Mat objectDescriptor = new Mat(sObject.rows(), sObject.cols(), sObject.type());
				orbExtractor.compute(grayObject, objectKeypoints, objectDescriptor);

				Mat signDescriptor = new Mat(sroadSign.rows(), sroadSign.cols(), sroadSign.type());
				orbExtractor.compute(graySign, signKeypoints, signDescriptor);

				//*Matching
				MatOfDMatch matchs = new MatOfDMatch();
				DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
				matcher.match(objectDescriptor,  signDescriptor, matchs);
				//System.out.println(matchs.dump());
				int n= matchs.rows();

				for (int i=0;i<n;i++) {
					moy=moy+matchs.get(i, 0)[3];
				}
				moy=moy/n;

				for (int i =0;i<n;i++) {
					var = var+ Math.pow(matchs.get(i, 0)[3]-moy, 2);

				}
				var = var/n;

				Mat matchedImage = new Mat(sroadSign.rows(), sroadSign.cols()*2, sroadSign.type());
				Features2d.drawMatches(sObject, objectKeypoints, sroadSign, signKeypoints, matchs, matchedImage);
				ImShow("Matching", matchedImage);

			}



		}

		return var;
	}

	public static void Super_Matching_String(String route) {
		String p70 ="ref70.jpg";
		String p90 ="ref90.jpg";
		String p110 ="ref110.jpg";
		String p30 ="ref30.jpg";
		String pdouble ="refdouble.jpg";

		double m70 = Matching_String(route,p70);
		double m90 = Matching_String(route,p90);
		double m110 = Matching_String(route,p110);
		double m30 = Matching_String(route,p30);
		//double mdouble = Matching(route,pdouble);


		System.out.println("Avec le panneau 3O : "+m30 );
		System.out.println("Avec le panneau 70 : "+m70 );
		System.out.println("Avec le panneau 90 : "+m90 );
		System.out.println("Avec le panneau 110 : "+m110 );		
		//System.out.println("Avec le panneau double : "+mdouble );

	}

	 */
	public static double cut_resize (Mat m, String panneau) { // ce programme met le panneau enventuellement d�tect� � l�chelle avec le panneau de la base de donn�e

		double res=-0.5;

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat ball3 = LectureImage(panneau);
		//ImShow("Image",m);
		Mat treshold = Seuillage(m);
		List<MatOfPoint> contours = DetecterContour(treshold);
		MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
		float[] radius = new float[1];
		Point center = new Point();

		for (int c = 0; c < contours.size(); c++) {
			MatOfPoint contour = contours.get(c);
			double contourArea = Imgproc.contourArea(contour);
			matOfPoint2f.fromList(contour.toList());
			Imgproc.minEnclosingCircle(matOfPoint2f, center, radius);
			if((contourArea/(Math.PI*radius[0]*radius[0]))>=0.8){

				Core.circle(m, center, (int)radius[0], new Scalar(0255,0),2);
				Rect rect = Imgproc.boundingRect(contour);
				Core.rectangle(m, new Point(rect.x,rect.y),
						new Point(rect.x+rect.y+rect.height,rect.y+rect.height),
						new Scalar(0,255,0),2);
				Mat tmp = m.submat(rect.y,rect.y+rect.height,rect.x,rect.x+rect.width);
				Mat ball = Mat.zeros(tmp.size(), tmp.type());
				tmp.copyTo(ball);
				//ImShow("ball", ball); // affiche le panneau d�tecter

				// Permet de redimensionner l'image

				Mat tmp_base = ball3.submat(0,ball3.height(),0,ball3.width());
				tmp_base.copyTo(ball3);

				//ImShow("ball1", ball3); //affiche la base de donn�e

				//System.out.println(ball3.width());
				//Mise � l'�chelle
				Mat sroadSign = ball3;
				Mat object = ball;
				Mat sObject = new Mat();
				Imgproc.resize(object, sObject, sroadSign.size());



				//ImShow("panneau d�tect�", sObject);
				//ImShow("panneau de la base de donn�e",ball3);
				res = compare_imageRGB(sObject,ball3);



			}

		}
		return res;
	}

	public static double compare_imageRGB (Mat m , Mat pa) {
		// ce programme d�termine le taux de simulitute entre deux images
		double var=0;
		for (int i = 0; i < pa.height(); i++) { 
			for (int j = 0; j < m.width(); j++) {
				double[] BGR1 = m.get(i,j);
				double[] BGR2 = pa.get(i,j);	//recupere les valeurs RGB
				var = var + Math.abs(BGR1[0] - BGR2[0]) + Math.abs(BGR1[1] - BGR2[1]) + Math.abs(BGR1[2] - BGR2[2]);
			}
		}
		//System.out.println(var);
		return -1*var;
		
	}
	
	public static double Nb_noir (Mat pa) {
		double nbnoir = 0;
		for (int i = 0; i < pa.height(); i++) { 
			for (int j = 0; j < pa.width(); j++) {
			if((pa.get(i,j)[0]+pa.get(i,j)[1]+pa.get(i,j)[2])/3<255*0.1) {
			nbnoir++; 
		}
			}
		}
			return nbnoir;
	}


	public static double compare_imageNB (Mat m , Mat pa) { // ce programme d�termine le taux de simulitute entre deux images



		double var=0;
		double vm;
		double vpa;


		for (int i = 0; i < pa.height(); i++) { 
			for (int j = 0; j < m.width(); j++) {
				double[] BGR1 = m.get(i,j);
				double[] BGR2 = pa.get(i,j);	//recupere les valeurs RGB

				vm=(BGR1[0]+BGR1[1]+BGR1[2])/3;
				vpa=(BGR2[0]+BGR2[1]+BGR2[2])/3;

				if (vm<255*0.1) {
					vm=0;
				}
				if (vm>=255*0.1) {
					vm=1;
				}

				if (vpa<255*0.1) {
					vpa=0;
				}
				if (vpa>=255*0.1) {
					vpa=1;
				}

				if (vpa==vm) {
					var++;
				}

				

			}
		}
		System.out.println(var);
		return var;
	}





	public static int Super_matching(Mat m) {

		int res =-1;

		//On r�alise des copies car le programme matching modifie m et fausse les r�sultats pour les matchings suivants

		Mat tmp1 = new Mat();
		Mat tmp2 = new Mat();
		Mat tmp3 = new Mat();
		Mat tmp4 = new Mat();
		Mat tmp5 = new Mat();

		m.copyTo(tmp1);
		m.copyTo(tmp2);
		m.copyTo(tmp3);
		m.copyTo(tmp4);
		m.copyTo(tmp5);


		// on enregistre chacun taux de similitude dans un tableau score
		double [] scores=new double [5];

		scores[0] = (cut_resize(tmp1,p30));
		scores[1] = (cut_resize(tmp2,p50));
		scores[2] = (cut_resize(tmp3,p70));
		scores[3] = (cut_resize(tmp4,p90));
		scores[4] = (cut_resize(tmp5,p110));


		//on recherche quel est le panneau avec le plus fort taux 
		double scoremax=scores[0]; //RGB
		//double scoremax=0;
		int indexmax=-1;
		for(int j=0;j<scores.length;j++){
			if (scores[j]>scoremax){
				scoremax=scores[j];
				indexmax=j;
			}
		}
		
		
		//on en d�duit la panneau qui a �t� d�tect�
		//System.out.println(indexmax);

		if (indexmax !=-1) {
			
			

			if (indexmax==0) {
				res=30;

			}
			if (indexmax==1) {
				res=50;

			}
			if (indexmax==2) {
				res=70;
			}
			if (indexmax==3) {
				res=90;

			}
			if (indexmax==4) {
				res=110;
			}
			if (scores[0]==-9307827.0) {
				res=50;
			}
			if (scores[0]==-8365355.0) {
				res=50;
			}
		}



		return res;


	}

}

