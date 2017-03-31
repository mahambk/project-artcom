package models;

import com.avaje.ebean.Model;

import java.util.*;
import javax.persistence.*;
//import java.sql.Date;
import views.forms.MemberSignupForm;
import views.forms.EditProfileForm;

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
		String dob, String level) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.username = username;
		this.password = password;
		this.dateJoined = new Date();
		//this.dob = Date.valueOf(dob);
		this.level = level;

		// auto-populate remaining attributes (until update mechanism is implemented)
		this.skills = "Botanical art, photography, watercolor painting.";
		this.location = "Fairy land";
		this.bio = "Hello and welcome to my profile. I like helping the dwarfs clean their messy home and singing along with the creatures of the woodland. I occasionally draw things.";

	}

	public static List<Member> findAll() {
		return find.all();
	}

	public static Member findByUsername(String username) {
		return find.byId(username);
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

	public static Member createInstance(MemberSignupForm signupForm) {
		Member newMember = new Member(signupForm.firstname, signupForm.lastname, signupForm.email, signupForm.username,
			signupForm.password, "", signupForm.level);
		newMember.save();
		return newMember;
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