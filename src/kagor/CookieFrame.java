package kagor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.SoftBevelBorder;

import database.Database;

@SuppressWarnings("serial")
public class CookieFrame extends JFrame {
	
	private Database db;
	private JLabel statusLabel;
	
	public CookieFrame(String title, Database db) {
		super(title);
		this.db = db;
		JTabbedPane tabbedPane = new JTabbedPane();
        
        
        SimulationPane simp = new SimulationPane(db);
        SearchPane sp = new SearchPane(db);
        QualityPane qp = new QualityPane(db);
        
        tabbedPane.addTab("Pallet Search", null, sp, "Search for pallets");
        tabbedPane.addTab("Quality Assessment", null, qp, "Block pallets that doesn't meet the quality standards");
        tabbedPane.addTab("Simulation", null, simp, "Simulate pallet production and delivery");
        
        tabbedPane.setSelectedIndex(0);
        
        add(tabbedPane, BorderLayout.CENTER);

        addWindowListener(new WindowHandler());

        
        
        statusLabel = new JLabel("Herro!");
        add(statusLabel, BorderLayout.SOUTH);
        statusLabel.setBorder
        (new CompoundBorder(new SoftBevelBorder(BevelBorder.RAISED),
                            statusLabel.getBorder()));
        
        setSize(800, 400);
        setVisible(true);

        displayMessage("Connecting to database", Color.BLACK);
        if (db.openConnection("kagor.db")) {
            displayMessage("Connected to database.", Color.BLACK);
        } else {
            displayMessage("Could not connect to database.", Color.RED);
        }
	}
	
	class WindowHandler extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            db.closeConnection();
            System.exit(0);
        }
    }
	
	public void displayMessage(String msg, Color color) {
		statusLabel.setForeground(color);
		statusLabel.setText(msg);
    }
}
