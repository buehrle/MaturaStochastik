package at.leoflo.maturastochastik;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import at.leoflo.maturastochastik.networking.Client;
import at.leoflo.maturastochastik.networking.ClientCommunicator;
import at.leoflo.maturastochastik.swing.ProgressCircleUI;
import net.miginfocom.swing.MigLayout;

public class MainStudentClient extends JFrame implements ClientCommunicator, ActionListener, WindowListener {


	private static final long serialVersionUID = -5741292959178077563L;
	
	
	private JPanel contentPane;
	private Client connection;
	
	private int relapseTime;
	private int ID[];
	
	private JButton leftButton;
	private JButton rightButton;
	
	private Thread timer;
	
	private JProgressBar progressBar;
	
	
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
		leftButton.addActionListener(this);
		contentPane.add(leftButton, "cell 0 2,grow");
		
		 progressBar = new JProgressBar();
		progressBar.setForeground(Color.DARK_GRAY);
		progressBar.setUI(new ProgressCircleUI());
		progressBar.setBorderPainted(false);
		contentPane.add(progressBar, "cell 1 2,grow");
		
		rightButton = new JButton("New button");
		rightButton.addActionListener(this);
		contentPane.add(rightButton, "cell 2 2,grow");
		
		JPanel panel = new JPanel();
		contentPane.add(panel, "cell 0 3 3 1,grow");
		
		this.addWindowListener(this);
	
		boolean everythingOk = false;
		String ip = (String) JOptionPane.showInputDialog(this, "Bitte gib die IP-Adresse ein, die der Lehrer dir mitgeteilt hat.", "IP-Adresse", JOptionPane.PLAIN_MESSAGE, null, null, "127.0.0.1");
		
		while (!everythingOk) {
			if (ip != null) {
				try {
					connection = new Client(ip, this);
					connection.start();
					everythingOk = true;
				} catch (UnknownHostException e) {
					ip = (String) JOptionPane.showInputDialog(this, "Das hat nicht funktioniert ¯\\_(ツ)_/¯", "IP-Adresse", JOptionPane.PLAIN_MESSAGE, null, null, "127.0.0.1");
				} catch (IOException e) {
					close();
				}
				
			} else {
				close();
			}
		}
		timer = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				progressBar.setValue((progressBar.getValue() % (relapseTime * 100)) + 1);
				progressBar.repaint();
				
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
	}

	@Override
	public void updateQuestions(int[] ID, String[] questions) {
		leftButton.setText(questions[0]);
		rightButton.setText(questions[1]);
		
		this.ID = ID;
		
	}

	@Override
	public void resetTimer() {
		progressBar.setValue(0);
	}

	@Override
	public void connected() {
		relapseTime = connection.getRelapseTime();
		progressBar.setMinimum(0);
		progressBar.setMaximum(relapseTime * 100);
		timer.start();
	}

	@Override
	public void disconnected() {
		close();
		//System.exit(0);
	}
	
	private void close() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == leftButton) {
			//Send ID[0]
			connection.sendResult(ID[0]);
			//System.out.println("ACTION PERFORMED LEFT");
		}
		
		if (e.getSource() == rightButton) {
			//Send ID[1]
			connection.sendResult(ID[1]);
		}
		
		//System.out.println("ACTION PERFORMED");
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		if (timer != null) timer.interrupt();
		if (connection != null) connection.interrupt();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
