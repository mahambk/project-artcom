package controllers;

import play.mvc.*;
import play.data.Form;
import views.html.*;
import views.forms.*;
import models.Member;

/**
 * 
 */
public class Application extends Controller {

    /**
     * Returns the login form page
     */
    public Result login() {
        return ok(login.render(""));
    }

    /**
     * Returns the signup form page
     */
    public Result signup() {
    	MemberSignupForm emptyForm = new MemberSignupForm();
    	Form<MemberSignupForm> signupForm = Form.form(MemberSignupForm.class).fill(emptyForm);
        return ok(signup.render(signupForm, ""));
    }

    /**
    * Processes the login form
    * @return
    */
    public Result submitLogin() {
    	Form<LoginForm> loginForm = Form.form(LoginForm.class).bindFromRequest();

    	if (loginForm.hasErrors()) {
    		flash("error", "Username or password is incorrect.");
    		return badRequest(login.render("Username or password is incorrect."));
    	} else {
    		flash("success", "Form parsed with no errors.");
    		session().clear();
    		session("loggedIn", loginForm.get().username);
    		return ok(home.render());
    	}
    }

    public Result logout() {
    	session().remove("loggedIn");
    	return ok(home.render());
    }

    public static boolean isLoggedIn() {
    	return session().containsKey("loggedIn");
    }

    /**
    * Processes the signup form
    * @return
    */
    public Result submitSignup() {
    	Form<MemberSignupForm> signupForm = Form.form(MemberSignupForm.class).bindFromRequest();

    	if (signupForm.hasErrors()) {
    		flash("error", "Please complete above fields.");
    		//String errorMsg = signupForm.errorsAsJson().toString();
    		String errorMsg = "Please complete the above fields.";
    		return badRequest(signup.render(signupForm, errorMsg));
    	} else {
    		Member member = Member.createInstance(signupForm.get());
    		flash("success", "Form parsed with no errors.");
            session().clear();
            session("loggedIn", signupForm.get().username);
            return ok(home.render());
    		/* For testing
            String confirmation = "Form parsed successfully. Member instance created: " + member.toString();
    		return ok(signup.render(signupForm, confirmation));*/
    	}
    }

}
