package views.forms;

import models.Member;
import java.util.*;
import play.data.validation.ValidationError;


/**
* Backing class for the Member signup form.
*
*/
public class SignupForm {

	public String firstname = "";
	public String lastname = "";
	public String email = "";
	public String username = "";
	public String password = "";
	public String passwordRepeat = "";
	public String birthday = "";
	public String birthyear = "";
	public String birthmonth = "";
	public String level = "";


	public SignupForm() {
	}

	public SignupForm(String firstname, String lastname, String email, String username,
		String password, String passwordRepeat, String birthday, String birthmonth, String birthyear, String level) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.username = username;
		this.password = password;
		this.passwordRepeat = passwordRepeat;
		this.birthday = birthday;
		this.birthyear = birthyear;
		this.birthmonth = birthmonth;
		this.level = level;
	}


	/**
	* Form validation.
	* Checks for:
	* First name, last name, username and email, date non-empty
	* Password is at least eight characters long
	* Password repeat matches password
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

		if (username == null || username.length() == 0) {
			errors.add(new ValidationError("username", "Enter a valid username"));
		} else if (Member.usernameExists(username)) {
			// check if username already exists in db
			//errors.add(new ValidationError("username"), "This username is already taken"));
			errors.add(new ValidationError("username", "This username is already taken"));

		}
		
		if (password == null || password.length() < 8) {
			errors.add(new ValidationError("password", "Password must have at least 8 characters."));
		} else if (!password.equals(passwordRepeat)) {
			//String msg = "Password: " + password + ", Password Repeat: " + passwordRepeat;
			errors.add(new ValidationError("passwordRepeat", "Passwords do not match"));
		}

		
		if (birthday == null || birthday.length() == 0) {
			errors.add(new ValidationError("birthday", "Please enter a date."));
		} else if(!(birthday.matches("[\\d][\\d]") || birthday.matches("[\\d]"))
				|| birthday.equals("0")) {
				errors.add(new ValidationError("birthday", "Please enter a valid birth date."));
		}

		if (birthyear == null || birthyear.length() == 0) {
			errors.add(new ValidationError("birthyear", "Please enter a date."));
		} else if(!birthyear.matches("[\\d][\\d][\\d][\\d]")) {
				errors.add(new ValidationError("birthyear", "Please enter a valid birth date."));
		}


		if (errors.size() > 0)
			return errors;

		return null;
	}

}