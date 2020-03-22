package ar.edu.unlp.info.bd2.model;

import java.util.Date;

public class User {
	
	private String email;
	private String password;
	private String username;
	private String name;
	private Date dateOfBirth;
	
	
	public User(String email, String password, String username, String name, Date dateOfBirth) {
		super();
		this.email = email;
		this.password = password;
		this.username = username;
		this.name = name;
		this.dateOfBirth = dateOfBirth;
	}

	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	
}
