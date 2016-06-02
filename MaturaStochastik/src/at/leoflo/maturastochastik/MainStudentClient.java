package at.leoflo.maturastochastik;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import at.leoflo.maturastochastik.swing.ProgressCircleUI;
import net.miginfocom.swing.MigLayout;
import java.awt.Color;

public class MainStudentClient extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainStudentClient frame = new MainStudentClient();
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
	public MainStudentClient() {
		try {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //make it look beautiful.
	} catch (Exception e) {}
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1410, 319);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[549px,grow][280px][549px]", "[227px][][][grow][]"));
		
		JButton button = new JButton("New button");
		contentPane.add(button, "cell 0 2,grow");
		
		JProgressBar progressBar = new JProgressBar(0, 1000);
		progressBar.setForeground(Color.DARK_GRAY);
		progressBar.setUI(new ProgressCircleUI());
		progressBar.setBorderPainted(false);
		contentPane.add(progressBar, "cell 1 2,grow");
		
		JButton button_1 = new JButton("New button");
		contentPane.add(button_1, "cell 2 2,grow");
		
		JPanel panel = new JPanel();
		contentPane.add(panel, "cell 0 3 3 1,grow");
	
		new Thread(() -> {
			int counter = 0;
			
			while (true) {
				progressBar.setValue(counter);
				progressBar.repaint();
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				counter++;
				counter %= 1000;
			}
		}).start();
	}
}
