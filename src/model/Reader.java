package model;

public class Reader {
	private String ReaderId;
	private String Name;
	private String Phone;
	private String BirthDate;

	public Reader() {

	}
	public Reader(String ReaderId, String Name, String Phone, String BirthDate) {
		this.ReaderId = ReaderId;
		this.Name = Name;
		this.Phone = Phone;
		this.BirthDate = BirthDate;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getBirthDate() {
		return BirthDate;
	}

	public void setBirthDate(String birthDate) {
		BirthDate = birthDate;
	}

	public String getReaderId() {
		return ReaderId;
	}

	public void setReaderId(String readerId) {
		ReaderId = readerId;
	}
}
