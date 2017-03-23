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
public class SimulationPane extends BasicPane {
	public SimulationPane(Database db) {
		super(db);
	}
	
	@Override
	public JComponent createTopPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 3));
		
		JButton addButton = new JButton("Add Pallet");
		JTextField addTypeField = new JTextField();
		JTextField addTimeField = new JTextField();
		
		JButton removeButton = new JButton("Remove Pallet");
		JTextField removePalletIdField = new JTextField();
		JTextField removeOrderIdField = new JTextField();
		
		addButton.addActionListener(new AddListener(addTypeField,addTimeField));
		removeButton.addActionListener(new RemoveListener(removePalletIdField,removeOrderIdField));
		
		
		panel.add(new JLabel(""));
		panel.add(new JLabel("Type"));
		panel.add(new JLabel("Time"));
		panel.add(addButton);
		panel.add(addTypeField);
		panel.add(addTimeField);
		
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));
		panel.add(new JLabel(""));
		
		panel.add(new JLabel(""));
		panel.add(new JLabel("Pallet Id"));
		panel.add(new JLabel("Order Id"));
		panel.add(removeButton);
		panel.add(removePalletIdField);
		panel.add(removeOrderIdField);
		
		addTimeField.setToolTipText("yyyy-mm-dd hh:mm:ss");
		
		return panel;
    }
	
	class AddListener implements ActionListener {
		private JTextField typeField;
		private JTextField timeField;
		
		public AddListener(JTextField type, JTextField time) {
			super();
			this.typeField = type;
			this.timeField = time;
		}
		
        public void actionPerformed(ActionEvent ae) {
        	String type = typeField.getText();
        	Timestamp time = new Timestamp(System.currentTimeMillis()); 
        			
        			
        	try {
        		time = stringToTimestamp(timeField.getText());
        	} catch (CookieEmptyTimeFieldException e) {
        		
        	} catch (CookieException e) {
        		displayMessage(e);
        		return;
        	}
        	
        	try {
        		// Updates ingredients
        		db.deductIngredientsFor(type);
        		
        		// Adds the pallet to db
        		int barcode = db.addPallet(type,time);
            	
            	// Print informative message to message bar!
            	displayMessage("One pallet of "+type+" baked, packaged and placed in freezer! Barcode: "+barcode);
            	
        	} catch (CookieException e) {
        		displayMessage(e);
        	}
        }
    }
	
	class RemoveListener implements ActionListener {
		private JTextField palletIdField;
		private JTextField orderIdField;
		
		public RemoveListener(JTextField palletId, JTextField orderId) {
			super();
			this.palletIdField = palletId;
			this.orderIdField = orderId;
		}
		
        public void actionPerformed(ActionEvent ae) {
        	int palletId = -1;
        	int orderId = -1;
        	
        	try {
        		palletId = Integer.parseInt(palletIdField.getText());
        		orderId = Integer.parseInt(orderIdField.getText());
        	} catch (NumberFormatException ex) {
        		displayMessage(new CookieException(ex, "Pallet Id and order Id must be integers!"));
        		return;
        	}
        	
        	
        	try {
        		db.deliverPallet(palletId,orderId);
        		displayMessage("Took pallet "+palletId+" from Freezer and delivered to order "+orderId+".");
        	} catch (CookieException e) {
        		displayMessage(e);
        	}
        }
    }
}
