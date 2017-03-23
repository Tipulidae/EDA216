package kagor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import database.Database;
import exceptions.CookieEmptyTimeFieldException;
import exceptions.CookieException;

@SuppressWarnings("serial")
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
		
        public void actionPerformed(ActionEvent ae) {
        	String product = productField.getText();
        	Timestamp startTime = new Timestamp(System.currentTimeMillis());
        	Timestamp endTime = startTime;
        	
        	try {
	        	startTime = stringToTimestamp(startTimeField.getText());
	        	endTime = stringToTimestamp(endTimeField.getText());
        	} catch (CookieEmptyTimeFieldException e) {
        		
        	} catch (CookieException e) {
        		displayMessage(e);
        		return;
        	}
        	try {
        		int blocked = db.blockPallet(product, startTime, endTime);
        		displayMessage("Blocked "+blocked+" pallets.");
         	} catch (CookieException e) {
        		displayMessage(e);
        	}
        }
    }
}
