package database;

import java.sql.*;
import java.util.*;

import exceptions.CookieException;
import exceptions.CookieRollbackException;

public class Database {
	private static final boolean DEBUG_MODE = true;
	private static final double COOKIES_PER_PALLET = 5400;
	private static final double COOKIES_PER_RECIPE = 100;
	private static final double INGREDIENT_MULTIPLIER = COOKIES_PER_PALLET / COOKIES_PER_RECIPE;
	
    private Connection conn;

    
    public Database() {
        conn = null;
    }

    public boolean openConnection(String filename) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + filename);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return conn != null;
    }

    public int addPallet(String product, Timestamp time) throws CookieException {
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
    		throw new CookieException(e, "Database error when trying to add pallet.");
    	}
    	
    	
    	return barcode;
    }


	public List<Object[]> searchPallet(int id, Timestamp startTime, Timestamp endTime, String product, boolean blocked) 
			throws CookieException {
		List<Object[]> pallets = new LinkedList<Object[]>();
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
			int n= rs.getMetaData().getColumnCount();
			while (rs.next()) {
				Object[] objs = new String[n];
				
				for (int j=0; j<n; j++) {
					objs[j] = rs.getString(j+1);
				}
				objs[0] = Integer.toString(rs.getInt(1));
				objs[1] = Integer.toString(rs.getInt(2));
				objs[2] = rs.getString(3);
				objs[3] = rs.getString(4);
				objs[4] = rs.getTimestamp(5).toString();
				objs[5] = Boolean.toString(rs.getBoolean(6));
				
				pallets.add(objs);
				
								
				//String str = "%16s %16s %32s %16s %32s %16s";
				//pallets.add(String.format(str, rs.getInt(1), rs.getString(2),
				//		rs.getString(3), rs.getString(4), rs.getTimestamp(5), rs.getBoolean(6)));
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CookieException(e, "There was an unexpected problem with the database. Please try again later.");
		}
		return pallets;
	}

	public void deliverPallet(int palletId, int orderId) throws CookieException {
		
		
		try {
			conn.setAutoCommit(false);
			
			String sql = "SELECT cookieName FROM Pallets WHERE palletId = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, palletId);
			ResultSet rs = ps.executeQuery();
			if (!rs.next())	throw new CookieException("No such pallet Id.");
			
			String product = rs.getString(1);
			
			String sql2 = "SELECT quantity FROM OrderItems WHERE orderId = ? AND cookieName = ?";
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			ps2.setInt(1, orderId);
			ps2.setString(2,product);
			ResultSet rs2 = ps2.executeQuery();
			if (!rs2.next()) throw new CookieException("Order doesn't exist, or doesn't need this type of cookie.");
			
			int remaining = rs2.getInt(1);
			System.out.println("There are "+remaining +" of " + product);
			
			String sql3 = "SELECT count(*) FROM Pallets WHERE orderId = ? AND cookieName = ?";
			PreparedStatement ps3 = conn.prepareStatement(sql3);
			ps3.setInt(1, orderId);
			ps3.setString(2, product);
			ResultSet rs3 = ps3.executeQuery();
			if(!rs3.next()) throw new CookieException();
			
			remaining -= rs3.getInt(1);
			System.out.println("Found "+ rs3.getInt(1) + " orders already linked.");
			
			if (remaining > 0) {
				String updateString = "UPDATE Pallets SET location = ?, orderId = ? "
						+ "WHERE palletId = ?";
	    		PreparedStatement updateStatement = conn.prepareStatement(updateString);
	    		updateStatement.setString(1, "DELIVERED");
	    		updateStatement.setInt(2, orderId);
	    		updateStatement.setInt(3, palletId);
	    		if (updateStatement.executeUpdate() <= 0)
	    			throw new CookieException();
			} else {
				throw new CookieException("The specified order already has enough cookies of type "+product);
			}
		} catch (SQLException e) {
			throw new CookieException(e);
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				throw new CookieRollbackException(e);
			}
		}
		
	}
	
	public int blockPallet(String product, Timestamp startTime, Timestamp endTime) throws CookieException {
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
			throw new CookieException(e);
		}
		return blocked;
	}

	public void deductIngredientsFor(String cookie) throws CookieException {
		try{
			conn.setAutoCommit(false);
			String recipeString = "SELECT ingredient, quantity FROM Recipes WHERE cookieName = ?";
			PreparedStatement ps = conn.prepareStatement(recipeString);
			ps.setString(1, cookie);
			ResultSet rs = ps.executeQuery();
			
			String ingredientString = "UPDATE Ingredients SET amount = amount - ? WHERE ingredient = ?";
			while(rs.next()){
				String ingredient = rs.getString(1);
				double consumed = rs.getDouble(2) * INGREDIENT_MULTIPLIER;
				
				PreparedStatement ps2 = conn.prepareStatement(ingredientString);
				ps2.setDouble(1, consumed);
				ps2.setString(2, ingredient);
				
				ps2.executeUpdate();			}
		} catch (SQLException e) {
			try{
				conn.rollback();
			} catch (SQLException exc) {
				throw new CookieRollbackException(exc);
			}
			throw new CookieException(e);
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				throw new CookieRollbackException(e);
			}
		}
		debugPrintIngredients();
	}
    
	private void debugPrintIngredients(){
		if (!DEBUG_MODE) return;
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
}
