package views.forms;

import models.Post;
import java.util.*;
import play.data.validation.ValidationError;


/**
* Backing class for the edit post form.
*
*/
public class EditPostForm {

	public int id;
	public String title;
	public String subtitle;
	public String description;
	public String tags;
	public String category;
	public boolean feedbackEnabled;


	public EditPostForm() {
	}

	public EditPostForm(int id, String title, String subtitle, String description, String tags,
		String category, boolean feedbackEnabled) {
		this.id = id;
		this.title = title;
		this.subtitle = subtitle;
		this.description = description;
		this.tags = tags;
		this.category = category;
		this.feedbackEnabled = feedbackEnabled;
	}


	/**
	* Form validation.
	* Checks if title is not empty
	* 
	*/
	public List<ValidationError> validate() {

		List<ValidationError> errors = new ArrayList<>();

		if (title == null || title.length() == 0) {
			errors.add(new ValidationError("title", "Please enter a post title"));
		}

		return errors;
	}

}