package kagor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import kagor.SimulationPane.AddListener;
import kagor.SimulationPane.RemoveListener;

public class SearchPane extends BasicPane {
	private JTextField idField;
	private JTextField productField;
	private JTextField startTimeField;
	private JTextField endTimeField;
	private JCheckBox blockedCheckBox;
	
	private JList<String> searchList;
	private DefaultListModel<String> searchListModel;
	
	
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
		searchListModel = new DefaultListModel<String>();
        searchList = new JList<String>(searchListModel);
        for(int i = 0; i < 20; i++){
        	searchListModel.addElement("element " + (i+1));
        }
        //searchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //nameList.setPrototypeCellValue("123456789012");
        //nameList.addListSelectionListener(new NameSelectionListener());
        JScrollPane p1 = new JScrollPane(searchList);
        return p1;
	}
	
	class SearchListener implements ActionListener {
		
        public void actionPerformed(ActionEvent e) {
        	String id = idField.getText();
        	Timestamp startTime = stringToTimestamp(startTimeField.getText());
        	Timestamp endTime = stringToTimestamp(endTimeField.getText());
        	String product = productField.getText();
        	String blocked = (blockedCheckBox.isSelected() ? "Blocked!" : "Unblocked");
        	
        	searchListModel.clear();
        	for (String p : db.searchPallet(id,startTime,endTime,product,blocked)) 
        		searchListModel.addElement(p);
        	/*SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        	
        	Date startDate, endDate;
        	try {
				startDate = parser.parse(startTime);
				endDate = parser.parse(endTime);
			} catch (ParseException e1) {
				errorMsg("Bad format for time. Please input yyyy-mm-dd hh:mm");
				return;
			}*/
        	
        	System.out.println("Searching for: id="+id+", startTime="+startTime+", endTime="+endTime+", product="+product+", blocked="+blocked);
        }
    }
	
	private void errorMsg(String msg) {
    	JOptionPane.showMessageDialog((JFrame) SwingUtilities.getRoot(this),
    		    msg, "You Done Fucked up:" , JOptionPane.ERROR_MESSAGE);
    }
	
}
