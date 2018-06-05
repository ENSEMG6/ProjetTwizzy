import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.EventQueue;
import java.awt.Font;


public class Fenetre extends JFrame implements Runnable {

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

	static {
		try {
			System.load("C:\\Users\\Guillaume\\eclipse\\opencv\\build\\x64\\vc12\\bin\\opencv_ffmpeg2413_64.dll");
			//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		}catch(UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load. \n"+ e);
			System.exit(1);
		}
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}





	// imag va prendre la vidéo image par image ensuite l'afficher
	static Mat imag = null;
	private JPanel contentPane;
	// le thread permet de lancer la vidéo en cliquant sur un bouton
	private Thread T1;
	//lancer sert à déterminer si c'est la caméra ou la vidéo qu'on lance
	static int lancer=0;
	//si help et impéraire le bouton help fait apparaitre l'aide au deuxième clique la fenêtre disparait
	public int help=0;
	// en récuprérant la  valeur de la fonction jugement 
	//cette variable affiche le panneau de référence en bas de la fenetre lors de l'analyse de la vidéo
	public static int affiche=0;
	static String video;
	ArrayList<Integer> vitesse = new ArrayList();

	//ce tableau contient les vitesses connues dans la base de données
	int [] vitesses_dispo= {30,50,70,90,110};

	/**
	 * Create the frame.
	 */




	public Fenetre() {
		//fenetre globale et ses paramètres
		setTitle("Détecteur de panneaux");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(490, 125, 964, 790);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		//button lancement de la vidéo
		JButton btnNewButton = new JButton("Lancer la vid\u00E9o 1");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lancer=1;
				video="Video1.avi";
				start();
				
			}
		});
		//fixation du placement du button
		btnNewButton.setBounds(135, 529, 151, 42);
		//ajout du button au panel
		contentPane.add(btnNewButton);
		//button pour lancer la caméra
		
		
		
		JButton btnNewButton2 = new JButton("Lancer la vid\u00E9o 2");
		btnNewButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lancer=1;
				video="Video2.avi";
				start();
				
			}
		});
		//fixation du placement du button
		btnNewButton2.setBounds(135, 600, 151, 42);
		//ajout du button au panel
		contentPane.add(btnNewButton2);
		//button pour lancer la caméra
		
		
		JButton btnNewButton_1 = new JButton("Lancer la cam\u00E9ra");
		btnNewButton_1.setBounds(595, 529, 151, 42);
		contentPane.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lancer=0;
				start();
			}
		});
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(135, 99, 596, 385);
		lblNewLabel.setIcon(new ImageIcon("twizy.png"));
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Bienvenue dans notre Logiciel");
		lblNewLabel_1.setFont(new Font("Verdana", Font.PLAIN, 24));
		lblNewLabel_1.setBounds(300, 36, 380, 62);
		contentPane.add(lblNewLabel_1);



	}
	//implémentation du thread
	public void start(){
		T1 = new Thread(this);
		T1.start();
	}

	@Override
	public void run() {
		// le lavel qui va affihcer le panneau de référence
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBounds(312, 487, 256, 256);
		contentPane.add(lblNewLabel_2);
		// initialisation des variable de la classe utile 


		// imporatation de la base de donnée


		// configuration de la frame qui affichera la vidéo
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);    
		JFrame jframe = new JFrame("Vidéo test");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel vidpanel = new JLabel();
		jframe.setContentPane(vidpanel);
		jframe.setSize(640, 480);
		jframe.setLocation(625,180);
		jframe.setVisible(true);
		//lecture de la vidéo image par image
		Mat frame = new Mat();
		Size sz = new Size(640, 480);
		VideoCapture camera;
		//selon le bouton cliqué (caméra ou vidéo )
		//la variable lancer prend la valeur correspondante
		//afin de lancer le bon code
		// le principe est le meme poru les deux
		// lire image par image
		if(lancer==1){
			camera= new VideoCapture(video);
			jframe.setTitle("vidéo en cours de traitement");
			
		}
		else {
			camera = new VideoCapture(0);
			jframe.setTitle("camera activée");
		}
		//affichage d'image par imgage
		int i=1;
		while (camera.read(frame)) {
			if (true) {
				// la variable i comptent les frames affichées une par une pour anylser qu'une seule sur deux
				i++;
				if(true){ //permet au programe d'aller plus vite
					//la fonction extraite fait l'analyse de la frame

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

						affiche=vitesses_dispo[(int)vimax];
						System.out.println(affiche);
						if(affiche==110){
							lblNewLabel_2.setIcon(new ImageIcon("110.jpg"));
						}
						else if(affiche==90){
							lblNewLabel_2.setIcon(new ImageIcon("90.jpg"));
						}
						else if (affiche==70){
							lblNewLabel_2.setIcon(new ImageIcon("70.jpg"));
						}
						else if(affiche==50){
							lblNewLabel_2.setIcon(new ImageIcon("50.jpg"));
						}
						else if(affiche==30){
							lblNewLabel_2.setIcon(new ImageIcon("30.jpg"));
						}

					}
					//pour afficher l'image suivante : nous convertissant l'image en buffered pour
					//repaindre la même frame, ça nous évitera d'avoir des centaines de frames par vidéo
					//Imgproc.resize(frame, frame, sz);
					//Mat hsv_image1= Mat.zeros(frame.size(), frame.type());
					//Mat threshold_img1=Methodes.Seuillage(hsv_image1);
					// nous utilison la fonction contours sur origine pour afficher les contour dans la vidéo
					ImageIcon image1 = new ImageIcon(Mat2bufferedImage(frame));
					vidpanel.setIcon(image1);
					vidpanel.revalidate();
					//la condition if suivante va fermer la fenetre de la vidéo uen fois la dernière frame analysée
					if(lancer==1 && i==396 ){
						jframe.setVisible(false);
						jframe.dispose();
						lblNewLabel_2.setIcon(new ImageIcon("vide.jpg"));

					}	    		
				}





			}
		}
	}
}