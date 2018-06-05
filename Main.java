 
import java.awt.EventQueue;

import org.opencv.core.Core;
import org.opencv.core.Mat;



public class Main {
	
	static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    static Mat imag = null;
    

	public static void main(String[] args) {  
		// lancement de la Fenetre
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Fenetre frame = new Fenetre();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
        }
        
	}