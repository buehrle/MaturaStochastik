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

import javax.swing.JButton;
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

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class Main extends JFrame implements KeyListener, ActionListener {
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
		setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (table.getSelectedRow() != -1) {
			if (e.getKeyCode() == 10 && table.getSelectedRow() == table.getRowCount() -1) { //ENTER
				topicsTableModel.addRow(new Object[] {"", ""});
				try {
					new Robot().keyPress(KeyEvent.VK_LEFT);
				} catch (AWTException e1) {}
			} else if (e.getKeyCode() == 127) { //DEL
				if (table.getRowCount() > 1) topicsTableModel.removeRow(table.getSelectedRow());
			} else if (e.getKeyCode() == 8) { //BACK
				
			}
		}
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
		}
	}
}
