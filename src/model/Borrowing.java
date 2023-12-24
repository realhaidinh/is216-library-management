package model;

public class Borrowing {
	private String ISBN;
	private String ReaderId;
	private String BorrowDate;
	private String ReturnDate;
	public Borrowing() {

	}
	public Borrowing(String ISBN, String ReaderId, String BorrowDate, String ReturnDate) {
		this.ISBN = ISBN;
		this.ReaderId = ReaderId;
		this.BorrowDate = BorrowDate;
		this.ReturnDate = ReturnDate;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String ISBN) {
		this.ISBN = ISBN;
	}

	public String getReaderId() {
		return ReaderId;
	}

	public void setReaderId(String readerId) {
		ReaderId = readerId;
	}

	public String getBorrowDate() {
		return BorrowDate;
	}

	public void setBorrowDate(String borrowDate) {
		BorrowDate = borrowDate;
	}

	public String getReturnDate() {
		return ReturnDate;
	}

	public void setReturnDate(String returnDate) {
		ReturnDate = returnDate;
	}
}
