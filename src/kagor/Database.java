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
	private static final double COOKIES_PER_PALLET = 5400;
	private static final double COOKIES_PER_RECIPE = 100;
	
	private static final double INGREDIENT_MULTIPLIER = COOKIES_PER_PALLET / COOKIES_PER_RECIPE;
	
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

    public int addPallet(String product, Timestamp time){
    	int barcode = -1;
    	try{
    		String insertString = "INSERT INTO Pallets (cookieName, location, timestamp) VALUES (?,?,?)";
    		PreparedStatement insertStatement = conn.prepareStatement(insertString);
    		insertStatement.setString(1, product);
    		insertStatement.setString(2, "FREEZER");
    		insertStatement.setTimestamp(3, time);
    		insertStatement.executeUpdate();
    		
    		ResultSet rs = insertStatement.getGeneratedKeys();
    		rs.next();
    		barcode = rs.getInt(1);
    		
    	} catch (SQLException e) {
    		System.err.println(e);
    	}
    	
    	
    	return barcode;
    }


	public List<String> searchPallet(int id, Timestamp startTime, Timestamp endTime, String product, boolean blocked) {
		List<String> pallets = new LinkedList<String>();
		
		try {
			boolean[] params = new boolean[4];
			
			if (id >= 0) params[0] = true;
			if (startTime != null) params[1] = true;
			if (endTime != null) params[2] = true;
			if (!"".equals(product)) params[3] = true;
			
			StringBuilder sb = new StringBuilder("SELECT * FROM Pallets WHERE ");
			if (params[0]) sb.append("palletId = ? AND ");
			if (params[1]) sb.append("timestamp >= ? AND ");
			if (params[2]) sb.append("timestamp <= ? AND ");
			if (params[3]) sb.append("cookieName = ? AND ");
			
			sb.append("blocked = ? ");
			//sb.append("ORDER BY (cookieName, timestamp)");
			
			System.out.println("prepared Statement = "+sb);
			
			PreparedStatement ps = conn.prepareStatement(sb.toString());
			int i = 1;
			if (params[0]) ps.setInt(i++,id);
			if (params[1]) ps.setTimestamp(i++, startTime);
			if (params[2]) ps.setTimestamp(i++, endTime);
			if (params[3]) ps.setString(i++, product);
			ps.setBoolean(i, blocked);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String str = "%16s %16s %32s %16s %32s %16s";
				pallets.add(String.format(str, rs.getString(1), rs.getString(2),
						rs.getString(3), rs.getString(4), rs.getTimestamp(5), rs.getBoolean(6)));
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pallets;
	}

	public boolean deliverPallet(int palletId, int orderId) {
		
		
		boolean success = false;
		try {
			conn.setAutoCommit(false);
			
			String sql = "SELECT cookieName FROM Pallets WHERE palletId = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, palletId);
			ResultSet rs = ps.executeQuery();
			if (!rs.next())	return false;
			
			String product = rs.getString(1);
			System.out.println("Found product = " + product);
			
			String sql2 = "SELECT quantity FROM OrderItems WHERE orderId = ? AND cookieName = ?";
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			ps2.setInt(1, orderId);
			ps2.setString(2,product);
			ResultSet rs2 = ps2.executeQuery();
			if (!rs2.next()) return false;
			
			int remaining = rs2.getInt(1);
			System.out.println("There are "+remaining +" of " + product);
			
			String sql3 = "SELECT count(*) FROM Pallets WHERE orderId = ?";
			PreparedStatement ps3 = conn.prepareStatement(sql3);
			ps3.setInt(1, orderId);
			ResultSet rs3 = ps3.executeQuery();
			if(!rs3.next()) return false;
			
			remaining -= rs3.getInt(1);
			System.out.println("Found "+ rs3.getInt(1) + " orders already linked.");
			
			if (remaining > 0) {
				String updateString = "UPDATE Pallets SET location = ?, orderId = ? "
						+ "WHERE palletId = ?";
	    		PreparedStatement updateStatement = conn.prepareStatement(updateString);
	    		updateStatement.setString(1, "DELIVERED");
	    		updateStatement.setInt(2, orderId);
	    		updateStatement.setInt(3, palletId);
	    		success = updateStatement.executeUpdate() > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				System.err.println("Vem vare som fucking kasta=!=!=!");
				e.printStackTrace();
			}
		}
		return success;
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
			blocked = blockStatement.executeUpdate();
		} catch (SQLException e){
			e.printStackTrace();
		}
		return blocked;
	}

	public void makeOnePalletOf(String type) {
		try{
			conn.setAutoCommit(false);
			String recipeString = "SELECT ingredient, quantity FROM Recipes WHERE cookieName = ?";
			PreparedStatement ps = conn.prepareStatement(recipeString);
			ps.setString(1, type);
			ResultSet rs = ps.executeQuery();
			
			String ingredientString = "UPDATE Ingredients SET amount = amount - ? WHERE ingredient = ?";
			while(rs.next()){
				String ingredient = rs.getString(1);
				double consumed = rs.getDouble(2) * INGREDIENT_MULTIPLIER;
				
				PreparedStatement ps2 = conn.prepareStatement(ingredientString);
				ps2.setDouble(1, consumed);
				ps2.setString(2, ingredient);
				
				int updated = ps2.executeUpdate();
				System.out.println("Updated=" + updated + ", Removed "+ consumed + " from " + ingredient);
			}
		} catch (SQLException e) {
			try{
				conn.rollback();
			} catch (SQLException exc) {
				exc.printStackTrace();
			}
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				System.out.println("Vem var det som kasta!??");
				e.printStackTrace();
			}
		}
		printIngredients();
		
		// Calculate how many of each ingredient is needed to make one pallet of cookie type
		// Then remove these ingredients from the database.
		// TODO Auto-generated method stub
		
	}
    
	private void printIngredients(){
		String sql = "SELECT ingredient, amount FROM Ingredients";
		try{
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()){
				String ingredient = rs.getString(1);
				double amount = rs.getDouble(2);
				System.out.printf("%s : %.3f\n", ingredient,amount);
			}
		} catch (SQLException e){
			
		}
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
