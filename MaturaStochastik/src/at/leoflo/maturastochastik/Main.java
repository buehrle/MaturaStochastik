package at.leoflo.maturastochastik;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox.KeySelectionManager;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import at.leoflo.maturastochastik.networking.PollCoordinator;
import at.leoflo.maturastochastik.networking.PollServer;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class Main extends JFrame implements KeyListener, ActionListener, PollCoordinator {
	private static final String[] topicsColumnNames = {"Bezeichnung", "Beliebtheit"};
	private static final String[] resultsColumnNames = {"Themenbereich", "Anzahl Fragen", "insgesamt Gezogen", "Sicherheit"};
	private DefaultTableModel topicsTableModel;
	private DefaultTableModel resultsTableModel;
	private JTable table;
	private JPanel topicContainer;
	private JPanel dataContainer;
	private JLabel lblDurchgngegenauigkeit;
	private JSpinner spinner_1;
	private JSpinner spinner;
	private JLabel lblSicherheit;
	private JSpinner spinner_2;
	private JLabel label;
	private JPanel resultContainer;
	private JButton btnBerechnen;
	private JScrollPane scrollPane_1;
	private JTable table_1;
	private JPanel panel;
	private JPanel pollContainer;
	private JButton btnStartPoll;
	private JLabel lblZeitProAuswahl;
	private JSpinner spinner_3;
	private JLabel lblVerbundeneSchler;
	private JLabel label_1;
	
	private PollServer server;
	
	public static void main(String[] args) {
		new Main();
	}
	
	public Main() {
		BorderLayout borderLayout = (BorderLayout) getContentPane().getLayout();
		borderLayout.setHgap(5);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //make it look beautiful.
		} catch (Exception e) {}
		
		setTitle("Stochastische Berechnung für die Verteilung der Maturafragen | HTL Dornbirn");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setSize(912, 414);
		setMinimumSize(new Dimension(912, 414));
		
		topicContainer = new JPanel();
		topicContainer.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Themenbereiche", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(topicContainer, BorderLayout.WEST);
		topicContainer.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		topicContainer.add(scrollPane);
		
		topicsTableModel = new DefaultTableModel(topicsColumnNames, 1);
		resultsTableModel = new DefaultTableModel(resultsColumnNames, 0);
		
		table = new JTable(topicsTableModel);
		table.setFillsViewportHeight(true);
		table.getColumnModel().getColumn(0).setPreferredWidth(145);
		table.getColumnModel().getColumn(1).setPreferredWidth(53);
		table.setCellSelectionEnabled(true);
		table.setRowHeight(20);
		table.addKeyListener(this);
		scrollPane.setViewportView(table);
		
		dataContainer = new JPanel();
		getContentPane().add(dataContainer, BorderLayout.NORTH);
		dataContainer.setLayout(new MigLayout("", "[][][][][][][][][][][grow][][][][][][][][][]", "[grow]"));
		
		JLabel lblAnzahlDerSchler = new JLabel("Anzahl Schüler");
		dataContainer.add(lblAnzahlDerSchler, "cell 0 0");
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		spinner.setMinimumSize(new Dimension(60, 0));
		dataContainer.add(spinner, "cell 1 0");
		
		lblDurchgngegenauigkeit = new JLabel("Durchgänge (Genauigkeit)");
		dataContainer.add(lblDurchgngegenauigkeit, "cell 3 0");
		
		spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(1000, 1, null, 1));
		spinner_1.setMinimumSize(new Dimension(100, 0));
		dataContainer.add(spinner_1, "cell 4 0");
		
		lblSicherheit = new JLabel("Sicherheit >");
		dataContainer.add(lblSicherheit, "cell 6 0");
		
		spinner_2 = new JSpinner();
		spinner_2.setModel(new SpinnerNumberModel(70, 50, 100, 1));
		dataContainer.add(spinner_2, "cell 7 0");
		
		label = new JLabel("%");
		dataContainer.add(label, "cell 8 0");
		
		btnBerechnen = new JButton("Berechnen");
		btnBerechnen.addActionListener(this);
		
		panel = new JPanel();
		dataContainer.add(panel, "cell 10 0 9 1,grow");
		dataContainer.add(btnBerechnen, "cell 19 0,alignx right,aligny top");
		
		resultContainer = new JPanel();
		resultContainer.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Ergebnisse", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(resultContainer, BorderLayout.CENTER);
		resultContainer.setLayout(new BorderLayout(0, 0));
		
		scrollPane_1 = new JScrollPane();
		resultContainer.add(scrollPane_1, BorderLayout.CENTER);
		
		table_1 = new JTable(resultsTableModel);
		table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_1.setEnabled(false);
		table_1.getColumnModel().getColumn(0).setPreferredWidth(180);
		table_1.getColumnModel().getColumn(1).setPreferredWidth(85);
		table_1.getColumnModel().getColumn(2).setPreferredWidth(113);
		table_1.getColumnModel().getColumn(3).setPreferredWidth(60);
		table_1.setFillsViewportHeight(true);
		table_1.setRowHeight(20);
		scrollPane_1.setViewportView(table_1);
		
		pollContainer = new JPanel();
		getContentPane().add(pollContainer, BorderLayout.SOUTH);
		pollContainer.setLayout(new MigLayout("", "[127px][][][]", "[25px][][]"));
		
		btnStartPoll = new JButton("Umfrage starten");
		btnStartPoll.addActionListener(this);
		pollContainer.add(btnStartPoll, "cell 0 0,alignx left,aligny top");
		
		lblZeitProAuswahl = new JLabel("Zeit pro Auswahl [s]");
		pollContainer.add(lblZeitProAuswahl, "cell 2 0");
		
		spinner_3 = new JSpinner();
		spinner_3.setModel(new SpinnerNumberModel(new Integer(10), new Integer(1), null, new Integer(1)));
		spinner_3.setMinimumSize(new Dimension(100, 0));
		pollContainer.add(spinner_3, "cell 3 0");
		
		lblVerbundeneSchler = new JLabel("Verbundene Schüler:");
		pollContainer.add(lblVerbundeneSchler, "cell 0 2");
		
		label_1 = new JLabel("0");
		pollContainer.add(label_1, "cell 1 2");
		setVisible(true);
		
		server = null;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (table.getSelectedRow() != -1) {
			if (e.getKeyCode() == 10 && table.getSelectedRow() == table.getRowCount() -1) { //ENTER
				topicsTableModel.addRow(new Object[] {"", ""});
				try {
					new Robot().keyPress(KeyEvent.VK_LEFT);
				} catch (AWTException e1) {}
			} else if (e.getKeyCode() == 127 || e.getKeyCode() == 92) { //DEL
				if (table.getRowCount() > 1) topicsTableModel.removeRow(table.getSelectedRow());
			}
		}
		
		//btnBerechnen.setText(Integer.toString(e.getKeyCode()));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnBerechnen) {
			ArrayList<Topic> topics = new ArrayList<Topic>();
			
			for (int i = 0; i < topicsTableModel.getRowCount(); i++) {
				String topicName = (String) topicsTableModel.getValueAt(i, 0);
				String topicPopularity = (String) topicsTableModel.getValueAt(i, 1);
				
				if (topicName == null ? false : topicName != "" && topicPopularity == null ? false : topicPopularity.matches("[-+]?\\d*\\.?\\d+")) {
					topics.add(new Topic(topicName, Integer.parseInt(topicPopularity)));
				} else {
					return;
				}
			}
			
			MathUtils.stochasticate(topics, (Integer) spinner.getValue(), (Integer) spinner_1.getValue(), (Integer) spinner_2.getValue());
			
			while (resultsTableModel.getRowCount() > 0) {
				resultsTableModel.removeRow(0);
			}
			for (int i = 0; i < topics.size(); i++) {
				resultsTableModel.addRow(new Object[]{topics.get(i).getName(), topics.get(i).getAmountQuestions(), topics.get(i).getAmountChosen(), Math.round(topics.get(i).getPercentage() * 10.0) / 10.0 + " %"});
			}
		} else if (e.getSource() == btnStartPoll) {
			if (server == null) {
				
				if (topicsTableModel.getRowCount() > 1) {
					btnStartPoll.setText("Umfrage beenden");
					spinner_3.setEnabled(false);
					table.setEnabled(false);
					
					server = new PollServer(10000, hashMapFromTopics(), this, (Integer) spinner_3.getValue());
					server.start();
				}
			} else {
				btnStartPoll.setText("Umfrage starten");
				spinner_3.setEnabled(true);
				table.setEnabled(true);
				
				server.interrupt();
				server = null;
			}
		}
	}

	@Override
	public void topicIncreased(int topic) {
		Object valueAt = topicsTableModel.getValueAt(topic, 1);
		
		if (valueAt != null) {
			topicsTableModel.setValueAt((Integer) valueAt + 1, topic, 1);
		} else {
			topicsTableModel.setValueAt(1, topic, 1);
		}
		
		
		System.out.println("Inkreased: " + topic);
	}

	@Override
	public void clientCountUpdate(int count) {
		label_1.setText(String.valueOf(count));
	}
	
	private HashMap<Integer, String> hashMapFromTopics() {
		HashMap<Integer, String> temp = new HashMap<Integer, String>();
		
		for (int i = 0; i < topicsTableModel.getRowCount(); i++) {
			temp.put(i, (String) topicsTableModel.getValueAt(i, 0));
		}
		
		return temp;
	}
}
