package views.forms;

import models.Member;
import java.util.*;
import play.data.validation.ValidationError;


/**
* Backing class for the Member login form.
*
*/
public class LoginForm {

	public String username;
	public String password;

	public LoginForm() {
	}

	public LoginForm(String username, String password) {
		this.username = username;
		this.password = password;
	}


	/**
	* Form validation.
	* 
	* 
	*/
	public List<ValidationError> validate() {

		List<ValidationError> errors = new ArrayList<>();

		if (!Member.loginValid(username, password)) {
			errors.add(new ValidationError("password", "Incorrect username or password."));
		}

		if (errors.size() > 0)
			return errors;

		return null;
	}

}