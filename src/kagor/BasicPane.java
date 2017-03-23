package kagor;

import javax.swing.*;

import database.Database;
import exceptions.CookieEmptyTimeFieldException;
import exceptions.CookieException;

import java.awt.*;
import java.sql.Timestamp;

@SuppressWarnings("serial")
public class BasicPane extends JPanel {
    protected Database db;
    private static final Color DEFAULT_MSG_COLOR = Color.BLACK;
    
    public BasicPane(Database db) {
        this.db = db;
        
        setLayout(new BorderLayout());
                
        JComponent leftPanel = createLeftPanel();
        add(leftPanel, BorderLayout.WEST);
                
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
                
        JComponent topPanel = createTopPanel();
        JComponent middlePanel = createMiddlePanel();
        
        rightPanel.add(topPanel, BorderLayout.NORTH);
        rightPanel.add(middlePanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.CENTER);
    }
        
    
    protected JComponent createLeftPanel() { 
        return new JPanel();
    }
    
    protected JComponent createTopPanel() { 
        return new JPanel();
    }
    
    protected JComponent createMiddlePanel() { 
        return new JPanel();
    }
    
    
    public void entryActions() {
    	
    }
    
    protected void displayMessage(CookieException e) {
    	e.printStackTrace();
    	displayMessage(e.getMessage(), Color.RED);
    }
    
    protected void displayMessage(String msg) {
    	displayMessage(msg, DEFAULT_MSG_COLOR);
    }
    
    protected void displayMessage(String msg, Color color) {
    	((CookieFrame)getTopLevelAncestor()).displayMessage(msg, color);
    }
    
    protected Timestamp stringToTimestamp(String time) throws CookieException {
    	if (time == null || time.isEmpty()) {
    		throw new CookieEmptyTimeFieldException();
    	}
    	try {
    		return Timestamp.valueOf(time);
    	} catch (IllegalArgumentException e) {
    		throw new CookieException(e,"Invalid format in time field. Use yyyy-mm-dd hh:mm:ss.");
    	}
    }
    
    protected void errorMessage(String msg, String title) {
    	JOptionPane.showMessageDialog((JFrame) SwingUtilities.getRoot(this),
    		    msg, title , JOptionPane.ERROR_MESSAGE);
    }
}
