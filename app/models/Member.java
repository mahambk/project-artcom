package models;

import com.avaje.ebean.Model;

import java.util.*;
import javax.persistence.*;

@Entity
public class Member extends Model {
	
	@Id
	public String username;
	public String password;
	public String firstName;
	public String lastName;
	public String email;
	public Date dateJoined;
	public Date dob;
	public String bio;
	public String profilePic;

	public static Find<String,Member> find = new Find<String,Member>(){};


	public Member() {

	}

	/*public static List<Member> findAll() {

	}*/


}