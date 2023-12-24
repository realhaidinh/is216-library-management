package controller;
import java.sql.*;
import java.util.ArrayList;

import model.Borrowing;
import model.Reader;
import model.Book;

import javax.xml.transform.Result;

public class BorrowingDAO {
	private static BorrowingDAO dao = null;
	private BorrowingDAO() {

	}
	public static void createBorrowingTable() throws SQLException {
		String query = "create table if not exists borrowing " +
				"(id string primary key, isbn string, readerId string, borrowdate string, returndate string, " +
				"foreign key(isbn) references book (isbn) on delete cascade on update no action," +
				"foreign key(readerId) references reader (readerId) on delete cascade on update no action)";
		Statement statement = DatabaseConnection.getConnection().createStatement();
		statement.executeUpdate(query);
	}

	public static BorrowingDAO getDAO() {
		if(dao == null) {
			dao = new BorrowingDAO();
		}
		return dao;
	}
	public Boolean deleteBorrow(int id, String isbn) {
		String query = "delete from borrowing where id = ?";
		String query2 = "update book set status = true where isbn = ?";
		try{
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			PreparedStatement ps2 = DatabaseConnection.getConnection().prepareStatement(query2);
			ps.setInt(1, id);
			ps2.setString(1, isbn);
			int a = ps.executeUpdate();
			int b = ps2.executeUpdate();
			return a > 0 && b > 0;
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}
	public static Borrowing borrowingFromResult(ResultSet rs) throws SQLException {
		if(rs == null)
			return null;
		Borrowing borrowing = new Borrowing();
		borrowing.setId(rs.getString("id"));
		borrowing.setReaderId(rs.getString("readerId"));
		borrowing.setISBN(rs.getString("isbn"));
		borrowing.setBorrowDate(rs.getString("borrowdate"));
		borrowing.setReturnDate(rs.getString("returndate"));
		return borrowing;
	}
	public Boolean borrowBook(String id, String readerId, String isbn, String date) {
		try {
			Book book = BookDAO.getDAO().findBookByISBN(isbn);
			Reader reader = ReaderDAO.getDAO().findReaderById(readerId);
			if(book != null && book.getStatus() == true && reader != null) {
				String query1 = "INSERT INTO borrowing VALUES(?, ?, ?, ?, ?)";
				String query2 = "update book set status = false where isbn = ?";
				PreparedStatement ps1 = DatabaseConnection.getConnection().prepareStatement(query1);
				PreparedStatement ps2 = DatabaseConnection.getConnection().prepareStatement(query2);
				ps1.setString(1, id);
				ps1.setString(2, isbn);
				ps1.setString(3, readerId);
				ps1.setString(4, date);
				ps1.setString(5, "");
				ps2.setString(1, isbn);
				return ps1.executeUpdate() > 0 && ps2.executeUpdate() > 0;

			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}
	public Boolean returnBook(String id, String isbn, String date) {
		//idField.getText(), readerIdField.getText(), isbnField.getText()
		String query1 = "update borrowing set returndate = ? where id = ?";
		String query2 = "update book set status = true where isbn = ?";
		try {
			PreparedStatement ps1 = DatabaseConnection.getConnection().prepareStatement(query1);
			PreparedStatement ps2 = DatabaseConnection.getConnection().prepareStatement(query2);
			ps1.setString(1, date);
			ps1.setString(2, id);
			ps2.setString(1, isbn);
			return ps1.executeUpdate() > 0 && ps2.executeUpdate() > 0;
		}
		catch(SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
	public static ArrayList<Borrowing> findAllBorrowing(){
		ArrayList<Borrowing> borrowings = new ArrayList<>();
		String query = "SELECT * FROM borrowing";
		try {
		PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				borrowings.add(borrowingFromResult(rs));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return borrowings;
	}
	public ArrayList<Borrowing> findBorrowingByReaderId(String id) throws SQLException {
		ArrayList<Borrowing> borrowings = new ArrayList<>();
		String query = "SELECT * FROM borrowing where readerId = ?";
		PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			borrowings.add(borrowingFromResult(rs));
		}
		return borrowings;
	}
}
