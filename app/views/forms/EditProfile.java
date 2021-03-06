package views.forms;

import models.Member;
import java.util.*;
import play.data.validation.ValidationError;


/**
* Backing class for the edit profile form.
*
*/
public class EditProfile {

	public String firstname = "";
	public String lastname = "";
	public String email = "";
	public String level = "";
	public String location = "";
	public String skills = "";
	public String bio = "";


	public EditProfile() {
	}

	public EditProfile(String firstname, String lastname, String email, String level,
		String location, String skills, String bio) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.level = level;
		this.location = location;
		this.skills = skills;
		this.bio = bio;
	}


	/**
	* Form validation.
	* Checks for:
	* First name, last name, username and email are non-empty
	* 
	*/
	public List<ValidationError> validate() {

		List<ValidationError> errors = new ArrayList<>();

		if (firstname == null || firstname.length() == 0) {
			errors.add(new ValidationError("firstname", "Enter your first name"));
		}

		if (lastname == null || lastname.length() == 0) {
			errors.add(new ValidationError("lastname", "Enter your last name"));
		}

		String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		if (email == null || email.length() == 0) {
			errors.add(new ValidationError("email", "Enter your email"));
		} else if (!email.matches(emailRegex)) {
			errors.add(new ValidationError("email", "Please enter a valid email"));
		}

		return errors;
	}

}