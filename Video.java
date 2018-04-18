import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class Video {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}


	public static void lancer_video() {


		JFrame jframe = new JFrame("Detection de panneaux sur un flux vid�o");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel vidpanel = new JLabel();
		jframe.setContentPane(vidpanel);
		jframe.setSize(720, 480);
		jframe.setVisible(true);

		Mat frame = new Mat();
		VideoCapture camera = new VideoCapture("video1.avi");
		//Mat PanneauAAnalyser = null;



		while (camera.read(frame)) {
			/*
			ArrayList<Integer> vitesse = new ArrayList();

			vitesse.add(Methodes.Super_matching(frame));

			//Ici il faut analyser les donn�es pour en d�duire le panneau :

					//Il faut supprimer les -1 au sein du m�me panneau

					//regrouper les s�ries de panneau 

					//d�terminer le panneau en fonction du nb d'occrurence du panneau d�tecter


			if ((vitesse.get(vitesse.size()-1)) != -1 && vitesse.size()>6) { //on a d�tect� un panneau v�rifions si il n'a pas d�j� �tait d�tect�
				int som=0;
				int i=5;
				while (som==0 && i>0) {


					som = som + (vitesse.get(vitesse.size()-2-i)) +1; //ici le +1 compense le -1 si il y a pas de panneau d�tect�
					i--;

				}

				if (i!=0) { // ca veut dire que sur les 5 derni�res images un panneau a �t� d�tect� 
					//il faut donc supprimer les -1 entre le meme panneau
					for int (k=0;k)


			 */
			
			System.out.println(Methodes.Super_matching(frame));
			
			ImageIcon image = new ImageIcon(Mat2bufferedImage(frame));
			vidpanel.setIcon(image);
			vidpanel.repaint();

		}






		//System.out.println(vitesse.get(vitesse.size()-1));






		





	}










public static BufferedImage Mat2bufferedImage(Mat image) {


	MatOfByte bytemat = new MatOfByte();
	Highgui.imencode(".jpg", image, bytemat);
	byte[] bytes = bytemat.toArray();
	InputStream in = new ByteArrayInputStream(bytes);
	BufferedImage img = null;
	try {
		img = ImageIO.read(in);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return img;
}



}