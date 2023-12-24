package model;

public class Book {
	private String ISBN;
	private String Title;
	private String Author;
	private String Category;
	private String Publisher;
	private int PublishDate;
	private Boolean Status;
	public Book () {

	}
	public Book(String ISBN, String Title, String Author, String Category, String Publisher, int PublishDate, Boolean Status) {
		this.ISBN = ISBN;
		this.Title = Title;
		this.Author = Author;
		this.Category = Category;
		this.Publisher = Publisher;
		this.PublishDate = PublishDate;
		this.Status = Status;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getAuthor() {
		return Author;
	}

	public void setAuthor(String author) {
		Author = author;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}

	public String getPublisher() {
		return Publisher;
	}

	public void setPublisher(String publisher) {
		Publisher = publisher;
	}

	public int getPublishDate() {
		return PublishDate;
	}

	public void setPublishDate(int publishDate) {
		PublishDate = publishDate;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String ISBN) {
		this.ISBN = ISBN;
	}

	public Boolean getStatus() {
		return Status;
	}

	public void setStatus(Boolean status) {
		Status = status;
	}
}
