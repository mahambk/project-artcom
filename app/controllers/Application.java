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

    public Result index() {
        return ok(index.render());
    }


    /**
     * Returns the login form page
     */
    public Result login() {
        LoginForm emptyForm = new LoginForm();
        Form<LoginForm> loginForm = Form.form(LoginForm.class).fill(emptyForm);
        return ok(login.render(loginForm, ""));
    }


    /**
     * Returns the signup form page
     */
    public Result signup() {
    	SignupForm emptyForm = new SignupForm();
    	Form<SignupForm> signupForm = Form.form(SignupForm.class).fill(emptyForm);
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
    		return ok(login.render(loginForm, "Username or password is incorrect."));
    	} else {
    		flash("success", "Form parsed with no errors.");
    		session().clear();
    		session("loggedIn", loginForm.get().username);
    		return redirect(routes.Profile.myProfile());
    	}
    }

    public Result logout() {
    	session().remove("loggedIn");
    	return redirect(routes.Application.index());
    }

    public static boolean isLoggedIn() {
    	return session().containsKey("loggedIn");
    }

    public static boolean memberLoggedIn(Member member) {
        if (session().containsKey("loggedIn")) {
             String currentMember = session().get("loggedIn");
             if (currentMember.equals(member.username)) {
                return true;
             }
        }
        return false;
    }

    /**
    * Processes the signup form
    * @return
    */
    public Result submitSignup() {
    	Form<SignupForm> signupForm = Form.form(SignupForm.class).bindFromRequest();

    	if (signupForm.hasErrors()) {
    		flash("error", "Please complete above fields.");
    		//String errorMsg = signupForm.errorsAsJson().toString();
    		String errorMsg = "Please complete the above fields.";
    		return ok(signup.render(signupForm, errorMsg));
    	} else {
    		Member member = Member.createInstance(signupForm.get());
    		flash("success", "Form parsed with no errors.");
            session().clear();
            session("loggedIn", signupForm.get().username);
            return redirect(routes.Profile.myProfile());
    		/* For testing
            String confirmation = "Form parsed successfully. Member instance created: " + member.toString();
    		return ok(signup.render(signupForm, confirmation));*/
    	}
    }

}