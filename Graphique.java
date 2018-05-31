import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import java.awt.Panel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Button;

public class Graphique extends JFrame {

	private JPanel contentPane;
	private JTextField txtNomDeLapplication;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Graphique frame = new Graphique();
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
	public Graphique() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 763, 502);
		contentPane = new JPanel();
		contentPane.setBackground(Color.ORANGE);
		contentPane.setForeground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		txtNomDeLapplication = new JTextField();
		txtNomDeLapplication.setHorizontalAlignment(SwingConstants.CENTER);
		txtNomDeLapplication.setForeground(new Color(0, 0, 0));
		txtNomDeLapplication.setFont(new Font("Ink Free", Font.BOLD, 40));
		txtNomDeLapplication.setEditable(false);
		txtNomDeLapplication.setText("Vitesse Maximale");
		contentPane.add(txtNomDeLapplication, BorderLayout.NORTH);
		txtNomDeLapplication.setColumns(10);
		
		Panel panel = new Panel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		Button button = new Button("Test");
		button.setActionCommand("Test");
		button.setBackground(Color.RED);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				Video.lancer_video();
				
				
			}
		});
		panel.add(button);
		
		Button button_1 = new Button("Direct");
		panel.add(button_1);
	}

}
