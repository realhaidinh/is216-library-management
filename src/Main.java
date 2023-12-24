import controller.BookDAO;
import controller.BorrowingDAO;
import controller.ReaderDAO;
import view.*;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) {
		try {
			BookDAO.createBookTable();
			ReaderDAO.createReaderTable();
			BorrowingDAO.createBorrowingTable();
			new MainFrame().setTitle("Chương trình quản lý thư viện");
		}
		catch(SQLException e) {
			System.err.println(e.getMessage());
		}
	}
}