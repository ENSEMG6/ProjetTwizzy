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

		// ouvre la fenetre
		
		JFrame jframe = new JFrame("Detection de panneaux sur un flux vidéo");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel vidpanel = new JLabel();
		jframe.setContentPane(vidpanel);
		jframe.setSize(720, 480);
		jframe.setVisible(true);

		Mat frame = new Mat();
		VideoCapture camera = new VideoCapture("video1.avi");
		//Mat PanneauAAnalyser = null;


		//on range toutes les vitesses dans une liste
		ArrayList<Integer> vitesse = new ArrayList();

		//ce tableau contient les vitesses connues dans la base de données
		int [] vitesses_dispo= {30,50,70,90,110};
		


		// tant qu'il y a une image à lire on rentre dans la bouble
		while (camera.read(frame)) {

			//camera.read(frame);
			//on ajoute la vitesse de l'image dans notre liste de vitesse
			vitesse.add(Methodes.Super_matching(frame));

			
			//on lit la derniere vitesse si un panneau a été détecté
			int n = vitesse.size();
			if (vitesse.get(n-1)!=-1){ // si -1 cela signifie qu'aucun panneau n'a été détecté
				//System.out.println(vitesse.get(n-1));
			}
			
			//mais du coup le pg affiche plusieurs fois le meme panneau
			//il faut maintenant faire en sorte de l'afficher une seule fois

			

			
			
			
			// on détermine le panneau détecté
			int vitesse_pre=0;
			int [] vi = new int [5];
			int nb_moins=0;
			int comp=0;
			double vimax=0.5;
			
			//on compte les occurence de panneau détecté
			vi[0]=0;
			vi[1]=0;
			vi[2]=0;
			vi[3]=0;
			vi[4]=0;

			if(n>12 && vitesse.get(n-11)!=-1) {
				while (nb_moins<10 && comp<11 ) {
					if (vitesse.get(n-11+comp)==30) {
						vi[0]++;
						nb_moins=0;
					}
					if (vitesse.get(n-11+comp)==50) {
						vi[1]++;
						nb_moins=0;
					}
					if (vitesse.get(n-11+comp)==70) {
						vi[2]++;
						nb_moins=0;
					}
					if (vitesse.get(n-11+comp)==90) {
						vi[3]++;
						nb_moins=0;
					}if (vitesse.get(n-11+comp)==110) {
						vi[4]++;
						nb_moins=0;
					}
					else {
						nb_moins++;
					}

					comp++;

				}

				vitesse.clear(); // on vide la liste pour ne pas compter plusieurs fois les mêmes panneaux
			}
			
			

			for(int j=0;j<vi.length;j++){
				if (vi[j]>vimax){
					vimax=vi[j];
					vimax=j;
				}
			}	


			if (vimax!=0.5) {

				if (vitesses_dispo[(int)vimax] != vitesse_pre) {
					System.out.println("Panneau "+vitesses_dispo[(int)vimax]);
				vitesse_pre=vitesses_dispo[(int)vimax];
				}



			}



			ImageIcon image = new ImageIcon(Mat2bufferedImage(frame));
			vidpanel.setIcon(image);
			vidpanel.repaint();








		}
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