import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class interfaceGraphique extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	
	static ImageIcon ref30= new ImageIcon("ref30.jpg");
	static ImageIcon ref50= new ImageIcon("ref50.jpg");
	static ImageIcon ref70= new ImageIcon("ref70.jpg");
	static ImageIcon ref90= new ImageIcon("ref90.jpg");
	static ImageIcon ref110= new ImageIcon("ref110.jpg");
	static JLabel label = new JLabel("");
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					interfaceGraphique frame = new interfaceGraphique();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public interfaceGraphique() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JComboBox<String> comboBox = new JComboBox();
		comboBox.setBounds(45, 90, 89, 20);
		contentPane.add(comboBox);
		comboBox.addItem("video1.avi");
		comboBox.addItem("video2.avi");
		
		JButton btnTest = new JButton("Test");
		btnTest.setBounds(45, 56, 89, 23);
		contentPane.add(btnTest);
		btnTest.addActionListener((new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Video.lancer_video((String) comboBox.getSelectedItem());
			}
		}));
		
		JButton btnDirect = new JButton("Direct");
		btnDirect.setBounds(284, 56, 89, 23);
		contentPane.add(btnDirect);
		
		
		
		//label.setIcon(interfaceGraphique.ref30);
		label.setBounds(165, 113, 136, 137);
		contentPane.add(label);
		
		
 
		
	}

}
