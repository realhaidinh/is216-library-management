package controller;
import java.sql.*;
import java.util.ArrayList;
import model.Reader;
public class ReaderDAO {
	private static ReaderDAO dao = null;
	private ReaderDAO() {

	}
	public static ReaderDAO getDAO() {
		if(dao == null) {
			dao = new ReaderDAO();
		}
		return dao;
	}
	public static void createReaderTable() throws SQLException{
		String query = "create table if not exists reader " +
				"(id string primary key, name string, phone string, birthdate string)";
		Statement statement = DatabaseConnection.getConnection().createStatement();
		statement.executeUpdate(query);
	}

	public Boolean addReader(Reader reader) {
		String query = "insert into reader values(?, ?, ?, ?)";
		try {
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			ps.setString(1, reader.getReaderId());
			ps.setString(2, reader.getName());
			ps.setString(3, reader.getPhone());
			ps.setString(4, reader.getBirthDate());
			return ps.executeUpdate() > 0;
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}
	public static Reader readerFromResult(ResultSet rs) throws SQLException {
		if(rs == null)
			return null;
		Reader reader = new Reader();
		reader.setReaderId(rs.getString("id"));
		reader.setName(rs.getString("name"));
		reader.setPhone(rs.getString("phone"));
		reader.setBirthDate(rs.getString("birthdate"));
		return reader;
	}
	public Boolean deleteById(String id) {
		String query = "delete from reader where id = ?";
		try {
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			ps.setString(1, id);
			return ps.executeUpdate() > 0;
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}
	public static ArrayList<Reader> findAllReader() {
		ArrayList<Reader> readers = new ArrayList<>();
		String query = "SELECT * FROM reader";
		try {
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				readers.add(readerFromResult(rs));
			}
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return readers;
	}
	public Reader findReaderById(String id) {
		Reader reader = null;
		String query = "SELECT * FROM reader WHERE id = ?";
		try {
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			reader = readerFromResult(rs);
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return reader;
	}
	public ArrayList<Reader> findReaderByName(String name) {
		ArrayList<Reader> readers = new ArrayList<>();
		String query = "SELECT * FROM reader WHERE name LIKE ?";
		try {
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			ps.setString(1, name + "%");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				readers.add(readerFromResult(rs));
			}
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return readers;
	}
	public Boolean updateReaderById(String id, Reader reader) {
		String query = "update reader " +
				"set name = ?, phone = ?, birthdate = ?" +
				"where id = ?";
		try {
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			ps.setString(1, reader.getName());
			ps.setString(2, reader.getPhone());
			ps.setString(3, reader.getBirthDate());
			ps.setString(4, reader.getReaderId());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}
}
