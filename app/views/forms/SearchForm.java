package views.forms;

import models.Member;
import java.util.*;
import play.data.validation.ValidationError;


/**
* Backing class for the Member login form.
*
*/
public class SearchForm {

	public String query;

	public SearchForm() {
	}

	public SearchForm(String query) {
		this.query = query;
	}
}