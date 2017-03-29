package controllers;

import play.mvc.*;

import views.html.*;

/**
 */
public class Browse extends Controller {

    /**
     */
    public Result index() {
        return ok(browse.render("drawing"));
    }

    public Result byCategory(String category) {
        return ok(browse.render(category));
    }

    public Result discover() {
        return TODO;
    }


}
