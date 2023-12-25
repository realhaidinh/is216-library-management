package controller;
import java.sql.*;

public class DatabaseConnection {
	private static Connection conn = null;
	private DatabaseConnection() {

	}
	public static Connection getConnection() {
		if(conn == null) {
			try {
				conn = DriverManager.getConnection("jdbc:sqlite:database.db");
				conn.prepareStatement("PRAGMA foreign_keys=ON").execute();
			}
			catch(SQLException e) {
				System.err.println(e.getMessage());
			}
		}
		return conn;
	}
	public static void disconnect() {
		try {
			if(conn != null) {
				conn.close();
				conn = null;
			}
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
}
