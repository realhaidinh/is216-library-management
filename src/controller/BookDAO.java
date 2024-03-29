package controller;
import model.Book;
import java.sql.*;
import java.util.ArrayList;

public class BookDAO {
	private static BookDAO dao = null;
	private BookDAO() {

	}
	public static BookDAO getDAO() {
		if(dao == null) {
			dao = new BookDAO();
		}
		return dao;
	}
	public static void createBookTable() throws SQLException {
		String query = "create table if not exists book " +
				"(isbn string primary key, title string, author string, category string, publisher string, publishdate integer, status boolean)";
		Statement statement = DatabaseConnection.getConnection().createStatement();
		statement.executeUpdate(query);
	}
	public Book bookFromResult(ResultSet rs) throws SQLException{
		if(rs == null) {
			return null;
		}
		Book book = new Book();
		book.setISBN(rs.getString("isbn"));
		book.setTitle(rs.getString("title"));
		book.setAuthor(rs.getString("author"));
		book.setCategory(rs.getString("category"));
		book.setPublisher(rs.getString("publisher"));
		book.setPublishDate(rs.getInt("publishdate"));
		book.setStatus(rs.getBoolean("status"));
		return book;
	}
	public void setBookPreparedStatement(PreparedStatement ps, Book book) throws SQLException {
		ps.setString(1, book.getISBN());
		ps.setString(2, book.getTitle());
		ps.setString(3, book.getAuthor());
		ps.setString(4, book.getCategory());
		ps.setString(5, book.getPublisher());
		ps.setInt(6, book.getPublishDate());
	}
	public boolean addBook(Book book){
		String query = "insert into book values(?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			setBookPreparedStatement(ps, book);
			ps.setBoolean(7, book.getStatus());
			return ps.executeUpdate() > 0;
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}
	public Book findBookByISBN(String ISBN) {
		String query = "SELECT * FROM book WHERE isbn = ?";
		Book book = null;
		try{
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			ps.setString(1, ISBN);
			ResultSet rs = ps.executeQuery();
			book = bookFromResult(rs);
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		return book;
	}
	public Boolean deleteByISBN(String isbn) {
		String query = "delete from book where isbn = ?";
		try {
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			ps.setString(1, isbn);
			return ps.executeUpdate() > 0;
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}

	public ArrayList<Book> findAllBook() {
		ArrayList<Book> books = new ArrayList<>();
		String query = "SELECT * FROM book";
		try {
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				books.add(bookFromResult(rs));
			}
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return books;
	}
	public ArrayList<Book> findBookByCategory(String Category){
		ArrayList<Book> books = new ArrayList<>();
		String query = "SELECT * FROM book WHERE category LIKE ?";
		try{
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			ps.setString(1, Category + "%");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				books.add(bookFromResult(rs));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return books;
	}
	public ArrayList<Book> findBookByTitle(String Title){
		ArrayList<Book> books = new ArrayList<>();
		String query = "SELECT * FROM book WHERE title LIKE ?";
		try {
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			ps.setString(1, Title + "%");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				books.add(bookFromResult(rs));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());;
		}
		return books;
	}
	public ArrayList<Book> findBookByPublisher(String publisher){
		ArrayList<Book> books = new ArrayList<>();
		String query = "SELECT * FROM book WHERE publisher LIKE ?";
		try{
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			ps.setString(1, publisher + "%");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				books.add(bookFromResult(rs));
			}
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		return books;
	}
	public ArrayList<Book> findBookByPublishDate(String date){
		ArrayList<Book> books = new ArrayList<>();
		String query = "SELECT * FROM book WHERE publishdate LIKE ?";
		try{
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			ps.setInt(1, Integer.parseInt(date));
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				books.add(bookFromResult(rs));
			}
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		return books;
	}
	public ArrayList<Book> findBookByAuthor(String author){
		ArrayList<Book> books = new ArrayList<>();
		String query = "SELECT * FROM book WHERE author LIKE ?";
		try{
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			ps.setString(1, author + "%");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				books.add(bookFromResult(rs));
			}
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		return books;
	}
	public ArrayList<Book> findBookByStatus(String status){
		ArrayList<Book> books = new ArrayList<>();
		String query = "SELECT * FROM book WHERE status = ?";
		try{
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			ps.setBoolean(1, status.equals("Sẵn có") ? true : false);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				books.add(bookFromResult(rs));
			}
		} catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		return books;
	}
	public Boolean updateByISBN(String isbn, Book book) {
		String query = "update book " +
				"set isbn = ?, title = ?, author = ?, category = ?, publisher = ?, publishdate = ?" +
				"where isbn = ?";
		try {
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query);
			setBookPreparedStatement(ps, book);
			ps.setString(7, isbn);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}
}
