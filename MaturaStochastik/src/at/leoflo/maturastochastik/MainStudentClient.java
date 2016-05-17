package at.leoflo.maturastochastik;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import at.leoflo.maturastochastik.swing.TimerCanvas;

import javax.swing.JButton;
import java.awt.Canvas;
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
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1571, 467);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton button = new JButton("New button");
		button.setBounds(59, 52, 549, 227);
		contentPane.add(button);
		
		JButton button_1 = new JButton("New button");
		button_1.setBounds(950, 52, 549, 227);
		contentPane.add(button_1);
		
		TimerCanvas canvas = new TimerCanvas(new Color(255, 0, 0), new Color(255,255,255));
		canvas.setBounds(668, 52, 227, 227);
		contentPane.add(canvas);
		
		canvas.setAmount(50);
	}
}
