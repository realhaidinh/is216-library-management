package controller;
import java.sql.*;
import java.util.ArrayList;

import model.Borrowing;
import model.Reader;
import model.Book;
public class BorrowingDAO {
	public BorrowingDAO() {

	}
	public static void createBorrowingTable() throws SQLException {
		String query = "create table if not exists book " +
				"(isbn string , id string, borrowdate string, returndate string, primary key (isbn, id)," +
				"foreign key(isbn) references book (isbn) on delete cascade on update no action," +
				"foreign key(id) references reader (id) on delete cascade on update no action)";
		Statement statement = DatabaseConnection.getConnection().createStatement();
		statement.executeUpdate(query);
	}
	public Borrowing borrowingFromResult(ResultSet rs) throws SQLException {
		if(rs == null)
			return null;
		Borrowing borrowing = new Borrowing();
		borrowing.setReaderId(rs.getString("id"));
		borrowing.setISBN(rs.getString("isbn"));
		borrowing.setBorrowDate(rs.getString("borrowdate"));
		borrowing.setReturnDate(rs.getString("returndate"));
		return borrowing;
	}
	public Boolean borrowBook(String readerId, String isbn, String date) throws SQLException{
		Book book = BookDAO.getDAO().findBookByISBN(isbn);
		Reader reader = ReaderDAO.getDAO().findReaderById(readerId);
		if(book.getStatus() == true && reader != null) {
			String query1 = "INSERT INTO borrowing VALUES(?, ?, ?, ?)";
			String query2 = "update book set status = false where isbn = ?";
			PreparedStatement ps1 = DatabaseConnection.getConnection().prepareStatement(query1);
			PreparedStatement ps2 = DatabaseConnection.getConnection().prepareStatement(query2);
			ps1.setString(1, isbn);
			ps1.setString(2, readerId);
			ps1.setString(3, date);
			ps1.setString(4, "");
			ps2.setString(1, isbn);
			return ps1.executeUpdate() > 0 && ps2.executeUpdate() > 0;
		}
		return false;
	}
	public Boolean returnBook(String readerId, String isbn, String date) throws SQLException {
		Book book = BookDAO.getDAO().findBookByISBN(isbn);
		Reader reader = ReaderDAO.getDAO().findReaderById(readerId);
		if(book.getStatus() == true && reader != null) {
			String query1 = "update borrowing set returndate = ? where isbn = ? and id = ?";
			String query2 = "update book set status = true where isbn = ?";
			PreparedStatement ps1 = DatabaseConnection.getConnection().prepareStatement(query1);
			PreparedStatement ps2 = DatabaseConnection.getConnection().prepareStatement(query2);
			ps1.setString(1, date);
			ps1.setString(2, isbn);
			ps1.setString(3, readerId);
			ps2.setString(1, isbn);
			return ps1.executeUpdate() > 0 && ps2.executeUpdate() > 0;
		}
		return false;
	}
	public ArrayList<Borrowing> findAllBorrowing() throws SQLException {
		ArrayList<Borrowing> borrowings = new ArrayList<>();
		String query = "SELECT * FROM borrowing";
		PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			borrowings.add(borrowingFromResult(rs));
		}
		return borrowings;
	}
	public ArrayList<Borrowing> findBorrowingByReaderId(String id) throws SQLException {
		ArrayList<Borrowing> borrowings = new ArrayList<>();
		String query = "SELECT * FROM borrowing where id = ?";
		PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			borrowings.add(borrowingFromResult(rs));
		}
		return borrowings;
	}
}
