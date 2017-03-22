package kagor;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


public class CookieGUI {
	private Database db;
	private JTabbedPane tabbedPane;
	public CookieGUI(Database db) {
		this.db = db;
		
		JFrame frame = new JFrame("Crusty Cookies");
        tabbedPane = new JTabbedPane();
        
        SearchPane sp = new SearchPane(db);
        QualityPane qp = new QualityPane(db);
        SimulationPane simp = new SimulationPane(db);
        
        tabbedPane.addTab("Pallet Search", null, sp, "Search for pallets");
        tabbedPane.addTab("Quality Assessment", null, qp, "Block pallets that doesn't meet the quality standards");
        tabbedPane.addTab("Simulation", null, simp, "Simulate pallet production and delivery");
        
        tabbedPane.setSelectedIndex(0);
        
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addChangeListener(new ChangeHandler());
        frame.addWindowListener(new WindowHandler());

        frame.setSize(800, 400);
        frame.setVisible(true);

        sp.displayMessage("Connecting to database ...");
        

        if (db.openConnection("kagor.db")) {
            sp.displayMessage("Connected to database");
            qp.displayMessage("Connected to database");
            simp.displayMessage("Connected to database");
            System.out.println("Great success!");
        } else {
            sp.displayMessage("Could not connect to database");
            qp.displayMessage("Could not connect to database");
            simp.displayMessage("Could not connect to database");
        }
	}
	
	class ChangeHandler implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            BasicPane selectedPane = (BasicPane) tabbedPane
                .getSelectedComponent();
            selectedPane.entryActions();
        }
    }


    class WindowHandler extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            db.closeConnection();
            System.exit(0);
        }
    }
	
	/*
    private Database db;
    private JTabbedPane tabbedPane;

    public CookieGUI(Database db) {
        this.db = db;

        JFrame frame = new JFrame("MovieBooking");
        tabbedPane = new JTabbedPane();

        UserLoginPane userLoginPane = new UserLoginPane(db);
        tabbedPane.addTab("User login", null, userLoginPane,
                          "Log in as a new user");

        BookingPane bookingPane = new BookingPane(db);
        tabbedPane.addTab("Book ticket", null, bookingPane, "Book a ticket");

        tabbedPane.setSelectedIndex(0);

        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addChangeListener(new ChangeHandler());
        frame.addWindowListener(new WindowHandler());

        frame.setSize(500, 400);
        frame.setVisible(true);

        userLoginPane.displayMessage("Connecting to database ...");
                

        if (db.openConnection("kagor.db")) {
            userLoginPane.displayMessage("Connected to database");
        } else {
            userLoginPane.displayMessage("Could not connect to database");
        }
    }


    class ChangeHandler implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            BasicPane selectedPane = (BasicPane) tabbedPane
                .getSelectedComponent();
            selectedPane.entryActions();
        }
    }


    class WindowHandler extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            db.closeConnection();
            System.exit(0);
        }
    }
    */
}
