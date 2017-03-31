package controllers;

import play.mvc.*;
import play.data.Form;
import models.Member;
import views.html.*;
import views.forms.EditProfileForm;


/**
 */
public class Profile extends Controller {

    /**
     */
    public Result index(String username) {
    	if (username != null && !username.isEmpty()) {
    		Member member = Member.findByUsername(username);
                if(member != null) {
        	       return ok(profile.render(member));
                }
    	}
    	return badRequest(home.render());
    }

    public Result myProfile() {
    	if(Application.isLoggedIn()) {
    		Member member = Member.findByUsername(session().get("loggedIn"));
        	return ok(profile.render(member));
    	} else {
    		return badRequest(home.render());
    	}
    }

    public Result editProfile() {
        if(Application.isLoggedIn()) {
            Member member = Member.findByUsername(session().get("loggedIn"));

            EditProfileForm currentProfileInfo = new EditProfileForm(member.firstname, member.lastname,
            member.email, member.level, member.location, member.skills, member.bio);

            Form<EditProfileForm> profileForm = Form.form(EditProfileForm.class).fill(currentProfileInfo);

            return ok(editProfile.render(profileForm, member));
        } else {
            return badRequest(home.render());
        }
    }

    public Result submitChanges() {
        Member member = Member.findByUsername(session().get("loggedIn"));
        Form<EditProfileForm> profileForm = Form.form(EditProfileForm.class).bindFromRequest();

        if (profileForm.hasErrors()) {
            flash("error", "Please complete above fields.");

            return ok(editProfile.render(profileForm, member));
        } else {
            member.updateInfo(profileForm.get());
            return ok(profile.render(member));
        }
    }


}
