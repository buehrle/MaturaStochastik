package at.leoflo.maturastochastik;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import at.leoflo.maturastochastik.networking.Client;
import at.leoflo.maturastochastik.networking.ClientCommunicator;
import at.leoflo.maturastochastik.swing.ProgressCircleUI;
import net.miginfocom.swing.MigLayout;
import java.awt.Color;

public class MainStudentClient extends JFrame implements ClientCommunicator, ActionListener {


	private static final long serialVersionUID = -5741292959178077563L;
	
	
	private JPanel contentPane;
	private Client connection;
	
	private int relapseTime;
	private int ID[];
	
	private JButton leftButton;
	private JButton rightButton;
	
	private int counter;
	
	
	
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
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //make it look like the os
		} catch (Exception e) {}
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1410, 319);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[549px,grow][280px][549px]", "[227px][][][grow][]"));
		
		leftButton = new JButton("New button");
		contentPane.add(leftButton, "cell 0 2,grow");
		
		JProgressBar progressBar = new JProgressBar(0, 1000);
		progressBar.setForeground(Color.DARK_GRAY);
		progressBar.setUI(new ProgressCircleUI());
		progressBar.setBorderPainted(false);
		contentPane.add(progressBar, "cell 1 2,grow");
		
		rightButton = new JButton("New button");
		contentPane.add(rightButton, "cell 2 2,grow");
		
		JPanel panel = new JPanel();
		contentPane.add(panel, "cell 0 3 3 1,grow");
	
		
		new Client("10.51.50.22", this);
		
		
		new Thread(() -> {
			counter = 0;
			
			while (relapseTime != 0) {
				progressBar.setValue(counter);
				progressBar.repaint();
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				counter += 1000 / relapseTime;
				counter %= 1000;
			}
		}).start();
	}

	@Override
	public void updateQuestions(int[] ID, String[] questions) {
		leftButton.setText(ID[0] + ": " + questions[0]);
		rightButton.setText(ID[1] + ": " + questions[1]);
		
		this.ID = ID;
		
	}

	@Override
	public void resetTimer() {
		counter = 0;
	}

	@Override
	public void connected() {
		relapseTime = connection.getRelapseTime();
		
	}

	@Override
	public void disconnected() {
		this.dispose();
		System.exit(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.equals(leftButton)) {
			//Send ID[0]
			connection.sendResult(ID[0]);
		}
		
		if (e.equals(rightButton)) {
			//Send ID[1]
			connection.sendResult(ID[1]);
		}
		
	}
}
