package kagor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Timestamp;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import kagor.SearchPane.SearchListener;

public class QualityPane extends BasicPane {
	private JTextField productField;
	private JTextField startTimeField;
	private JTextField endTimeField;
	
	public QualityPane(Database db) {
		super(db);
	}
	
	@Override
	public JComponent createTopPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 3));
		
		productField = new JTextField();
		startTimeField = new JTextField();
		endTimeField = new JTextField();
		JButton blockButton = new JButton("Block Product!");
		blockButton.addActionListener(new BlockListener());
		
		panel.add(new JLabel("Product"));
		panel.add(new JLabel("Start time"));
		panel.add(new JLabel("End time"));
		panel.add(productField);
		panel.add(startTimeField);
		panel.add(endTimeField);
		panel.add(blockButton);
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));		
	
		startTimeField.setToolTipText("yyyy-mm-dd hh:mm:ss");
		endTimeField.setToolTipText("yyyy-mm-dd hh:mm:ss");
		
		return panel;
    }
	
	class BlockListener implements ActionListener {
		
        public void actionPerformed(ActionEvent e) {
        	String product = productField.getText();
        	Timestamp startTime = stringToTimestamp(startTimeField.getText());
        	Timestamp endTime = stringToTimestamp(endTimeField.getText());
        	
        	db.blockPallet(product, startTime, endTime);
        	
        	/*
        	String[] ts = startTime.split(" ");
        	
        	try {
            	Date startDate = Date.valueOf(ts[0]);
            	int blocked = db.blockPallet(product, startDate, new Date(System.currentTimeMillis()));

            	System.out.println(String.format("blocked: n=%d", blocked));
            	//Date endDate = Date.valueOf(ts[]);
        	} catch (IllegalArgumentException e2) {
        		errorMsg("Bad format for time. Please input yyyy-mm-dd hh:mm");
        	}*/
        		
        	
        	//int blocked = db.blockPallet(product, startDate, endDate);
        	//System.out.println(String.format("blocked: n=%d", blocked));
        	/*SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        	
        	Date startDate, endDate;
        	try {
				startDate = parser.parse(startTime);
				endDate = parser.parse(endTime);
			} catch (ParseException e1) {
				errorMsg("Bad format for time. Please input yyyy-mm-dd hh:mm");
				return;
			}*/
        	
        	System.out.println("Searching for: startTime="+startTime+", endTime="+endTime+", product="+product);
        }
    }
	
	
}
