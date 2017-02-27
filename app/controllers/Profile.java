package controllers;

import play.mvc.*;
import models.Member;
import views.html.*;

/**
 */
public class Profile extends Controller {

    /**
     */
    public Result index(String username) {
    	if (username != null && !username.isEmpty()) {
    		Member member = Member.findByUsername(username);
        	return ok(profile.render(member));
    	} else {
    		return badRequest(home.render());
    	}
    }

    public Result myProfile() {
    	if(Application.isLoggedIn()) {
    		Member member = Member.findByUsername(session().get("username"));
        	return ok(profile.render(member));
    	} else {
    		return badRequest(home.render());
    	}
    }


}
