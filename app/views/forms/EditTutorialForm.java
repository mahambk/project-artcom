package views.forms;

import models.Tutorial;
import java.util.*;
import play.data.validation.ValidationError;


/**
* Backing class for the edit post form.
*
*/
public class EditTutorialForm {

	public int id;
	public String title;
	public String intro;
	public String body;
	public String tags;
	public String category;



	public EditTutorialForm() {
	}

	public EditTutorialForm(int id, String title, String intro, String body, String tags, String category) {
		this.id = id;
		this.title = title;
		this.intro = intro;
		this.body = body;
		this.tags = tags;
		this.category = category;
	}


	/**
	* Form validation.
	* Checks if title and body are not empty
	* 
	*/
	public List<ValidationError> validate() {

		List<ValidationError> errors = new ArrayList<>();

		if (title == null || title.length() == 0) {
			errors.add(new ValidationError("title", "Please enter a title"));
		}

		if (body == null || body.length() == 0) {
			errors.add(new ValidationError("body", "Please insert tutorial body"));
		}

		return errors;
	}

}