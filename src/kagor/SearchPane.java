package kagor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import database.Database;
import exceptions.CookieEmptyTimeFieldException;
import exceptions.CookieException;

@SuppressWarnings("serial")
public class SearchPane extends BasicPane {
	
	private JTextField idField;
	private JTextField productField;
	private JTextField startTimeField;
	private JTextField endTimeField;
	private JCheckBox blockedCheckBox;
	
	private PalletTableModel ptm;
	
	
	public SearchPane(Database db) {
		super(db);
	}
	
	@Override
	public JComponent createTopPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 4));
		
		idField = new JTextField();
		productField = new JTextField();
		startTimeField = new JTextField();
		endTimeField = new JTextField();
		blockedCheckBox = new JCheckBox("Blocked");
		JButton searchButton = new JButton("Search!");
		searchButton.addActionListener(new SearchListener());
		
		panel.add(new JLabel("Id"));
		panel.add(new JLabel("Product"));
		panel.add(new JLabel("Start time"));
		panel.add(new JLabel("End time"));
		panel.add(idField);
		panel.add(productField);
		panel.add(startTimeField);
		panel.add(endTimeField);
		
		panel.add(blockedCheckBox);
		
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));
		
		panel.add(searchButton);
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));
		
		startTimeField.setToolTipText("yyyy-mm-dd hh:mm:ss");
		endTimeField.setToolTipText("yyyy-mm-dd hh:mm:ss");
	
		return panel;
    }
	
	@Override
	public JComponent createMiddlePanel() {
		ptm = new PalletTableModel();
		JTable table = new JTable(ptm);
		JScrollPane sp = new JScrollPane(table);
		table.setEnabled(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		return sp;
	}
	
	class SearchListener implements ActionListener {
		
        public void actionPerformed(ActionEvent ae) {
        	
        	 
        	
        	int id = -1;
        	if (!idField.getText().isEmpty()) {
	        	try {
	        		id = Integer.valueOf(idField.getText());
	        	} catch (NumberFormatException ex) {
	        		displayMessage(new CookieException(ex, "Pallet id must be an integer or empty."));
	        		return;
	        	}
        	}
        	
        	Timestamp startTime=null,endTime=null;
        	try {
	        	startTime = stringToTimestamp(startTimeField.getText());
	        	endTime = stringToTimestamp(endTimeField.getText());
        	} catch (CookieEmptyTimeFieldException e) {
        		
        	} catch (CookieException e) {
        		displayMessage(e);
        		return;
        	}
        	String product = productField.getText();
        	
        	try {
        	   	List<Object[]> pallets = db.searchPallet(id,startTime,endTime,product,blockedCheckBox.isSelected());
        	   	ptm.setData(pallets);
        	   	displayMessage("Found "+pallets.size()+" pallets.");
        	} catch (CookieException e) {
        		displayMessage(e);
        	}
        	   	
        	   	
        }
    }
	
}
