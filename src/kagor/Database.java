package kagor;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Database is a class that specifies the interface to the movie
 * database. Uses JDBC.
 */
public class Database {

	private int id;
    /**
     * The database connection.
     */
    private Connection conn;

    /**
     * Create the database interface object. Connection to the
     * database is performed later.
     */
    public Database() {
        conn = null;
    }

    /**
     * Open a connection to the database, using the specified user
     * name and password.
     */
    public boolean openConnection(String filename) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + filename);
            initId();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    private void initId() {
    	try {
    		String sql = 
    				"SELECT MAX(id) FROM Ticket;";
    		Statement s = conn.createStatement();
    		ResultSet rs = s.executeQuery(sql);
    		while (rs.next()) {
    			id = rs.getInt(1);
    		}
    	} catch (SQLException e) {
    		
    	} finally {
    		
    	}
    	System.out.println("ID: " + id);
    }

    /**
     * Close the connection to the database.
     */
    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the connection to the database has been established
     * 
     * @return true if the connection has been established
     */
    public boolean isConnected() {
        return conn != null;
    }

    public boolean addPallet(String product, Timestamp time){
    	try{
    		String insertString = "INSERT INTO Pallets (cookieName, location, timestamp) VALUES (?,?,?)";
    		PreparedStatement insertStatement = conn.prepareStatement(insertString);
    		insertStatement.setString(1, product);
    		insertStatement.setString(2, "FREEZER");
    		insertStatement.setTimestamp(3, time);
    		insertStatement.executeUpdate();
    	} catch (SQLException e) {
    		System.err.println(e);
    	}
    	
    	
    	return false;
    }

	public List<String> searchPallet(String id2, Timestamp startTime, Timestamp endTime, String product, String blocked) {
		List<String> pallets = new LinkedList<String>();
		try {
			String sql = "SELECT * FROM Pallets";
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
			
			while (rs.next())
				pallets.add(rs.getString(1)+", "+rs.getString(2)+", "+rs.getString(3)+", "+rs.getString(4)+", "+rs.getString(5)+", "+rs.getString(6));
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pallets;
	}

	public void deliverPallet(String palletId, String orderId) {
		try {
			if(orderId == null || orderId.isEmpty()){
				String deleteString = "DELETE FROM Pallets WHERE palletId = ?";
				PreparedStatement deleteStatement = conn.prepareStatement(deleteString);
				deleteStatement.setString(1, palletId);
				deleteStatement.executeUpdate();
			} else {
				String updateString = "UPDATE Pallets SET location = ?, orderId = ? "
						+ "WHERE palletId = ?";
	    		PreparedStatement updateStatement = conn.prepareStatement(updateString);
	    		updateStatement.setString(1, "DELIVERED");
	    		updateStatement.setString(2, orderId);
	    		updateStatement.setString(3, palletId);
	    		updateStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public int blockPallet(String product, Timestamp startTime, Timestamp endTime){
		int blocked = 0;
		try{
			String blockString = "UPDATE Pallets SET blocked = ? WHERE cookieName = ? AND"
					+ " timestamp >= ? AND timestamp <= ?";
			PreparedStatement blockStatement = conn.prepareStatement(blockString);
			blockStatement.setBoolean(1, true);
			blockStatement.setString(2, product);
			blockStatement.setTimestamp(3, startTime);
			blockStatement.setTimestamp(4, endTime);
			//blockStatement.setDate(4, new Date(System.currentTimeMillis() - 1000000000));
			//blockStatement.setDate(3, new Date(System.currentTimeMillis() - 1000000000));
			//blockStatement.setDate(4, new Date(System.currentTimeMillis()));
			blocked = blockStatement.executeUpdate();
			//Date.
		} catch (SQLException e){
			e.printStackTrace();
		}
		return blocked;
	}
    
    /*
    public List<String> getDates(String movie) {
    	List<String> dates = new LinkedList<String>();
    	try {
    		String sql = 
    				"SELECT DISTINCT pdate FROM Performance WHERE movie = '"+movie+"'";
    		Statement s = conn.createStatement();
    		ResultSet rs = s.executeQuery(sql);
    		while (rs.next()) {
    			dates.add(rs.getString(1));
    		}
    	} catch (SQLException e) {
    		
    	} finally {
    		
    	}
    	return dates;
    }
    
    public Performance getPerformance(String movie, String date) {
    	Performance p = new Performance();
    	try {
    		String sql = 
    				"SELECT DISTINCT theater,seats "
    				+ "FROM Performance,Theater "
    				+ "WHERE movie = '"+movie+"' AND "
    				+ "pdate = '"+date+"' AND Theater.name = Performance.theater";
    		Statement s = conn.createStatement();
    		ResultSet rs = s.executeQuery(sql);
    		while (rs.next()) {
    			p.movie = movie;
    			p.date = date;
    			p.theater = rs.getString(1);
    			p.freeSeats = getFreeSeats(movie,date);
    		}
    	} catch (SQLException e) {
    		
    	} finally {
    		
    	}
    	return p;
    } */
 
	/*
	Date strToDate(String dateStr) {
		String[] ts = dateStr.split(" ");
		
	}*/
	
	
}
