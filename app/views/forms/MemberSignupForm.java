package views.forms;

import models.Member;
import java.util.*;
import play.data.validation.ValidationError;


/**
* Backing class for the Member signup form.
*
*/
public class MemberSignupForm {

	public String firstname = "";
	public String lastname = "";
	public String email = "";
	public String username = "";
	public String password = "";
	public String passwordRepeat = "";
	public String dob = "";
	public String birthday = "";
	public String birthyear = "";
	public String level = "";


	public MemberSignupForm() {
	}

	public MemberSignupForm(String firstname, String lastname, String email, String username,
		String password, String passwordRepeat, String birthday, String birthmonth, String birthyear, String level) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.username = username;
		this.password = password;
		this.passwordRepeat = passwordRepeat;
		this.dob = birthyear + "-" + birthmonth + "-" + birthday;
		this.birthday = birthday;
		this.birthyear = birthyear;
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

		if (email == null || email.length() == 0) {
			errors.add(new ValidationError("email", "Enter your email"));
		}

		if (username == null || username.length() == 0) {
			errors.add(new ValidationError("username", "Enter a valid username"));
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


		/*else {
			int intBirthDay = Integer.parseInt(birthday);
			int intBirthYear = Integer.parseInt(birthyear);
			if (intBirthDay < 1 || intBirthDay > 31
				|| intBirthYear < 1900 || intBirthYear > 2017) {
				errors.add(new ValidationError("birthday", "Please enter a valid date."));
			}
		}*/

		if (errors.size() > 0)
			return errors;

		return null;
	}

}