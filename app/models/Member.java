package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.persistence.*;
//import java.sql.Date;
import views.forms.SignupForm;
import views.forms.EditProfileForm;
import libs.BCrypt;

@Entity
public class Member extends Model {

	@Id
	public String username;
	public String firstname;
	public String lastname;
	public String email;
	public String password;
	public Date dob;
	public String level;
	
	public String bio;
	public String profilePic;
	public String skills;
	public String location;

	public Date dateJoined;


	public static Find<String,Member> find = new Find<String,Member>(){};

	public Member() {
	}

	public Member(String firstname, String lastname, String email, String username, String password,
		String birthday, String birthmonth, String birthyear, String level) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.username = username;
		this.password = password;
		this.dateJoined = new Date();
		this.setDob(birthday, birthmonth, birthyear);
		this.level = level;
	}

	public static List<Member> findAll() {
		return find.all();
	}

	public static Member findByUsername(String username) {
		return find.byId(username);
	}

	public static PagedList<Member> findAllPagedList(int page, int pageSize) {
		return find.findPagedList(page, pageSize);
	}

	public static PagedList<Member> findSearchPage(int page, int pageSize, String query) {
		return find.where()
		  .or()
		    .and()
		      .ilike("username", "%" + query + "%")
		      .endJunction()
		    .and()
		      .ilike("firstname", "%" + query + "%")
		      .endJunction()
		    .and()
		      .ilike("lastname", "%" + query + "%")
		      .endJunction()
		     .and()
		       .ilike("skills", "%" + query + "%")
		       .endJunction()
		     .and()
		       .ilike("location", "%" + query + "%")
		       .endJunction()
		    .findPagedList(page, pageSize);
	}

	public String toString() {
		return String.format("[Username: '%s', Password: '%s', First name: '%s', Last name: '%s', Email: '%s',"
			+ "Dob: '%s', Level: '%s']", this.username, this.password, this.firstname, this.lastname, 
			this.email, "", this.level);
	}

	public static boolean loginValid(String username, String password) {
		Member member = findByUsername(username);
		if (member != null) {
			if (member.password.equals(password)) {
				return true;
			}
		} 
		return false;		
	}

	public static boolean validateLogin(String username, String password) {
		Member member = findByUsername(username);
		if (member != null) {
			if (BCrypt.checkpw(password, member.password)) {
				return true;
			}
		} 
		return false;
	}

	public static Member createInstance(SignupForm signupForm) {
		// Create password hash
		String hashedPassword = BCrypt.hashpw(signupForm.password, BCrypt.gensalt());

		// TODO: ADD DATE
		Member newMember = new Member(signupForm.firstname, signupForm.lastname, signupForm.email, signupForm.username,
			hashedPassword, signupForm.birthday, signupForm.birthmonth, signupForm.birthyear, signupForm.level);
		newMember.save();
		return newMember;
	}

	public void setDob(String day, String month, String year) {
		String date = day + "-" + month  + "-" + year;
		SimpleDateFormat dateFormat = new SimpleDateFormat("d-MM-yyyy");
		try {
			this.dob = dateFormat.parse(date);
		} catch (java.text.ParseException e) {
 			//this.dob = new Date();
 			java.io.StringWriter sw = new java.io.StringWriter();
			java.io.PrintWriter pw = new java.io.PrintWriter(sw);
			e.printStackTrace(pw);
			this.skills = sw.toString(); // stack trace as a string
		}
	}

	public static boolean usernameExists(String username) {
		List<Member> memberWithSameUsername = find.where().eq("username", username).findList();
		if (!(memberWithSameUsername.size() > 0)) {
			return false;
		}
		return true;
	}

	public void resetProfilePic() {
		this.profilePic = "images/profile-pics/sample.jpg";
        this.save();
	}

	public void updateInfo(EditProfileForm profileForm) {
		this.firstname = profileForm.firstname;
		this.lastname = profileForm.lastname;
		this.email = profileForm.email;
		this.level =  profileForm.level;
		this.location = profileForm.location;
		this.skills = profileForm.skills;
		this.bio = profileForm.bio;
		this.save();
	}

}