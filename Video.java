import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
		
		
		JFrame jframe = new JFrame("Detection de panneaux sur un flux vidéo");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel vidpanel = new JLabel();
		jframe.setContentPane(vidpanel);
		jframe.setSize(720, 480);
		jframe.setVisible(true);

		Mat frame = new Mat();
		VideoCapture camera = new VideoCapture("video1.avi");
		//Mat PanneauAAnalyser = null;
		
		String p70 ="ref70.jpg";
		String p90 ="ref90.jpg";
		String p110 ="ref110.jpg";
		String p30 ="ref30.jpg";
		
		while (camera.read(frame)) {		
			
			Methodes.Super_matching(frame);
			
			double [] scores = new double [4];
			scores[0] = Methodes.matching(frame,p70);
			scores[1] = Methodes.matching(frame,p70);
			scores[2] = Methodes.matching(frame,p90);
			scores[3] = Methodes.matching(frame,p110);
			System.out.println(scores[0]);
			System.out.println(scores[1]);
			System.out.println(scores[2]);
			System.out.println(scores[3]);


			ImageIcon image = new ImageIcon(Mat2bufferedImage(frame));
			vidpanel.setIcon(image);
			vidpanel.repaint();
			
			
			
			
			
		}
		
		System.out.println(camera.read(frame));
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