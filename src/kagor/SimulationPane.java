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

public class SimulationPane extends BasicPane {
	public SimulationPane(Database db) {
		super(db);
	}
	
	@Override
	public JComponent createTopPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 3));
		
		//panel.add(new JButton);
		
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
		
        public void actionPerformed(ActionEvent e) {
        	String type = typeField.getText();
        	Timestamp time = stringToTimestamp(timeField.getText());
        	System.out.println("Adding "+type+", "+time);
        	
        	
        	db.addPallet(type, time);
        	// Create pallet in db (this creates the "barcode", aka palletId)
        	// Then add pallet to freezer (update pallet location)
        	// db.addPalletToFreezer(type,time);
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
		
        public void actionPerformed(ActionEvent e) {
        	String palletId = palletIdField.getText();
        	String orderId = orderIdField.getText();
        	System.out.println("Removing "+palletId+", "+orderId);
        	
        	
        	db.deliverPallet(palletId,orderId);
        	// Do stuff!
        	// db.addPalletToFreezer(type,time);
        }
    }
}
