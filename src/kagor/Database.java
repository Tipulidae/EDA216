package kagor;

import java.sql.*;
import java.util.*;

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

    
    public boolean userExists(String userID) {
    	boolean found = false;
    	try {
    		String sql = 
    				"SELECT username FROM User WHERE username = '"+userID+"'";
    		Statement s = conn.createStatement();
    		ResultSet rs = s.executeQuery(sql);
    		found = rs.next();
    	} catch (SQLException e) {
    		found = false;
    		//e.printStackTrace();
    	} finally {
    		
    	}
    	return found;
    }
    
    public List<String> getMovieNames() {
    	List<String> movies = new LinkedList<String>();
    	try {
    		String sql = 
    				"SELECT DISTINCT movie FROM Performance ";
    		Statement s = conn.createStatement();
    		ResultSet rs = s.executeQuery(sql);
    		while (rs.next()) {
    			movies.add(rs.getString(1));
    		}
    	} catch (SQLException e) {
    		
    	} finally {
    		
    	}
    	return movies;
    }
    
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
    }
    
    private int getFreeSeats(String movie, String date) {
    	return getMaxSeats(movie,date) - getBookedSeats(movie,date);
    }
    
    private int getBookedSeats(String movie, String date) {
    	int bookedSeats = -1;
    	try {
    		String sql = "SELECT count(*) FROM Ticket "
    				+ "WHERE movie = '"+ movie + "' and pdate = '"+date+"'"; 
    		
    		Statement s = conn.createStatement();
    		ResultSet rs = s.executeQuery(sql);
    		while (rs.next()) {
    			bookedSeats = rs.getInt(1);
      		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	} finally {
    		
    	}
    	System.out.println("bookedSeats="+bookedSeats);
    	return bookedSeats;
    }
    
    
    private int getMaxSeats(String movie, String date) {
    	int maxSeats = -1;
    	try {
    		String sql = 	"SELECT seats FROM Theater t, Performance p "
    				+ "WHERE t.name = p.theater and p.movie = '"+ movie + "' and p.pdate = '"+date+"'"; 
    		
    		Statement s = conn.createStatement();
    		ResultSet rs = s.executeQuery(sql);
    		while (rs.next()) {
    			maxSeats = rs.getInt(1);
      		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	} finally {
    		
    	}
    	System.out.println("maxSeats="+maxSeats);
    	return maxSeats;
        
    }
    
    public boolean bookTicket(String movie, String date) {
    	boolean success = false;
    	
    	try {
    		conn.setAutoCommit(false);
    		
    		if (getFreeSeats(movie, date) > 0) {
	    	
	    		String sql = 
	    				"INSERT INTO Ticket VALUES('"+(++id)+
	    				"','"+CurrentUser.instance().getCurrentUserId()+"','"+
	    				movie+"','"+date+"');";
	    		// Statement s = conn.createStatement();
	    		//PreparedStatement p = conn.prepareStatement(sql);
	    		//p.executeQuery();
	    		Statement s = conn.createStatement();
	    		conn.commit();
	    		if(s.executeUpdate(sql) > 0) {
	    			success = true;
	    		}
    		}

    	} catch (SQLException e) {
    		e.printStackTrace();
    	} finally {
    		try {
    			conn.setAutoCommit(true);
    		} catch (SQLException e) {
				System.err.println("Uh oh...");
			}
    	}
 
    	return success;
    }
}
